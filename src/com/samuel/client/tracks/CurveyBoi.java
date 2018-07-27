package com.samuel.client.tracks;

import com.samuel.client.Level;

public class CurveyBoi extends Level{
	public CurveyBoi() {
		generator = "00003333300000000000011111111112222222211111110000000";//0 up //1 right //2 down //3 left //4 finish
		indNum = generator.split("");
		tiles = new int[indNum.length];
		for(int i = 0; i < indNum.length; i++) {
			tiles[i] = Integer.parseInt(indNum[i]); 
		}
	}
}
