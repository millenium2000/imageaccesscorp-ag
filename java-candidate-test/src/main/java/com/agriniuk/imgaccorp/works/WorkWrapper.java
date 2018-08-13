package com.agriniuk.imgaccorp.works;


/**
 * Internal wrapper around {@link Work}.<br/>
 * 
 * @author Alexandre_Griniuk
 *
 */
public class WorkWrapper<T extends Work> {
	
	
	public final T work;
	
	
	public WorkWrapper(T work) {
		this.work = work;
	}
	
	
	public void execute() {
		try {
			this.work.start();
		} catch (Exception e) {
			//TODO: log
		}
	}

}
