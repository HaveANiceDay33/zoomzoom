package com.samuel.client.tracks;

import com.samuel.client.Level;

public class CurveyBoi extends Level{
	private static final String GENERATOR = "000033333000000000000111111111122222222111111100000004";//0 up //1 right //2 down //3 left //4 finish
	private static final String NAME = "CurveyBoi";	
	public CurveyBoi() {
		super(GENERATOR, NAME, 1);
		borderArgs[0] = 2500;
		borderArgs[1] = -700;
		borderArgs[2] = 64;
		borderArgs[3] = 1;
	}
}
