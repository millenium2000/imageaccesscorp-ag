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
		//I could just leave the RuntimeException to flow up and get cought by WorkWrapper
		// but I know that w3schools API fails a lot of times, so for illustration purpose
		// I don't want to pollute logs with these
		try {
			double res = TempService.convert(value, toCelsius);
			log.info("{} {}` => {} {}`", value, b2s(!toCelsius), res, b2s(toCelsius));
		} catch (Exception e) {
			log.info("{} {}` => ERROR({})", value, b2s(!toCelsius), e.toString());			
		}
	}
	
	
	private static String b2s(boolean isCelsius) {
		return isCelsius ? "C" : "F";
	}

}
