package com.samuel.client.cars;

import com.samuel.client.Car;
import com.samuel.client.MainClient;

public class GolfGTI extends Car{
	public GolfGTI() {
		setACCELERATION(30);
		setGEAR_COUNT(6);
		setTIRE_GRIP(0.4f);
		setMAX_RPM(6500);
		setMIN_RPM(0);
		setTextureSelect(MainClient.GTI_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 40;
		maxSpeedsPerGear[1] = 64;
		maxSpeedsPerGear[2] = 87;
		maxSpeedsPerGear[3] = 99;
		maxSpeedsPerGear[4] = 144;
		maxSpeedsPerGear[5] = 168;
	}

}
