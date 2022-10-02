package com.samuel.client;

import java.util.ArrayList;
import java.util.HashMap;

public class CarThreadManager {
	CarThread carThread;
	boolean jobComplete;
	ArrayList<Track> tracks;
	HashMap<String, UpdateCarBrainJob> jobs;
	public CarThreadManager() {
		carThread = new CarThread();
		jobComplete = true;
		carThread.start();
	} 
	
	public synchronized void assign(HashMap<String, UpdateCarBrainJob> j, ArrayList<Track> t) {
		if(!jobComplete) throw new RuntimeException("OI MATE");
		tracks = t;
		jobs = j;
		jobComplete = false;
	
	}
	
	public synchronized HashMap<String, UpdateCarBrainJob> pullCompletedJobs() {
		
		if(jobComplete) {
			HashMap<String, UpdateCarBrainJob> temp = new HashMap<>(jobs);
			jobs.clear();
			return temp;
		}
		
		return new HashMap<>();
	}
	
	public class CarThread extends Thread {
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				synchronized (CarThreadManager.this) {
					if(!jobComplete) {
						for(UpdateCarBrainJob j : jobs.values()) {
							j.updateNetwork(tracks);
						}
						jobComplete = true;
					}
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
