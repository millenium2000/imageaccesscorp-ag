package com.agriniuk.imgaccorp.tempconvert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agriniuk.imgaccorp.works.Work;


public class TempConvertWork extends Work {
	
	private static final Logger log = LoggerFactory.getLogger(TempConvertWork.class);
	
	public final double value;
	
	public final boolean toCelsius;
	
	
	public TempConvertWork(double value, boolean toCelsius) {
		this.value = value;
		this.toCelsius = toCelsius;
	}
	
	
	@Override
	public void start() {
		log.debug("converting {} => {}", value, tempNameFull(toCelsius));
		double res = TempService.convert(value, toCelsius);
		log.info("{} {}` = {} {}`", value, tempNameShort(!toCelsius), res, tempNameShort(toCelsius));
	}
	
	
	private static String tempNameFull(boolean isCelsius) {
		return isCelsius ? "Celsius" : "Farenheith";
	}
	
	
	private static String tempNameShort(boolean isCelsius) {
		return isCelsius ? "C" : "F";
	}

}
