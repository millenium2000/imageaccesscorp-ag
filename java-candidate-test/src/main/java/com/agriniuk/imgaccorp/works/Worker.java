package com.agriniuk.imgaccorp.works;

import java.util.LinkedList;

public class Worker<T extends Work> {
	
	
	private class WorkThread extends Thread {
		@Override
		public void run() {
			//TODO
			
			synchronized(queue) {
				while(queue.isEmpty()) {
					queue.wait();
				}
				
			}
			
		}
	}
	
	
	private class WorkWrapper {
		public final T work;
		public WorkWrapper(T work) {
			this.work = work;
		}
		public void start() {
			try {
				this.work.start();
			} catch (Exception e) {
				//TODO: log
			}
		}
	}
	
	private final LinkedList<WorkWrapper> queue = new LinkedList<>();
	
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
	
	
	public synchronized boolean add(T work) {
		
	}
	
	
	public synchronized void start() {
		
	}
	
	
	public synchronized void stop() {
		
	}
	
	
	public WorkerState getState() {
		
	}
	
}
