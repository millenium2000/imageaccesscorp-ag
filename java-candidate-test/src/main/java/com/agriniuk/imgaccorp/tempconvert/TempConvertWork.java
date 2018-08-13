package com.agriniuk.imgaccorp.tempconvert;

import com.agriniuk.imgaccorp.works.Work;


public class TempConvertWork extends Work {
	
	public final double value;
	
	public final boolean toCelsius;
	
	
	public TempConvertWork(double value, boolean toCelsius) {
		this.value = value;
		this.toCelsius = toCelsius;
	}
	
	
	@Override
	public void start() {
		
		//TODO
		throw new UnsupportedOperationException("Not implemented");
	}

}
