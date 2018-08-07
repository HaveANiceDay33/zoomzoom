package com.samuel.client.tracks;

import com.samuel.client.Level;

public class OS_Drag extends Level{
	private static final String GENERATOR = "0000000000000000000000000012222222222222222222222222210000000000000000000000000040012422104";//0 up //1 right //2 down //3 left //4 finish
	//private static final String GENERATOR = "000000000000000000004124222222222222222222223300000000000000000000040";//0 up //1 right //2 down //3 left //4 finish
	private static final String NAME = "OS's Drag Race";
	public OS_Drag() {
		super(GENERATOR, NAME);
	}
}
