package com.samuel.client;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;

public class GeneticsHandler {
	public static final int MAX_POP = 10000;
	
	public static int currentGeneration = 1;
	public static ArrayList<Player> population;
	
	public static void init() {
		population = new ArrayList<>();
		for(int i = 0; i < MAX_POP; i++) {
			populate(new Player());
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
			return 0.5f + Math.abs(HvlMath.map(HvlMath.distance(p.closestTrack().xPos, p.closestTrack().yPos, p.getXPos(), p.getYPos()), 0, 2.5f*Track.TRACK_SIZE, 0f, 0.49f));
		} else {
			return finishIndex - playerTrack;
		}
		
	}
	
	public static void duplicateParents(Player par1, Player par2) {

		Player parent1 = par1;
		Player parent2 = par2;
		//Keep the originals
		try {
			Player p = (Player) parent1.clone();
			p.setXPos(Display.getWidth()/2);
			p.setYPos(Display.getHeight()/2);
			p.currentGear = 1;
			p.currentRPM = p.selectedCar.MIN_RPM;
			p.currentRPMGoal = p.selectedCar.MIN_RPM;
			p.speedGoal = 0;
			p.currentRPMGoal = 0;
			p.speed = 0;
			p.turnAngle = 0;
			p.trackComplete = false;
			p.dead = false;
			p.sittingTimer = 6;
			populate(p);
			Player p2 = (Player) parent2.clone();
			p2.setXPos(Display.getWidth()/2);
			p2.setYPos(Display.getHeight()/2);
			p2.currentGear = 1;
			p2.currentRPM = p2.selectedCar.MIN_RPM;
			p2.currentRPMGoal = p2.selectedCar.MIN_RPM;
			p2.speedGoal = 0;
			p2.currentRPMGoal = 0;
			p2.speed = 0;
			p2.turnAngle = 0;
			p2.trackComplete = false;
			p2.dead = false;
			p2.sittingTimer = 6;
			populate(p2);
		} catch (CloneNotSupportedException e) {}
		
		try {
			for(int i = 0; i < (MAX_POP-2); i++) {
				Player child1 = (Player) parent1.clone();
				Player child2 = (Player) parent2.clone();
				mutatePlayer(crossOverGenes(child1, child2));
			}
		} catch (CloneNotSupportedException e) {} 
		
		Game.trackTimer = 0;
		currentGeneration++;
		Game.generationTimer = 60;
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
					if(rand < 0.01) {
						p.decisionNet.layers.get(l).nodes.get(n).connectionWeights.put(i, (float) Math.random());
					}
				}
				double biasRand = Math.random();
				if(biasRand < 0.01) {
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
}
