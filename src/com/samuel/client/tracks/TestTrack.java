package com.samuel.client.tracks;

import com.samuel.client.Level;

public class TestTrack extends Level{
	public TestTrack() {
		generator = "00001111222211111000011114";//0 up //1 right //2 down //3 left //4 finish
		name = "Test Track";
		indNum = generator.split("");
		tiles = new int[indNum.length];
		for(int i = 0; i < indNum.length; i++) {
			tiles[i] = Integer.parseInt(indNum[i]); 
		}
	}	
}
