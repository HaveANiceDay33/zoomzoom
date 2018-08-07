package com.samuel.client.cars;

import com.samuel.client.Car;
import com.samuel.client.MainClient;

public class AcuraNSX extends Car{
	public AcuraNSX() {
		NAME = "Acura NSX";
		setACCELERATION(31);
		setGEAR_COUNT(7);
		setTIRE_GRIP(0.38f);
		setMAX_RPM(7600);
		setMIN_RPM(0);
		setTextureSelect(MainClient.NSX_INDEX);
		maxSpeedsPerGear = new int[getGEAR_COUNT()];
		maxSpeedsPerGear[0] = 45;
		maxSpeedsPerGear[1] = 76;
		maxSpeedsPerGear[2] = 99;
		maxSpeedsPerGear[3] = 110;
		maxSpeedsPerGear[4] = 147;
		maxSpeedsPerGear[5] = 167;
		maxSpeedsPerGear[6] = 180;
	}
}
