package com.samuel.client.cars;

import com.samuel.client.Car;
import com.samuel.client.MainClient;

public class SubaruWRX extends Car{
	public SubaruWRX() {
		setACCELERATION(36);
		setGEAR_COUNT(6);
		setTIRE_GRIP(0.34f);
		setMAX_RPM(7000);
		setMIN_RPM(0);
		setTextureSelect(MainClient.WRX_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 37;
		maxSpeedsPerGear[1] = 56;
		maxSpeedsPerGear[2] = 75;
		maxSpeedsPerGear[3] = 100;
		maxSpeedsPerGear[4] = 137;
		maxSpeedsPerGear[5] = 177;
	}
}
