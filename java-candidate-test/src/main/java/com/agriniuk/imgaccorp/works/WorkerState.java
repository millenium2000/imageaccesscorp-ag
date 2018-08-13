package com.agriniuk.imgaccorp.works;


/**
 * States of {@link Worker}
 */
public enum WorkerState {
	
	
	/** 
	 * First initial state. 
	 * A {@link Worker} can only accept new {@Work}s 
	 * but execution will start only after calling {@link Worker#start()}
	 */
	INITIAL,
	
	
	/**
	 * In this state a {@link Worker} has active threads
	 * which process current {@list Work} queue.
	 */
	OPERATIONAL,
	
	
	/**
	 * In this state a {@link Worker} does not have any active
	 * processing threads, but can still accept new {@link Work} units.
	 */
	STOPPED
	
}
