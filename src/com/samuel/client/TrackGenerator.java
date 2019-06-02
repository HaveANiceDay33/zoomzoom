package com.samuel.client;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class TrackGenerator {
	public static int START_X = 1920/2;
	public static int START_Y = 1080/2;
	int orientation;
	int segmentLength;
	int finishOr;
	Level selectedLevel;
	
	public ArrayList<Track> tracks;
	public ArrayList<Border> borders;
	public TrackGenerator() {
		selectedLevel = MenuManager.selectedTrack;
		tracks = new ArrayList<Track>();
		borders = new ArrayList<Border>();
	}
	public void generateTrack() {
		float startTurn = 0.5f;
		if(selectedLevel.tiles[0] == 0 || selectedLevel.tiles[0] == 2) {
			startTurn = 0f;
		} else if(selectedLevel.tiles[0] == 1) {
			startTurn = 1f;
		} else if(selectedLevel.tiles[0] == 3) {
			startTurn = -1f;
		}
		Track startTrack = new Track(START_X, START_Y, selectedLevel.tiles[0], startTurn);
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
				Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos - Track.TRACK_SIZE, orientation, 0f);
				tracks.add(track);
			} else if(orientation == 1) { //RIGHT
				Track track = new Track(tracks.get(i-1).xPos+ Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation, 0f);
				tracks.add(track);
			} else if(orientation == 2) { //DOWN
				Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos+ Track.TRACK_SIZE, orientation, 0f);
				tracks.add(track);
			} else if(orientation == 3) { //LEFT
				Track track = new Track(tracks.get(i-1).xPos- Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation, 0f);
				tracks.add(track);
			} else if(orientation == 4) { //FINISH
				if(tracks.get(i-1).textureSelect == 0){
					Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos - Track.TRACK_SIZE, orientation, 0f);
					tracks.add(track);
				}else if(tracks.get(i-1).textureSelect == 1) {
					Track track = new Track(tracks.get(i-1).xPos+ Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation, 0f);
					tracks.add(track);
				}else if(tracks.get(i-1).textureSelect == 2) {
					Track track = new Track(tracks.get(i-1).xPos, tracks.get(i-1).yPos+ Track.TRACK_SIZE, orientation, 0f);
					tracks.add(track);
				}else if(tracks.get(i-1).textureSelect == 3) {
					Track track = new Track(tracks.get(i-1).xPos- Track.TRACK_SIZE,tracks.get(i-1).yPos, orientation, 0f);
					tracks.add(track);
				}

			}
			if(tracks.get(i-1).textureSelect == 1 && tracks.get(i).textureSelect == 0) {
				tracks.get(i-1).textureSelect = 100;
				tracks.get(i-1).turnDirection = -1;
				//System.out.println("right to up");
			}
			if(tracks.get(i-1).textureSelect == 0 && tracks.get(i).textureSelect == 1) {
				tracks.get(i-1).textureSelect = 88;
				tracks.get(i-1).turnDirection = 1;
				//System.out.println("up to right");
			}
			if(tracks.get(i-1).textureSelect == 0 && tracks.get(i).textureSelect == 3) {
				tracks.get(i-1).textureSelect = 76;
				tracks.get(i-1).turnDirection = -1;
				//System.out.println("up to left");
			}
			if(tracks.get(i-1).textureSelect == 3 && tracks.get(i).textureSelect == 0) {
				tracks.get(i-1).textureSelect = 64;
				tracks.get(i-1).turnDirection = 1;
				//System.out.println("left to up");
			}
			
			if(tracks.get(i-1).textureSelect == 1 && tracks.get(i).textureSelect == 2) {
				tracks.get(i-1).textureSelect = 52;//98
				tracks.get(i-1).turnDirection = 1;
				//System.out.println("right to down");
			}
			if(tracks.get(i-1).textureSelect == 3 && tracks.get(i).textureSelect == 2) {
				tracks.get(i-1).textureSelect = 40;//99
				tracks.get(i-1).turnDirection = -1;
				//System.out.println("left to down");
			}
			if(tracks.get(i-1).textureSelect == 2 && tracks.get(i).textureSelect == 1) {
				tracks.get(i-1).textureSelect = 28;//97
				tracks.get(i-1).turnDirection = -1;
				//System.out.println("down to right");
			}
			if(tracks.get(i-1).textureSelect == 2 && tracks.get(i).textureSelect == 3) {
				tracks.get(i-1).textureSelect = 16; //100
				tracks.get(i-1).turnDirection = 1;
				//System.out.println("down to left");
			}
			////////////////////////////////////////////////////////////////////////////////
			if(tracks.get(i-1).textureSelect == 100 && tracks.get(i).textureSelect == 0) {
				tracks.get(i).textureSelect = 112;
			}
			
			if(tracks.get(i-1).textureSelect == 88 && tracks.get(i).textureSelect == 1) {
				tracks.get(i).textureSelect = 124;
			}
			
			if(tracks.get(i-1).textureSelect == 76 && tracks.get(i).textureSelect == 3) {
				tracks.get(i).textureSelect = 136;
			}
			
			if(tracks.get(i-1).textureSelect == 64 && tracks.get(i).textureSelect == 0) {
				tracks.get(i).textureSelect = 148;
			}
			
			if(tracks.get(i-1).textureSelect == 52 && tracks.get(i).textureSelect == 2) {
				tracks.get(i).textureSelect = 160;
			}
			
			if(tracks.get(i-1).textureSelect == 40 && tracks.get(i).textureSelect == 2) {
				tracks.get(i).textureSelect = 172;
			}
			
			if(tracks.get(i-1).textureSelect == 28 && tracks.get(i).textureSelect == 1) {
				tracks.get(i).textureSelect = 184;
			}
			
			if(tracks.get(i-1).textureSelect == 16 && tracks.get(i).textureSelect == 3) {
				tracks.get(i).textureSelect = 200;
			}
			
		}
	}
	public void update(float delta) {
		//MARKED
		for(Track fullTrack : Game.trackGen.tracks) {fullTrack.draw(delta, new HvlCoord2D(Game.tracker.getX(), Game.tracker.getY()));}
		for(Border allBorders : Game.trackGen.borders) {allBorders.draw(delta);}
	}
}
