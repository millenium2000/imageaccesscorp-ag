package com.agriniuk.imgaccorp.tempconvert;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import com.agriniuk.imgaccorp.works.Work;
import com.agriniuk.imgaccorp.works.Worker;


public class DemoApp {
	
	
	private static final Logger log = LoggerFactory.getLogger(DemoApp.class);
	
	
	public static void main(String[] args) throws Exception {
		
		
		Worker<TempConvertWork> w = new TempConvertWorker();
		
		log.debug("------------ adding 10 work items ------------");
		addAll(w, generateRandomWorks(10));
		
		log.debug("------------ starting Worker ------------");
		w.start();
		
		log.debug("------------ sleeping 10s  ------------");
		Thread.sleep(10000);
		
		log.debug("------------ adding 10 work items  ------------");
		addAll(w, generateRandomWorks(10));
		
		log.debug("------------ sleeping 3 s  ------------");
		Thread.sleep(3000);
		
		log.debug("------------ stopping  ------------");
		w.stop();
		
		log.debug("------------ sleepign 1s  ------------");
		Thread.sleep(1000);
		
		log.debug("------------ starting Worker  ------------");
		w.start();
		
		log.debug("------------ sleepign 1s  ------------");
		Thread.sleep(1000);
		
		log.debug("------------ adding 10 work items  ------------");
		addAll(w, generateRandomWorks(10));
		
		log.debug("------------ sleepign 5s  ------------");
		Thread.sleep(5000);
		
		log.debug("------------ stopping  ------------");
		w.stop();
		
		log.debug("------------ done  ------------");
	}
	
	
	
	public static LinkedList<TempConvertWork> generateRandomWorks(int n) {
		Random r = new Random(System.currentTimeMillis());
		
		LinkedList<TempConvertWork> res = new LinkedList<>();
		for (int i=0; i<n; i++) {
			res.add( new TempConvertWork(r.nextInt(201)-100, r.nextBoolean()));
		}
		return res;
	}
	
	
	public static <T extends Work> void addAll(Worker<T> worker, Iterable<T> seq) {
		for (T work : seq) {
			worker.add(work);
		}
	}

	
	
}
