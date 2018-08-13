package com.agriniuk.imgaccorp.works;

import java.util.HashSet;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Worker<T extends Work> {
	
	private static final Logger log = LoggerFactory.getLogger(Worker.class);
	
	private final LinkedList<WorkWrapper<T>> queue = new LinkedList<>();
	
	private final HashSet<WorkThread<T>> threads = new HashSet<>();
	
	private volatile WorkerState state = WorkerState.INITIAL;
	
	private final int queueMaxSize;
	
	private final boolean isDaemon;
	
	private final int threadsMaxAmount;
	
	
	public Worker(int queueMaxSize, int threadsMaxAmount, boolean isDaemon) {
		this.queueMaxSize = queueMaxSize;
		this.isDaemon = isDaemon;
		this.threadsMaxAmount = threadsMaxAmount;
	}
	
	
	public Worker() {
		this(30, 3, false);
	}
	
	
	public synchronized void addAll(Iterable<T> seq) {
		for (T work : seq)
			add(work);
	}
	
	
	public synchronized void add(T work) {
		switch(getState()) {
			case INITIAL:
			case OPERATIONAL:
				synchronized (queue) {
					if (queue.size()>=queueMaxSize)
						throw new IllegalStateException("Queue is full.");
					queue.addLast(new WorkWrapper<T>(work));
					queue.notifyAll();
				}
				break;
			case STOPPED:
				throw new IllegalStateException("You can not add more Work while in STOPPED state");
		}
	}
	
	
	//TODO: comment
	public synchronized void start() {
		if (getState() == WorkerState.OPERATIONAL)
			throw new IllegalStateException("State is already 'OPERATIONAL'");
		
		for (int i=0; i<threadsMaxAmount; i++) {
			WorkThread<T> thread = new WorkThread<T>("WorkThread_"+i, queue);
			thread.setDaemon(isDaemon);
			threads.add(thread);
		}
		
		for (WorkThread<T> thread : threads)
			thread.start();
		
		state = WorkerState.OPERATIONAL;
		log.debug("started {}", this.getClass().getSimpleName());
	}
	
	
	//TODO: fix comment
	/**
	 * Puts the Worker into {@link WorkerState#STOPPED} state.<br/>
	 * User can still call {@link #start()} to move this back to {@link WorkerState#OPERATIONAL}. <br/> 
	 * 
	 * No more Works will be accepted. Depending on {@code waitForComplete} 
	 * flag, the method may wait for already started (active) Works to finish.<br/>
	 * 
	 * After method returns, if you set {@code waitForComplete=true} there should be
	 * no any internal threads left. If you set it to {@code false}, the method returns right away,
	 * leaving the currently executing threads to finish their started Works and then terminate
	 * (so, there will be some running threads for some period of time).<br/>
	 * 
	 * Notice, this may leave some some Works in the internal queue hanging
	 * (if those Works hadn't started by the time of calling this method).
	 * 
	 * @param waitForComplete weather to "wait for currently executing 
	 * 		jobs to finish and then return" or "return right away"
	 */
	public synchronized void stop() {
		if (getState() != WorkerState.OPERATIONAL)
			throw new IllegalStateException("State must be OPERATIONAL to call this method");
		
		for (WorkThread<T> thread : threads) {
			thread.toStop = true;
			thread.interrupt();
		}
		
		try {
			for (WorkThread<T> thread : threads)
				thread.join();
		} catch (InterruptedException e) {
			//TODO: log
		}
		
		threads.clear();
		state = WorkerState.STOPPED;
		log.debug("stopped {}", this.getClass().getSimpleName());
	}
	
	
	public WorkerState getState() {
		return state;
	}
	
}
