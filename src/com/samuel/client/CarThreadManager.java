package com.samuel.client;

import java.util.ArrayList;
import java.util.HashMap;

public class CarThreadManager {
	CarThread carThread;
	boolean jobComplete;
	ArrayList<Track> tracks;
	HashMap<String, ManageCarLife> jobs;
	
	public CarThreadManager() {
		carThread = new CarThread();
		jobComplete = true;
		carThread.start();
		jobs = new HashMap<>();
	} 
	
	public synchronized void assign(HashMap<String, ManageCarLife> j, ArrayList<Track> t) {
		if(!jobComplete) throw new RuntimeException("OI MATE");
		tracks = t;
		jobs = j;
	}
	
//	public synchronized HashMap<String, ManageCarLife> pullCompletedJobs() {
//		
//		if(jobComplete) {
//			HashMap<String, ManageCarLife> temp = new HashMap<>(jobs);
//			jobs.clear();
//			return temp;
//		}
//		
//		return new HashMap<>();
//	}
	
	public synchronized void reset() {
		tracks = new ArrayList<>();
		jobs = new HashMap<>();
		jobComplete = true;
	}
	
	public synchronized void assignInputs(HashMap<String, float[]> in) {
		if(!jobComplete) throw new RuntimeException("OI MATE 2");

		for(String uid : in.keySet()) {
			jobs.get(uid).xPos = in.get(uid)[0];
			jobs.get(uid).yPos = in.get(uid)[1];
			jobs.get(uid).xSpeed = in.get(uid)[2];
			jobs.get(uid).ySpeed = in.get(uid)[3];
			jobs.get(uid).speed = in.get(uid)[4];
		}
		
		jobComplete = false;
	}
	
	public synchronized HashMap<String, float[]> pullOutput(){
		HashMap<String, float[]> outs = new HashMap<>();

		if(jobComplete) {
			for(String uid : jobs.keySet()) {
				outs.put(uid, jobs.get(uid).output);
			}
		}
		
		return outs;
	}
	
	public class CarThread extends Thread {
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				synchronized (CarThreadManager.this) {
					if(!jobComplete) {
						for(ManageCarLife j : jobs.values()) {
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
