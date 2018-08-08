package com.samuel.client;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.HvlMath;

public class TrackGenerator {
	public static int START_X = 1920/2;
	public static int START_Y = 1080/2;
	int orientation;
	int segmentLength;
	int finishOr;
	Level selectedLevel;
	public static boolean onTrack;
	public static boolean trackComplete;
	public static ArrayList<Track> tracks;
	public static ArrayList<Border> borders;
	public TrackGenerator() {
		selectedLevel = MenuManager.selectedTrack;
		tracks = new ArrayList<Track>();
		borders = new ArrayList<Border>();
		trackComplete = false;
	}
	public void generateTrack() {
		Track startTrack = new Track(START_X, START_Y, selectedLevel.tiles[0]);
		tracks.add(startTrack);
		for(int i = 4; i <= selectedLevel.numBorders * 4; i+=4) {
			Border border = new Border(selectedLevel.borderArgs[i-4], selectedLevel.borderArgs[i-3],selectedLevel.borderArgs[i-2],selectedLevel.borderArgs[i-1]);
			borders.add(border);
		}
		for(int i = 1; i < selectedLevel.tiles.length; i++) {
			orientation = selectedLevel.tiles[i];
			if((tracks.get(tracks.size()-1).textureSelect == 0 && orientation == 2)) {
				orientation = 1;
			}else if((tracks.get(tracks.size()-1).textureSelect == 1 && orientation == 3)) {
				orientation = 2;
			}else if((tracks.get(tracks.size()-1).textureSelect == 2 && orientation == 0)) {
				orientation = 3;
			}else if((tracks.get(tracks.size()-1).textureSelect == 3 && orientation == 1)) {
				orientation = 0;
			}
			
			if(orientation == 0) { //UP
				Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos - Track.TRACK_SIZE, orientation);
				tracks.add(track);
			} else if(orientation == 1) { //RIGHT
				Track track = new Track(tracks.get(i-1).xPos+ Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation);
				tracks.add(track);
			} else if(orientation == 2) { //DOWN
				Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos+ Track.TRACK_SIZE, orientation);
				tracks.add(track);
			} else if(orientation == 3) { //LEFT
				Track track = new Track(tracks.get(i-1).xPos- Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation);
				tracks.add(track);
			} else if(orientation == 4) { //FINISH
				if(tracks.get(i-1).textureSelect == 0){
					Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos - Track.TRACK_SIZE, orientation);
					tracks.add(track);
				}else if(tracks.get(i-1).textureSelect == 1) {
					Track track = new Track(tracks.get(i-1).xPos+ Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation);
					tracks.add(track);
				}else if(tracks.get(i-1).textureSelect == 2) {
					Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos+ Track.TRACK_SIZE, orientation);
					tracks.add(track);
				}else if(tracks.get(i-1).textureSelect == 3) {
					Track track = new Track(tracks.get(i-1).xPos- Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation);
					tracks.add(track);
				}

			}
			if(tracks.get(i-1).textureSelect == 1 && tracks.get(i).textureSelect == 0) {
				tracks.get(i-1).textureSelect = 100;
				//System.out.println("right to up");
			}
			if(tracks.get(i-1).textureSelect == 0 && tracks.get(i).textureSelect == 1) {
				tracks.get(i-1).textureSelect = 99;
				//System.out.println("up to right");
			}
			if(tracks.get(i-1).textureSelect == 0 && tracks.get(i).textureSelect == 3) {
				tracks.get(i-1).textureSelect = 98;
				//System.out.println("up to left");
			}
			if(tracks.get(i-1).textureSelect == 3 && tracks.get(i).textureSelect == 0) {
				tracks.get(i-1).textureSelect = 97;
				//System.out.println("left to up");
			}
			
			if(tracks.get(i-1).textureSelect == 1 && tracks.get(i).textureSelect == 2) {
				tracks.get(i-1).textureSelect = 96;//98
				//System.out.println("right to down");
			}
			if(tracks.get(i-1).textureSelect == 3 && tracks.get(i).textureSelect == 2) {
				tracks.get(i-1).textureSelect = 95;//99
				//System.out.println("left to down");
			}
			if(tracks.get(i-1).textureSelect == 2 && tracks.get(i).textureSelect == 1) {
				tracks.get(i-1).textureSelect = 94;//97
				//System.out.println("down to right");
			}
			if(tracks.get(i-1).textureSelect == 2 && tracks.get(i).textureSelect == 3) {
				tracks.get(i-1).textureSelect = 93; //100
				//System.out.println("down to left");
			}
			
			
		}
	}
	public void update(float delta) {
		Track closestTrack = null;

		for(Track fullTrack : tracks) {
			if(closestTrack == null) {
				closestTrack = fullTrack;
			}
			float distance = HvlMath.distance(Game.player.getXPos(), Game.player.getYPos(), closestTrack.xPos, closestTrack.yPos);
			float distanceTest = HvlMath.distance(Game.player.getXPos(), Game.player.getYPos(), fullTrack.xPos, fullTrack.yPos);

			if(distanceTest < distance) {
				closestTrack = fullTrack;
			}
			//System.out.println(Game.player.getXPos() + "\t "+ (closestTrack.xPos + (Track.TRACK_SIZE/2)) + "\t" + (closestTrack.xPos - (Track.TRACK_SIZE/2)));
			fullTrack.draw(delta);
			
		}
		for(Border allBorders : borders) {
			allBorders.draw(delta);
		}
		
		
		if(Game.player.getXPos() <= closestTrack.xPos  + (Track.TRACK_SIZE/2) && Game.player.getXPos() >= closestTrack.xPos - (Track.TRACK_SIZE/2)
				&& Game.player.getYPos() <= closestTrack.yPos + (Track.TRACK_SIZE/2)&& Game.player.getYPos() >= closestTrack.yPos - (Track.TRACK_SIZE/2)) {	
			onTrack = true;
		}else {
			onTrack = false;
		}
		if(Game.player.getXPos() <= closestTrack.xPos  + (Track.TRACK_SIZE/2) && Game.player.getXPos() >= closestTrack.xPos - (Track.TRACK_SIZE/2)
				&& Game.player.getYPos() <= closestTrack.yPos + (Track.TRACK_SIZE/2)&& Game.player.getYPos() >= closestTrack.yPos - (Track.TRACK_SIZE/2) && closestTrack.textureSelect == 4) {
			onTrack = true;
			trackComplete = true;
			
		}
//		if(onTrack) {
//			System.out.println("ON");
//		}else {
//			System.out.println("OFF");
//		}
		//hvlDrawLine(Game.player.getXPos(), Game.player.getYPos(), closestTrack.xPos, closestTrack.yPos, Color.blue);
	}
}
