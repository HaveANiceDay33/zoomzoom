package com.samuel.client.tracks;

import com.samuel.client.Level;

public class TestTrack extends Level{
	private static final String GENERATOR = "00001111222211111000011114";
	//private static final String GENERATOR = "00000000000000000000000000000000000000000000000000000004";//0 up //1 right //2 down //3 left //4 finish
	private static final String NAME = "Test Track";
	public TestTrack() {
		super(GENERATOR, NAME, 1);
		borderArgs[0] = 2600;
		borderArgs[1] = -100;
		borderArgs[2] = 1;
		borderArgs[3] = 10;
	}	
}
