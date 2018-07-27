package com.samuel.client.tracks;

import com.samuel.client.Level;

public class OS_Hell extends Level{
	private static final String GENERATOR = "0121012101121012111001121111211111101000101111101010000030100303330000004";//0 up //1 right //2 down //3 left //4 finish
	private static final String NAME = "OS's Hell";
	public OS_Hell() {
		super(GENERATOR, NAME);
	}
}