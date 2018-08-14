package com.agriniuk.imgaccorp.tempconvert;

import com.agriniuk.imgaccorp.works.Worker;


/**
 * This class does not do any useful work - its only purpose is to satisfy
 * the requirements. It needs constructors to propagate the {@link Worker}s
 * initialization parameters.
 */
public class TempConvertWorker extends Worker<TempConvertWork> {
	
	/** @see Worker#Worker() */
	public TempConvertWorker() {
		super();
	}
	
	
	/** @see Worker#Worker(int, int, boolean) */
	public TempConvertWorker(int queueMaxSize, int threadsMaxAmount, boolean isDaemon) {
		super(queueMaxSize, threadsMaxAmount, isDaemon);
	}
	
}
