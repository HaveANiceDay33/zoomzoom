package com.samuel.client.tracks;

import com.samuel.client.Level;

public class YourMom extends Level{
	private static final String GENERATOR = "00000333333333333332222222222222221111111111111110121212121121212121221210121000111100000004";//0 up //1 right //2 down //3 left //4 finish
	private static final String NAME = "Your Mom";
	public YourMom() {
		super(GENERATOR, NAME, 1);
		borderArgs[0] = 1600;
		borderArgs[1] = 1400;
		borderArgs[2] = 2000;
		borderArgs[3] = 10;
	}
}
