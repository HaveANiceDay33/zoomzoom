package com.samuel.client.cars;

import com.samuel.client.Car;
import com.samuel.client.MainClient;

public class Supra extends Car{
	public Supra() {
		NAME = "Toyota Supra";
		setACCELERATION(28);
		setGEAR_COUNT(6);
		setTIRE_GRIP(0.39f);
		setMAX_RPM(7000);
		setMIN_RPM(0);
		setTextureSelect(MainClient.SUPRA_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 33;
		maxSpeedsPerGear[1] = 59;
		maxSpeedsPerGear[2] = 70;
		maxSpeedsPerGear[3] = 99;
		maxSpeedsPerGear[4] = 130;
		maxSpeedsPerGear[5] = 170;
	}

}
