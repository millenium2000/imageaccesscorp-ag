package com.agriniuk.imgaccorp.works;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal wrapper around {@link Work}.<br/>
 * 
 * @author Alexandre_Griniuk
 *
 */
public class WorkWrapper<T extends Work> {
	
	
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
