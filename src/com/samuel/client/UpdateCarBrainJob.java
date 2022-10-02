package com.samuel.client;

import java.util.ArrayList;

import com.osreboot.ridhvl.HvlMath;
import com.samuel.Network;
import com.samuel.NetworkMain;

public class UpdateCarBrainJob {
	
	Network decisionNet;
	float xPos, yPos, xSpeed, ySpeed;
	
	public UpdateCarBrainJob(Network n, float x, float y, float xs, float ys) {
		decisionNet = n;
		xPos = x;
		yPos = y;
		xSpeed = xs;
		ySpeed = ys;
	}

	public void updateNetwork(ArrayList<Track> tracks) {
		
		Track ct = closestTrack(tracks);
		
		int finishIndex = tracks.size() - 1;
		int playerTrack = tracks.indexOf(ct);

		if (playerTrack <= finishIndex - 2) {
			decisionNet.layers.get(0).nodes.get(0).value = tracks
					.get(playerTrack + 2).turnDirection;
		} else {
			decisionNet.layers.get(0).nodes.get(0).value = 0;
		}
		if (playerTrack <= finishIndex - 1) {
			decisionNet.layers.get(0).nodes.get(1).value = tracks
					.get(playerTrack + 1).turnDirection;
		} else {
			decisionNet.layers.get(0).nodes.get(1).value = 0;
		}
		decisionNet.layers.get(0).nodes.get(2).value = ct.turnDirection;
		if (playerTrack > 0) {
			decisionNet.layers.get(0).nodes.get(3).value = tracks
					.get(playerTrack - 1).turnDirection;
		} else {
			decisionNet.layers.get(0).nodes.get(3).value = 0;
		}
		if (playerTrack > 1) {
			decisionNet.layers.get(0).nodes.get(4).value = tracks
					.get(playerTrack - 2).turnDirection;
		} else {
			decisionNet.layers.get(0).nodes.get(4).value = 0;
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
			decisionNet.layers.get(0).nodes.get(5).value = HvlMath.map(yDistanceToCloseTrack, -Track.TRACK_SIZE,
					Track.TRACK_SIZE, -1.0f, 1.0f);
		} else {
			decisionNet.layers.get(0).nodes.get(5).value = 0;
		}

		if (ct.textureSelect == 0 || ct.textureSelect == 112
				|| ct.textureSelect == 148 || ct.textureSelect == 2
				|| ct.textureSelect == 172 || ct.textureSelect == 160) {
			decisionNet.layers.get(0).nodes.get(6).value = HvlMath.map(xDistanceToCloseTrack, -Track.TRACK_SIZE,
					Track.TRACK_SIZE, -1.0f, 1.0f);
		} else {
			decisionNet.layers.get(0).nodes.get(6).value = 0;
		}

//		decisionNet.layers.get(0).nodes.get(7).value = HvlMath.map(currentGear, 1, selectedCar.GEAR_COUNT, 0, 1);	
//		decisionNet.layers.get(0).nodes.get(8).value = HvlMath.map(currentRPM, 0, selectedCar.MAX_RPM, 0, 1);
//		decisionNet.layers.get(0).nodes.get(9).value = HvlMath.map(speed, 0, selectedCar.maxSpeedsPerGear[currentGear-1], 0, 1);
//		decisionNet.layers.get(0).nodes.get(10).value = HvlMath.map(turnAngle, -360, 360, 0, 1);

		NetworkMain.propogateAsNetwork(decisionNet);
		
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
