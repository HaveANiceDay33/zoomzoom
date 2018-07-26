package com.samuel.tracks;

import com.samuel.Level;

public class TestTrack extends Level{
	public TestTrack() {
		generator = "00001111222211111000011114";//0 up //1 right //2 down //3 left //4 finish
		indNum = generator.split("");
		tiles = new int[indNum.length];
		for(int i = 0; i < indNum.length; i++) {
			tiles[i] = Integer.parseInt(indNum[i]); 
		}
	}	
}
