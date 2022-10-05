package com.samuel.client;

import java.util.ArrayList;
import java.util.HashMap;

import NEAT.com.evo.NEAT.Genome;

public final class MultithreadingManager {
	static HashMap<String, UpdateCarBrainJob> jobs;
	static ArrayList<CarThreadManager> managers;
	static ArrayList<Track> tracks;

	private MultithreadingManager() {
	}

	private static final int NUM_THREADS = 150;

	public static void init(ArrayList<Track> t) {
		jobs = new HashMap<>();
		tracks = t;
		managers = new ArrayList<>();
		for (int i = 0; i < NUM_THREADS; i++) {
			managers.add(new CarThreadManager());
		}
	}

	public static void executeJobs() {
		ArrayList<HashMap<String, UpdateCarBrainJob>> batches = new ArrayList<>();
		for(int i = 0; i < NUM_THREADS; i++) {
			batches.add(new HashMap<String, UpdateCarBrainJob>());
		}
		
		int i = 0;
		for (String s : jobs.keySet()) {
			int batchIndex = i % NUM_THREADS;
			batches.get(batchIndex).put(s, jobs.get(s));
			i++;
		}
		int initSize = jobs.size();
		jobs.clear();
		for(int j = 0; j < NUM_THREADS; j++) {
			managers.get(j).assign(batches.get(j), tracks);
		}
		while(jobs.size() < initSize) {
			for(CarThreadManager c : managers) {
				jobs.putAll(c.pullCompletedJobs());
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void queueJob(Genome genome) {
		jobs.put(genome.p.uid, new UpdateCarBrainJob(genome, genome.p.xPos, genome.p.yPos, genome.p.xSpeed, genome.p.ySpeed, genome.p.throttle, genome.p.selectedCar.maxSpeedsPerGear[genome.p.selectedCar.maxSpeedsPerGear.length - 1]));
	}

	public static UpdateCarBrainJob fetchJob(String uid) {
		return jobs.remove(uid);
	}

}
