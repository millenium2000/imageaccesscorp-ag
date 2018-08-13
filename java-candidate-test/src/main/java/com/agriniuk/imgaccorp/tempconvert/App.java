package com.agriniuk.imgaccorp.tempconvert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agriniuk.imgaccorp.works.Work;
import com.agriniuk.imgaccorp.works.WorkWrapper;


public class App {
	
	
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
	
	public static void main(String[] args) {
		
		log.debug("hello from logger");
		
		System.out.println("---> 10.2 into C == " + TempService.toC(10.2));
		
		System.out.println("---> 10.2 into F == " + TempService.toF(10.2));
		
		Work w = new Work();
		WorkWrapper<Work> ww ;
		
	}
}
