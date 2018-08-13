package com.agriniuk.imgaccorp.works;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Internal wrapper around {@link Work}.<br/>
 * 
 * It's not very useful right now, but it can put to use.
 * For example, we can have a date-time stamp of the {@link Work}), 
 * a runtime measured, work id, etc. 
 */
class WorkWrapper<T extends Work> {
	
	
	private static final Logger log = LoggerFactory.getLogger(WorkWrapper.class);
	
	public final T work;
	
	
	public WorkWrapper(T work) {
		this.work = work;
	}
	
	
	public void execute() {
		try {
			log.trace("starting work execution");
			this.work.start();
			log.trace("finished work execution");
		} catch (Exception e) {
			log.error("exception while executing work", e);
		}
	}

}
