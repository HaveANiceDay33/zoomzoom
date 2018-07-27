package com.samuel.client.tracks;

import com.samuel.client.Level;

public class OS_Hell extends Level{
	public OS_Hell() {
		generator = "0121012101121012111001121111211111101000101111101010000030100303330000004";//0 up //1 right //2 down //3 left //4 finish
		name = "OS's Hell";
		indNum = generator.split("");
		tiles = new int[indNum.length];
		for(int i = 0; i < indNum.length; i++) {
			tiles[i] = Integer.parseInt(indNum[i]); 
		}
	}
}