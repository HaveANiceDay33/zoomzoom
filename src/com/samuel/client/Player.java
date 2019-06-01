package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.input.HvlInput;
import com.samuel.client.effects.CarEffectApplicator;
import com.samuel.client.effects.MysteryUnlocker;

public class Player {
	private float xPos;
	private float yPos;
	float xSpeed;
	float ySpeed;
	float throttle;
	float turnAngle;
	public float turnAngleSpeed;
	int currentGear;
	public int speed;
	int currentRPM;
	int currentRPMGoal;
	float rpmMod;
	float speedGoal;
	public boolean onTrack;
	public boolean hitWall;
	public boolean trackComplete;
	float accurateAngleRPM;
	float accurateAngleSpeed;
	HvlInput shiftUpInput;
	HvlInput shiftDownInput;
	
	PlayerInput inputMethod; 
	
	public float finalTrackTime;
	Car selectedCar;
	public Player(PlayerInput inputMethodArg){
		xPos = Display.getWidth()/2;
		yPos = Display.getHeight()/2;
		selectedCar = MenuManager.selectedCar;
		currentGear = 1;
		currentRPM = selectedCar.MIN_RPM;
		currentRPMGoal = selectedCar.MIN_RPM;
		speedGoal = 0;
		currentRPMGoal = 0;
		speed = 0;
		trackComplete = false;
		turnAngle = 0;
		
		inputMethod = inputMethodArg;
		
		shiftUpInput = new HvlInput(new HvlInput.InputFilter() {
			@Override
			public float getCurrentOutput() {
				if(inputMethod != null && inputMethod.isShiftingUp() && Game.startTimer <= 0.1) {
					return 1;
				}
				else {
					return 0;
				}
			}
		});
		shiftDownInput = new HvlInput(new HvlInput.InputFilter() {
			@Override
			public float getCurrentOutput() {
				if(inputMethod != null && inputMethod.isShiftingDown()) {
					return 1;
				}
				else {
					return 0;
				}
			}
		});
		
		shiftUpInput.setPressedAction(new HvlAction1<HvlInput>() {
			@Override
			public void run(HvlInput a) {
				if(currentGear < selectedCar.GEAR_COUNT) {
					currentGear++;
					currentRPMGoal = (int)((float)selectedCar.MAX_RPM * (float)speed / (float)selectedCar.maxSpeedsPerGear[currentGear - 1]);				
				}
			}
		});
		shiftDownInput.setPressedAction(new HvlAction1<HvlInput>() {
			@Override
			public void run(HvlInput a) {
				if(currentGear > 1) {
					currentGear--;
					currentRPMGoal = (int)((float)selectedCar.MAX_RPM * (float)speed / (float)selectedCar.maxSpeedsPerGear[currentGear - 1]);
				}
			}
		});
	}
	
	public void draw(float delta) {
		if(MysteryUnlocker.myUnlockedEffect != null){
			CarEffectApplicator.drawCar(MysteryUnlocker.myUnlockedEffect, xPos, yPos, turnAngle, selectedCar.textureSelect, MenuManager.color);
		}else{
			hvlRotate(xPos,yPos-30, turnAngle);
			hvlDrawQuadc(xPos, yPos, 100, 100, MainClient.getTexture(selectedCar.textureSelect));
			hvlDrawQuadc(xPos, yPos, 100, 100, MainClient.getTexture(selectedCar.textureSelect + 1), MenuManager.color);
			hvlResetRotation();
		}
		
	}
	
	public void drawUI(float delta) {
		drawTach(190, 870);
		drawSpeed(1730, 870);
	}
	
	public void update(float delta) {
		updateTrackAndBorderCollisions(delta);
		
		if(inputMethod != null && inputMethod.isAccelerating()) {
			rpmMod = (selectedCar.ACCELERATION / currentGear);
		} else if(inputMethod != null && inputMethod.isBreaking()) {
			rpmMod = (-50 / currentGear);
		} else {
			rpmMod = (-Game.FRICTION/currentGear);
		}
		
		if(Game.startTimer <= 0.1) {
			speedGoal = HvlMath.stepTowards(speedGoal, 0.5f * 142 * delta, 
					(((float)currentRPMGoal / (float)selectedCar.MAX_RPM) * (float)selectedCar.maxSpeedsPerGear[currentGear - 1]));
		}
		if(speedGoal <= 0) {speedGoal = 0;}
		if(currentRPMGoal <= selectedCar.MIN_RPM) {currentRPMGoal = selectedCar.MIN_RPM;}
		if(currentRPMGoal >= selectedCar.MAX_RPM) {currentRPMGoal = selectedCar.MAX_RPM;}
		if(Game.startTimer >= 0.1 && currentRPMGoal > 3000) {currentRPMGoal = 3000;}
		if(!onTrack) {
			if(inputMethod != null && inputMethod.isAccelerating()) {
				rpmMod = (float)(selectedCar.ACCELERATION / currentGear/3.5);
			}
			if(currentRPMGoal > 1300) {
				currentRPMGoal -= (20 * 142 *delta);
			}		
		}
		if(hitWall) {
			if(xSpeed > 0) {xPos -= 32;}
			if(xSpeed < 0) {xPos += 32;}
			if(ySpeed > 0) {yPos += 32;}
			if(ySpeed < 0) {yPos -= 32;}
			xSpeed = 0;
			ySpeed = 0;
			currentRPMGoal = 0;
			throttle = 0;
		}
		
		currentRPMGoal += (rpmMod * 142 *delta);
	
		currentRPM = (int) HvlMath.stepTowards(currentRPM, 35 * 142 * delta, currentRPMGoal); 
		speed = (int) HvlMath.stepTowards(speed, 1, speedGoal);

		throttle = speed;
	
		if(inputMethod != null && inputMethod.isTurningLeft() && throttle > 0) {
			turnAngleSpeed = -1 * Math.abs(130 - throttle)/142;
		}
		if(inputMethod != null && inputMethod.isTurningRight() && throttle > 0) {
			turnAngleSpeed = Math.abs(130 - throttle)/142;
		}
		
		turnAngle += turnAngleSpeed * 150 * delta;
		turnAngleSpeed = HvlMath.stepTowards(turnAngleSpeed, (float)(selectedCar.TIRE_GRIP/throttle) * 142 * delta, 0);

		ySpeed = (float) ((throttle / 12) * Math.cos(Math.toRadians(turnAngle)));
		xSpeed = (float) ((throttle / 12) * Math.sin(Math.toRadians(turnAngle)));

		yPos -= ySpeed * delta * 142; 
		xPos += xSpeed * delta * 142;
	}
	
	private Track closestTrack() {
		Track closestTrack = null;
		for(Track fullTrack : TrackGenerator.tracks) {
			if(closestTrack == null) {
				closestTrack = fullTrack;
			}
			float distance = HvlMath.distance(xPos, yPos, closestTrack.xPos, closestTrack.yPos);
			float distanceTest = HvlMath.distance(xPos, yPos, fullTrack.xPos, fullTrack.yPos);

			if(distanceTest < distance) {
				closestTrack = fullTrack;
			}
		}
		return closestTrack;
	}
	
	private Border closestBorder() {
		Border closestBorder = null;
		for(Border allBorders : TrackGenerator.borders) {
			if(closestBorder == null) {
				closestBorder = allBorders;
			}
			float distance = HvlMath.distance(xPos, yPos, closestBorder.xPos, closestBorder.yPos);
			float distanceTest = HvlMath.distance(xPos, yPos, allBorders.xPos, allBorders.yPos);

			if(distanceTest < distance) {
				closestBorder = allBorders;
			}
		}
		return closestBorder;
	}
	
	public void updateTrackAndBorderCollisions(float delta) {
	
		if(this.xPos <= closestTrack().xPos  + (Track.TRACK_SIZE/2) && xPos >= closestTrack().xPos - (Track.TRACK_SIZE/2)
				&& this.yPos <= closestTrack().yPos + (Track.TRACK_SIZE/2)&& yPos >= closestTrack().yPos - (Track.TRACK_SIZE/2)) {	
			onTrack = true;
		}else {
			onTrack = false;
		}
		if(xPos <= closestTrack().xPos  + (Track.TRACK_SIZE/2) && xPos >= closestTrack().xPos - (Track.TRACK_SIZE/2)
				&& yPos <= closestTrack().yPos + (Track.TRACK_SIZE/2)&& yPos >= closestTrack().yPos - (Track.TRACK_SIZE/2) && closestTrack().textureSelect == 4) {
			onTrack = true;
			trackComplete = true;
		}
		
		if(TrackGenerator.borders.size() > 0) {
			if(xPos > closestBorder().xPos - closestBorder().xSize*32 && xPos < closestBorder().xPos + closestBorder().xSize*32) {
				if(yPos > closestBorder().yPos - closestBorder().ySize*32 && yPos < closestBorder().yPos + closestBorder().ySize*32) {
					hitWall = true;
				} else {
					hitWall = false;
				}
			}
			else {
				hitWall = false;
			}
		}
	}
	
	public void drawTach(int x, int y) {
		accurateAngleRPM = HvlMath.map(currentRPM, 0, 8000, -145, 145);
		hvlDrawQuadc(x, y, 330, 330, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);
		hvlDrawQuadc(x,y, 330, 330, MainClient.getTexture(MainClient.TACH_INDEX));
		hvlRotate(x, y, accurateAngleRPM);
		hvlDrawQuadc(x, y, 5, 255, MainClient.getTexture(MainClient.NEEDLE_INDEX));
		hvlDrawQuadc(x, y, 30, 30, MainClient.getTexture(MainClient.CIRCLE_INDEX));
		hvlDrawQuadc(x, y, 25, 25, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);
		hvlResetRotation();
		for(float i = 0; i < 62; i++) {
			double radiansRot = (Math.toRadians(4.8) * i)-Math.toRadians(-124);
			hvlRotate(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137),(float)radiansRot * i);
			hvlDrawQuadc(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), 5, 30, new Color(0,0,255,0.4f));
			hvlResetRotation();
		}
		for(float i = 0; i < 9; i++) {
			double radiansRot = (Math.toRadians(37) * i)-Math.toRadians(-123);
			if(i == 8 || i == 7) {
				MainClient.gameFont.drawWordc((int)(i)+"",x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), Color.red);
			} else {
				MainClient.gameFont.drawWordc((int)(i)+"",x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), Color.white);
			}
		}


		MainClient.gameFont.drawWordc(""+currentGear, x, y+75,Color.white);
		if(currentRPM >= selectedCar.MAX_RPM-500) {
			hvlDrawQuadc(x+30, y+75, 10,10, MainClient.getTexture(MainClient.CIRCLE_INDEX));
		}
	}
	public void drawSpeed(int x, int y) {
		accurateAngleSpeed = HvlMath.map(speed, 0, 180, -145, 145);
		hvlDrawQuadc(x, y, 330, 330, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);

		hvlDrawQuadc(x,y, 330, 330, MainClient.getTexture(MainClient.TACH_INDEX));
		hvlRotate(x, y,accurateAngleSpeed);
		hvlDrawQuadc(x, y, 5, 255, MainClient.getTexture(MainClient.NEEDLE_INDEX));
		hvlDrawQuadc(x, y, 30, 30, MainClient.getTexture(MainClient.CIRCLE_INDEX));
		hvlDrawQuadc(x, y, 25, 25, MainClient.getTexture(MainClient.CIRCLE_INDEX), Color.black);

		hvlResetRotation();
		for(float i = 0; i < 62; i++) {
			double radiansRot = (Math.toRadians(4.8) * i)-Math.toRadians(-124);
			hvlRotate(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137),(float)radiansRot * i);
			hvlDrawQuadc(x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), 5, 30, new Color(0,0,255,0.4f));
			hvlResetRotation();
		}
		for(float i = 0; i < 10; i++) {
			double radiansRot = (Math.toRadians(33) * i)-Math.toRadians(-123);
			MainClient.gameFont.drawWordc((int)(i*20)+"",x+(float)(Math.cos(radiansRot) * 137), y+(float)(Math.sin(radiansRot) * 137), Color.white);
		}
		MainClient.gameFont.drawWordc(speed+" MPH",x, y+75, Color.white);
	}
	
	public float getXPos() {
		return xPos;
	}
	
	public float getYPos() {
		return yPos;
	}
	
}
