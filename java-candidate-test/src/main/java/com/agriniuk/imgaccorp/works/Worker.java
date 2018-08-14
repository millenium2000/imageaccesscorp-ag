package com.agriniuk.imgaccorp.works;

import java.util.HashSet;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Generic implementation of thread pool service<br/>
 * 
 * This is the main class which handles the {@link Work} sub-classes,
 * internal thread pool (processing {@link Work} items), and a life cycle of them.<br/>
 * 
 * The general usage pattern:
 * <pre>
 * 
 *  1) create a sub-class of {@link Work} and implement {@link Work#start()}:<code>
 *  	public class SomeWork extends Work {
 *  		public void start() { /* do something * / }
 *  	}</code>
 *  
 *  2) optionally crate a sub-class of the Worker:<code>
 *  	public class SomeWorker extends Worker<SomeWork> {
 *  	// you don't really need to implement any methods.
 *  	}</code>
 *  
 *  3) in your main application you can do:
 *  {@code Worker<SomeWork> worker = new SomeWorker(); }
 *  or
 *  {@code Worker<SomeWork> worker = new Worker<>(); }
 *  if you don't need to override any SomeWorker functionality
 *  
 *  4) add some work:
 *  {@code worker.add(new SomeWork());}
 *  
 *  5) start worker:
 *  {@code worker.start();}
 *  
 *  6) you may add some more work
 *  
 *  7) to terminate, call {@code worker.stop()}
 *  
 *  8) after that you can call {@code worker.start()} again.
 * </pre>
 * 
 * @param <T> subclass of {@link Work}
 */
public class Worker<T extends Work> {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final LinkedList<WorkWrapper<T>> queue = new LinkedList<>();
	
	private final HashSet<WorkThread<T>> threads = new HashSet<>();
	
	private volatile WorkerState state = WorkerState.INITIAL;
	
	private final int queueMaxSize;
	
	private final boolean isDaemon;
	
	private final int threadsMaxAmount;
	
	
	/**
	 * Create new Worker instance.<br/>
	 * 
	 * It is stated in {@link WorkerState#INITIAL} state in which 
	 * no processing is done, but it can accept new {@link Work} which will get
	 * put into internal queue. User should call {@link #start()} to start
	 * internal processing threads.
	 * 
	 * @param queueMaxSize maximum number of {@link Work} items to store
	 * @param threadsMaxAmount maximum number of processing threads
	 * @param isDaemon whether processing threads will be started as daemons (see {@link Thread#setDaemon(boolean)})
	 */
	public Worker(int queueMaxSize, int threadsMaxAmount, boolean isDaemon) {
		this.queueMaxSize = queueMaxSize;
		this.isDaemon = isDaemon;
		this.threadsMaxAmount = threadsMaxAmount;
	}
	
	
	/**
	 * Default constructor.<br/>
	 * Equivalent of {@link #Worker(int, int, boolean) Worker(30, 3, false)}
	 */
	public Worker() {
		this(30, 3, false);
	}
	
	
	/**
	 * Adds new {@link Work} item.<br/>
	 * 
	 * It can only be done while the Worker is in {@link WorkerState#INITIAL} or {@link WorkerState#OPERATIONAL} states.
	 * The items is being put into internal queue, and is processed as soon as worker threads gets to it.<br/>
	 * 
	 * There is a limit on number of items in the queue (set during creation of the Worker).
	 * If you accede the limit an {@link IllegalStateException} will be thrown. 
	 * 
	 * @param work new work items to add (subclass of {@link Work})
	 * @throws IllegalStateException if you accede the queue limit
	 */
	public synchronized void add(T work) {
		log.trace("adding more work");
		int a = -1;
		switch(getState()) {
			case INITIAL:
			case OPERATIONAL:
				synchronized (queue) {
					if (queue.size()>=queueMaxSize)
						throw new IllegalStateException("Queue is full.");
					queue.addLast(new WorkWrapper<T>(work));
					a = queue.size();
					queue.notifyAll();
				}
				break;
			case STOPPED:
				throw new IllegalStateException("You can not add more Work while in STOPPED state");
		}
		log.debug("added more work (queue size is {})", a);
	}
	
	
	/**
	 * Starts worker threads to process existing or new {@link Work}.<br/>
	 * 
	 * This also puts the Worker into {@link WorkerState#OPERATIONAL} state
	 * in which it can accept new {@link Work} with {@link #add(Work)} function.<br/>
	 * 
	 * In order to stop processing, a user can call {@link #stop()}.
	 */
	public synchronized void start() {
		if (getState() == WorkerState.OPERATIONAL)
			throw new IllegalStateException("State is already 'OPERATIONAL'");
		
		log.trace("creating workign threads");
		for (int i=0; i<threadsMaxAmount; i++) {
			WorkThread<T> thread = new WorkThread<T>("WorkThread_"+i, queue);
			thread.setDaemon(isDaemon);
			threads.add(thread);
		}
		
		int a = queue.size();
		
		log.trace("starting working threads");
		for (WorkThread<T> thread : threads)
			thread.start();
		
		state = WorkerState.OPERATIONAL;
		log.debug("started with {} threads (queue size was {})", threads.size(), a);
	}
	
	
	/**
	 * Stops further processing and accepting of new {@link Work}.<br/>
	 * 
	 * Terminates all processing threads (waiting for the currently executing ones to finish).
	 * Puts the Worker into {@link WorkerState#STOPPED} state.
	 * After this function returns, this Worker does not have any threads running processing work.
	 * Obviously, there might be some work items left in the queue.
	 * 
	 * User can still call {@link #start()} to move this back to {@link WorkerState#OPERATIONAL}
	 * and to start processing of {@link Work}s again. <br/> 
	 */
	public synchronized void stop() {
		if (getState() != WorkerState.OPERATIONAL)
			throw new IllegalStateException("State must be OPERATIONAL to call this method");
		
		log.trace("stopping working threads");
		for (WorkThread<T> thread : threads) {
			thread.toStop = true;
			thread.interrupt();
		}
		
		log.trace("waiting for working threads to finish");
		try {
			for (WorkThread<T> thread : threads)
				thread.join();
		} catch (InterruptedException e) {
			log.error("interrupted while waiting for working threads to finish", e);
		}
		
		int a = threads.size();
		int b = queue.size();
		threads.clear();
		state = WorkerState.STOPPED;
		log.debug("stopped {} working threads (queue size is {})", a, b);
	}
	
	
	/** Returns the current state. */
	public WorkerState getState() {
		return state;
	}
	
}
