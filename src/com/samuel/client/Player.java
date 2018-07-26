package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.input.Keyboard;

import com.osreboot.ridhvl.HvlMath;

public class Player {
	public static float xPos;
	public static float yPos;
	static float xSpeed;
	static float ySpeed;
	static float throttle;
	float turnAngle;
	static float turnAngleSpeed;
	
	public float finalTrackTime;
	Car selectedCar;
	public Player(float xArg, float yArg){
		xPos = xArg;
		yPos = yArg;
		selectedCar = MenuManager.selectedCar;
	}
	public void draw(float delta) {
		hvlRotate(xPos,yPos-30, turnAngle);
		hvlDrawQuadc(xPos, yPos, 100, 100, MainClient.getTexture(selectedCar.textureSelect));
		hvlDrawQuadc(xPos, yPos, 100, 100, MainClient.getTexture(selectedCar.textureSelect + 1), MenuManager.color);
		hvlResetRotation();
	}
	public void update(float delta) {

		throttle = Game.speed * delta * Game.speed;
	
		if(Keyboard.isKeyDown(Keyboard.KEY_A) && throttle > 0) {
			turnAngleSpeed = -1 * Math.abs(130 - throttle)* delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D) && throttle > 0) {
			turnAngleSpeed = Math.abs(130 - throttle) * delta;
		}
		
		turnAngleSpeed = HvlMath.stepTowards(turnAngleSpeed, (float)(selectedCar.TIRE_GRIP/throttle), 0);
		turnAngle += turnAngleSpeed;
		ySpeed = (float) ((throttle / 7) * Math.cos(Math.toRadians(turnAngle)));
		xSpeed = (float) ((float) (throttle / 7) * Math.sin(Math.toRadians(turnAngle)));

		yPos -= ySpeed; 
		xPos += xSpeed;
	}
	public float getXPos() {
		return xPos;
	}
	public float getYPos() {
		return yPos;
	}
	
}
