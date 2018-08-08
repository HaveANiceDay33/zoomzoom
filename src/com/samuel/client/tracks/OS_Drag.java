package com.samuel.client.tracks;

import com.samuel.client.Border;
import com.samuel.client.Level;
import com.samuel.client.TrackGenerator;

public class OS_Drag extends Level{
	private static final String GENERATOR = "000000000000000000000000000000000000000000000000004001242222222222222222222222222222222222222222222222222223300000000000000000000000000000000000000000000000000400";//0 up //1 right //2 down //3 left //4 finish
	private static final String NAME = "OS's Drag Race";
	public OS_Drag() {
		super(GENERATOR, NAME, 0);
	}
}
