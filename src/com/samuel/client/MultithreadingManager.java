package com.samuel.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import NEAT.com.evo.NEAT.Genome;

public final class MultithreadingManager {
	static HashMap<String, ManageCarLife> jobs;
	static HashMap<String, float[]> inputs;
	static HashMap<String, float[]> outputs;
	static ArrayList<CarThreadManager> managers;
	static ArrayList<Track> tracks;

	private MultithreadingManager() {
	}

	private static final int NUM_THREADS = 12;

	public static void init(ArrayList<Track> t) {
		jobs = new HashMap<>();
		inputs = new HashMap<>();
		outputs = new HashMap<>();
		tracks = t;
		managers = new ArrayList<>();
		for (int i = 0; i < NUM_THREADS; i++) {
			managers.add(new CarThreadManager());
		}
	}
	
	public static void initGen() {
		for(CarThreadManager c : managers) {
			c.reset();
		}
		ArrayList<HashMap<String, ManageCarLife>> batches = new ArrayList<>();
		for(int i = 0; i < NUM_THREADS; i++) {
			batches.add(new HashMap<String, ManageCarLife>());
		}
		
		int i = 0;
		for (String s : jobs.keySet()) {
			int batchIndex = i % NUM_THREADS;
			batches.get(batchIndex).put(s, jobs.get(s));
			i++;
		}
		jobs.clear();
		
		for(int j = 0; j < NUM_THREADS; j++) {
			managers.get(j).assign(batches.get(j), tracks);
		}
	}
	
	public static void executeJobs() {
		outputs.clear();
		for(CarThreadManager c : managers) {
			synchronized (c) {
				c.assignInputs(new HashMap<>(inputs.entrySet().stream().filter(e -> c.jobs.containsKey(e.getKey())).collect(Collectors.toMap(e->e.getKey(), e->e.getValue()))));
			}
		}
		
		int initSize = inputs.size();
		inputs.clear();
		
		while(outputs.size() < initSize) {
			for(CarThreadManager c : managers) {
				outputs.putAll(c.pullOutput());
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
		jobs.put(genome.p.uid, new ManageCarLife(genome, genome.p.xPos, genome.p.yPos, genome.p.xSpeed, genome.p.ySpeed, genome.p.throttle, genome.p.selectedCar.maxSpeedsPerGear[genome.p.selectedCar.maxSpeedsPerGear.length - 1]));
	}

	
	public static void queueInput(String uid, float [] in) {
		inputs.put(uid, in);
	}
	
	public static float[] fetchOutput(String uid) {
		if(!outputs.containsKey(uid))
			throw new RuntimeException("HEY!");
		return outputs.get(uid);
	}

}
