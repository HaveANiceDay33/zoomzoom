package com.samuel.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl2.HvlConfig;
import com.osreboot.ridhvl2.menu.HvlType;
import com.samuel.Network;

public class GeneticsHandler {
	public static final int MAX_POP = 10000;

	public static int currentGeneration = 1;
	public static ArrayList<Player> population;
	
	//public static Player hero;

	public static void init() {
		population = new ArrayList<>();
		//hero = new Player();
		//hero.fitness = 100;
		File bestPlayerData = new File("championNetwork.json");
		if(bestPlayerData.exists()) {
			HvlType.cacheForName(Network.class);
			Network championNet = HvlConfig.PJSON.load("championNetwork.json");
			Player p = new Player();
		
			p.setNetwork(championNet);
			populate(p);
				
			for(int i = 1; i < MAX_POP; i++) {
				populate(new Player());
			}
		} else {
			for(int i = 0; i < MAX_POP; i++) {
				populate(new Player());
			}
		}
	} 

	public static void populate(Player p) {
		population.add(p);
	}

	public static float calcFitness(Player p) {
	
		int finishIndex = Game.trackGen.tracks.size()-1;
		int playerTrack = Game.trackGen.tracks.indexOf(p.closestTrack());
		if(p.trackComplete) {
			return (p.finalTrackTime) / 1000;
		} else if(!p.trackComplete && finishIndex - playerTrack == 0){
			return 0.5f + Math.abs(HvlMath.map(HvlMath.distance(p.closestTrack().xPos, p.closestTrack().yPos, p.getXPos(), p.getYPos()), 0, 1.5f*Track.TRACK_SIZE, 0f, 0.49f));
		} else {
			return finishIndex - playerTrack;
		}
	}

	public static void duplicateParents(Player par1, Player par2) {

		Network parent1Network = par1.decisionNet;
		Network parent2Network = par2.decisionNet;
		//Keep the originals
		try {
			Player p = new Player();
			p.setNetwork((Network)parent1Network.clone());
			populate(p);
			Player p2 = new Player();
			p2.setNetwork((Network)parent2Network.clone());
			populate(p2);
		} catch (CloneNotSupportedException e) {}

		try {
			for(int i = 0; i < (MAX_POP-2); i++) {
				Player child1 = (Player) par1.clone();
				Player child2 = (Player) par2.clone();
				mutatePlayer(crossOverGenes(child1, child2));
			}
		} catch (CloneNotSupportedException e) {} 

		Game.trackTimer = 0;
		currentGeneration++;
		Game.generationTimer = 45;
	}

	public static Player crossOverGenes(Player c1, Player c2) {

		Player child = new Player();

		float geneticBias = (c2.getFitness() - c1.getFitness())/c2.getFitness();

		for(int l = 0; l < child.decisionNet.layers.size(); l++) {
			for(int n = 0; n < child.decisionNet.layers.get(l).numNodes; n++) {
				for(int i = 0; i < child.decisionNet.layers.get(l).nodes.get(n).connectionWeights.size(); i++) {
					double rand = Math.random();
					if(rand < 0.5 + geneticBias) {
						child.decisionNet.layers.get(l).nodes.get(n).connectionWeights.put(i, c1.decisionNet.layers.get(l).nodes.get(n).connectionWeights.get(i));
					} else {
						child.decisionNet.layers.get(l).nodes.get(n).connectionWeights.put(i, c2.decisionNet.layers.get(l).nodes.get(n).connectionWeights.get(i));
					}
				}
				double biasRand = Math.random();
				if(biasRand < 0.5 + geneticBias) {
					child.decisionNet.layers.get(l).nodes.get(n).bias = c1.decisionNet.layers.get(l).nodes.get(n).bias;
				} else {
					child.decisionNet.layers.get(l).nodes.get(n).bias = c2.decisionNet.layers.get(l).nodes.get(n).bias;
				}
			}
		}
		return child;
	}

	public static void mutatePlayer(Player p) {
		for(int l = 0; l < p.decisionNet.layers.size(); l++) {
			for(int n = 0; n < p.decisionNet.layers.get(l).numNodes; n++) {
				for(int i = 0; i < p.decisionNet.layers.get(l).nodes.get(n).connectionWeights.size(); i++) {
					double rand = Math.random();
					if(rand < 0.05) {
						p.decisionNet.layers.get(l).nodes.get(n).connectionWeights.put(i, (float) Math.random());
					}
				}
				double biasRand = Math.random();
				if(biasRand < 0.05) {
					p.decisionNet.layers.get(l).nodes.get(n).bias = (float) Math.random();
				}
			}
		}
		populate(p);
	}

	public static Comparator<Player> compareByScore = new Comparator<Player>() {
		@Override
		public int compare(Player p1, Player p2) {
			return Float.compare(p1.getFitness(), p2.getFitness());
		}
	};
	
//	public static void setHero(Player p) {
//		hero = p;
//	}
}
