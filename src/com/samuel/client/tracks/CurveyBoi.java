package com.samuel.client.tracks;

import com.samuel.client.Level;

public class CurveyBoi extends Level{
	public CurveyBoi() {
		generator = "000033333000000000000111111111122222222111111100000004";//0 up //1 right //2 down //3 left //4 finish
		name = "CurveyBoi";
		indNum = generator.split("");
		tiles = new int[indNum.length];
		for(int i = 0; i < indNum.length; i++) {
			tiles[i] = Integer.parseInt(indNum[i]); 
		}
	}
}
