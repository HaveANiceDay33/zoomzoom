package com.samuel.client;

import java.util.ArrayList;

import com.osreboot.ridhvl.HvlMath;


import NEAT.com.evo.NEAT.Genome;

public class ManageCarLife {
	
	Genome genome;
	public float[] output;
	float xPos, yPos, xSpeed, ySpeed, speed,maxSpeed;
	
	public ManageCarLife(Genome n, float x, float y, float xs, float ys, float s, float ms) {
		genome = n;
		xPos = x;
		yPos = y;
		xSpeed = xs;
		ySpeed = ys;
		speed = s;
		maxSpeed = ms;
		output = new float[7];
	}

	public void updateNetwork(ArrayList<Track> tracks) {
		
		Track ct = closestTrack(tracks);
		
		int finishIndex = tracks.size() - 1;
		int playerTrack = tracks.indexOf(ct);
		
		float t2, t1, t0, tm1, tm2, x, y, s;

		if (playerTrack <= finishIndex - 2) {
			t2 = tracks
					.get(playerTrack + 2).turnDirection;
		} else {
			t2 = 0;
		}
		if (playerTrack <= finishIndex - 1) {
			t1 = tracks
					.get(playerTrack + 1).turnDirection;
		} else {
			t1 = 0;
		}
		t0 = ct.turnDirection;
		if (playerTrack > 0) {
			tm1 = tracks
					.get(playerTrack - 1).turnDirection;
		} else {
			tm1 = 0;
		}
		if (playerTrack > 1) {
			tm2 = tracks
					.get(playerTrack - 2).turnDirection;
		} else {
			tm2 = 0;
		}

		float yDistanceToCloseTrack = yPos > ct.yPos
				? HvlMath.distance(ct.xPos, ct.yPos, xPos, yPos) * Math.signum(ySpeed)
				: -HvlMath.distance(ct.xPos, ct.yPos, xPos, yPos) * Math.signum(ySpeed);
		float xDistanceToCloseTrack = xPos > ct.xPos
				? HvlMath.distance(ct.xPos, ct.yPos, xPos, yPos) * Math.signum(xSpeed)
				: -HvlMath.distance(ct.xPos, ct.yPos, xPos, yPos) * Math.signum(xSpeed);

		if (ct.textureSelect == 1 || ct.textureSelect == 124
				|| ct.textureSelect == 184 || ct.textureSelect == 3
				|| ct.textureSelect == 136 || ct.textureSelect == 200) {
			y = HvlMath.map(yDistanceToCloseTrack, -Track.TRACK_SIZE,
					Track.TRACK_SIZE, -1.0f, 1.0f);
		} else {
			y = 0;
		}

		if (ct.textureSelect == 0 || ct.textureSelect == 112
				|| ct.textureSelect == 148 || ct.textureSelect == 2
				|| ct.textureSelect == 172 || ct.textureSelect == 160) {
			x = HvlMath.map(xDistanceToCloseTrack, -Track.TRACK_SIZE,
					Track.TRACK_SIZE, -1.0f, 1.0f);
		} else {
			x = 0;
		}

//		decisionNet.layers.get(0).nodes.get(7).value = HvlMath.map(currentGear, 1, selectedCar.GEAR_COUNT, 0, 1);	
//		decisionNet.layers.get(0).nodes.get(8).value = HvlMath.map(currentRPM, 0, selectedCar.MAX_RPM, 0, 1);
		s = HvlMath.map(speed, 0, maxSpeed, 0, 1);
//		decisionNet.layers.get(0).nodes.get(10).value = HvlMath.map(turnAngle, -360, 360, 0, 1);
		
		float[] inputs = {t2, t1, t0, tm1, tm2, y, x, s};
		// Propogate
		output = genome.evaluateNetwork(inputs);	
	}
	
	public Track closestTrack(ArrayList<Track> tracks) {
		Track closestTrack = null;
		for(Track fullTrack : tracks) {
			if(closestTrack == null) {
				closestTrack = fullTrack;
			}
			float distance = HvlMath.distance(xPos, yPos, closestTrack.xPos, closestTrack.yPos);
			float distanceTest = HvlMath.distance(xPos, yPos, fullTrack.xPos, fullTrack.yPos);

			if(distanceTest < distance) {
				closestTrack = fullTrack;
			}
		}
		return closestTrack;
	}
	
	
}
