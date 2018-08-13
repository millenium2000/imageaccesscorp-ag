package com.agriniuk.imgaccorp.tempconvert;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import com.agriniuk.imgaccorp.works.Work;
import com.agriniuk.imgaccorp.works.WorkWrapper;
import com.agriniuk.imgaccorp.works.Worker;


public class App {
	
	
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
	
	public static void main(String[] args) throws Exception {
		
		
		
		Worker<TempConvertWork> w = new TempConvertWorker();
		
		w.addAll(generateRandomWorks(10));
		
		w.start();
		
		Thread.sleep(10000);
		
		w.stop();
		
		Thread.sleep(10000);
		
		w.start();
		
		Thread.sleep(1000);
		
		w.addAll(generateRandomWorks(10));
		
		Thread.sleep(10000);
		
		w.stop();
		
	}
	
	
	
	public static LinkedList<TempConvertWork> generateRandomWorks(int n) {
		Random r = new Random(System.currentTimeMillis());
		
		LinkedList<TempConvertWork> res = new LinkedList<>();
		for (int i=0; i<n; i++) {
			res.add( new TempConvertWork(r.nextInt(201)-100, r.nextBoolean()));
		}
		return res;
	}
	
	
	
}
