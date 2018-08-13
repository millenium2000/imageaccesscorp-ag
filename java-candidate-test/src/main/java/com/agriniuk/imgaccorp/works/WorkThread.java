package com.agriniuk.imgaccorp.works;

import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The worker thread. This is where actual {@link Work#start()} is done.<br/>
 * 
 * Instances of this Thread will be created on {@link Worker#start()} call,
 * and will be pulling work off the queue. When queue becomes empty, they will
 * fall into 'waiting' state (waiting on that queue).<br/><br/>
 * 
 * If a new work is added into the queue (with the {@link Worker#add(Work)}), 
 * all waiting threads will be awoken.<br/><br/>
 * 
 * Before pulling a new {@link Work}, the thread will check the {@link #toStop} variable.
 * If it was set (should really be only done by the {@link Worker#stop()} function,
 * the tread will terminate (even if there are more items in the queue).
 */
class WorkThread<T extends Work> extends Thread {
	
	private static final Logger log = LoggerFactory.getLogger(WorkThread.class);
	
	private final LinkedList<WorkWrapper<T>> queue;
	
	public volatile boolean toStop = false;
	
	
	public WorkThread(String name, LinkedList<WorkWrapper<T>> queue) {
		super(name);
		this.queue = queue;
	}
	
	
	@Override
	public void run() {
		while(true) {
			//retrieve next Work or wait until someone wakes us up
			WorkWrapper<T> work = null;
			while(true) {
				//in case there are items in the queue, but the Worker wants to stop processing
				if (toStop) {
					log.trace("found toStop==true. exiting.");
					return;
				}
				synchronized(queue) {
					//get next item to process
					if ( ! queue.isEmpty()) {
						work = queue.removeFirst();
						break;
					}
					//or wait
					try {
						log.trace("no work found. waiting.");
						queue.wait();
					} catch (InterruptedException e) {
						//we got interrupted while waiting..
						log.trace("interrupted from waiting");
						continue;
					}
				}
			}
			
			//do this work
			log.debug("found more work. executing.");
			work.execute();
		}
	}

}
