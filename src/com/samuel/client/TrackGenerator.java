package com.samuel.client;

import java.util.ArrayList;

public class TrackGenerator {
	public static int START_X = 1920/2;
	public static int START_Y = 1080/2;
	int orientation;
	int segmentLength;
	int finishOr;
	Level selectedLevel;
	
	public static ArrayList<Track> tracks;
	public static ArrayList<Border> borders;
	public TrackGenerator() {
		selectedLevel = MenuManager.selectedTrack;
		tracks = new ArrayList<Track>();
		borders = new ArrayList<Border>();
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
		for(Track fullTrack : TrackGenerator.tracks) {fullTrack.draw(delta, Game.players.get(0));}
		for(Border allBorders : TrackGenerator.borders) {allBorders.draw(delta);}
	}
}
