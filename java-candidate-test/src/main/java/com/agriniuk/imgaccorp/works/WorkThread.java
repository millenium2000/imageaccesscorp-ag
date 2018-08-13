package com.agriniuk.imgaccorp.works;

import java.util.LinkedList;

//TODO: fix comment

/**
 * The worker thread. This is where actual work is done.<br/>
 * 
 * Instances of this Thread will be pulling work off the queue,
 * falling into 'waiting' state (on that queue) if there is no more work.<br/><br/>
 * 
 * If a work is added into the queue, someone has to wake up these threads.<br/><br/>
 * 
 * Upon interruption, the tread exits (if it is processing a Work, it will exits
 * after this Work processing is finished, even if there are more items in the queue).
 * 
 * @author Alexandre_Griniuk
 *
 */
public class WorkThread<T extends Work> extends Thread {
	
	
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
				if (toStop)
					return;
				synchronized(queue) {
					//get next item to process
					if ( ! queue.isEmpty()) {
						work = queue.removeFirst();
						break;
					}
					//or wait
					try {
						queue.wait();
					} catch (InterruptedException e) {
						//we got interrupted while waiting..
						continue;
					}
				}
			}
			
			//do this work
			work.execute();
		}
	}

}
