package com.samuel.cars;

import com.samuel.Car;
import com.samuel.Main;

public class GolfGTI extends Car{
	public GolfGTI() {
		setACCELERATION(35);
		setGEAR_COUNT(6);
		setTIRE_GRIP(0.29f);
		setMAX_RPM(6500);
		setMIN_RPM(0);
		setTextureSelect(Main.GTI_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 40;
		maxSpeedsPerGear[1] = 64;
		maxSpeedsPerGear[2] = 87;
		maxSpeedsPerGear[3] = 99;
		maxSpeedsPerGear[4] = 144;
		maxSpeedsPerGear[5] = 168;
	}

}
