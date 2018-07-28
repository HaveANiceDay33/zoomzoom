package com.samuel.client.cars;

import com.samuel.client.Car;
import com.samuel.client.MainClient;

public class Camaro extends Car{
	public Camaro() {
		setACCELERATION(32);
		setGEAR_COUNT(6);
		setTIRE_GRIP(0.36f);
		setMAX_RPM(6600);
		setMIN_RPM(0);
		setTextureSelect(MainClient.CAMARO_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 30;
		maxSpeedsPerGear[1] = 57;
		maxSpeedsPerGear[2] = 78;
		maxSpeedsPerGear[3] = 99;
		maxSpeedsPerGear[4] = 125;
		maxSpeedsPerGear[5] = 175;
	}
}
