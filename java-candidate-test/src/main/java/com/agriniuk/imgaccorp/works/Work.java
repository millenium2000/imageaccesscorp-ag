package com.agriniuk.imgaccorp.works;


/**
 * General work unit.<br/>
 * Users of framework should sub-class and override {@link #start()}
 */
public class Work {
	
	
	/**
	 * The real work will be done in this function
	 * when it is called and executed in some thread
	 * belonging to {@link Worker}
	 */
	public void start() {
	}
	
}
