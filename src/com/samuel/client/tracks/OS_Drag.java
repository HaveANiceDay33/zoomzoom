package com.samuel.client.tracks;

import com.samuel.client.Border;
import com.samuel.client.Level;
import com.samuel.client.TrackGenerator;

public class OS_Drag extends Level{
	private static final String GENERATOR = "0000000000000000000000000000000000000000000000000040012422222222222222222222222222222222222222222222222222233000000000000000000000000000000000000000000000000004";//0 up //1 right //2 down //3 left //4 finish
	private static final String NAME = "OS's Drag Race";
	public OS_Drag() {
		super(GENERATOR, NAME, 0);
	}
}
