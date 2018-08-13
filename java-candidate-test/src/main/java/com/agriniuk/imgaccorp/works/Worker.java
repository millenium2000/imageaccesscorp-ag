package com.agriniuk.imgaccorp.works;

import java.util.HashSet;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO: comment
public class Worker<T extends Work> {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
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
		int a = 0;
		for (T work : seq) {
			add(work);
			a++;
		}
		//yes yes.. the size printed here may already be off
		log.debug("added more work {} items (queue size roughly should be {})", a, queue.size());
	}
	
	
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
	
	
	public WorkerState getState() {
		return state;
	}
	
}
