package com.samuel.client.cars;

import com.samuel.client.Car;
import com.samuel.client.MainClient;

public class Mini extends Car{
	public Mini() {
		NAME = "Mini Cooper";
		setACCELERATION(26);
		setGEAR_COUNT(5);
		setTIRE_GRIP(0.4f);
		setMAX_RPM(6000);
		setMIN_RPM(0);
		setTextureSelect(MainClient.MINI_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 35;
		maxSpeedsPerGear[1] = 61;
		maxSpeedsPerGear[2] = 99;
		maxSpeedsPerGear[3] = 125;
		maxSpeedsPerGear[4] = 155;
	}
}
