package com.samuel.client;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.io.Serializable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.input.HvlInput;
import com.samuel.Main;
import com.samuel.Network;
import com.samuel.NetworkMain;
import com.samuel.client.effects.CarEffectApplicator;
import com.samuel.client.effects.MysteryUnlocker;

public class Player implements Cloneable{
	
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
	public boolean dead;
	float accurateAngleRPM;
	float accurateAngleSpeed;
	float sittingTimer;
	HvlInput shiftUpInput;
	HvlInput shiftDownInput;
	
	public float finalTrackTime;
	Car selectedCar;
	
	public Network decisionNet;
	public float fitness;
	
	public Player(){
		xPos = Display.getWidth()/2;
		yPos = Display.getHeight()/2;
		selectedCar = MenuManager.selectedCar;
		currentGear = 1;
		currentRPM = selectedCar.MIN_RPM;
		currentRPMGoal = selectedCar.MIN_RPM;
		speedGoal = 0;
		currentRPMGoal = 0;
		speed = 0;
		turnAngle = 0;
		trackComplete = false;
		dead = false;
		sittingTimer = 6;
		
		decisionNet = new Network(7,6,6);
		
		shiftUpInput = new HvlInput(new HvlInput.InputFilter() {
			@Override
			public float getCurrentOutput() {
				if(isShiftingUp() && Game.startTimer <= 0.1) {
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
				if(isShiftingDown()) {
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
		decisionNet.draw(delta, MainClient.gameFont, MainClient.getTexture(MainClient.NODE_INDEX), 0.4f);
	}
	
	public void update(float delta) {
		if(!dead) {
			if(xSpeed == 0 && ySpeed == 0) {
				sittingTimer -= delta;
			} else {
				sittingTimer = 3;
			}
			updateTrackAndBorderCollisions(delta);
			updateNetwork();
			fitness = GeneticsHandler.calcFitness(this);
			/*
			if(fitness < GeneticsHandler.hero.fitness) {
				GeneticsHandler.setHero(this);
			}
			*/
			if(isAccelerating()) {
				rpmMod = (selectedCar.ACCELERATION / currentGear);
			} else if(isBraking()) {
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
				if(isAccelerating()) {
					rpmMod = (float)(selectedCar.ACCELERATION / currentGear/5);
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
		
			if(isTurningLeft() && throttle > 0) {
				turnAngleSpeed = -1 * Math.abs(130 - throttle)/142;
			}
			if(isTurningRight() && throttle > 0) {
				turnAngleSpeed = Math.abs(130 - throttle)/142;
			}
			
			turnAngle += turnAngleSpeed * 150 * delta;
			turnAngleSpeed = HvlMath.stepTowards(turnAngleSpeed, (float)(selectedCar.TIRE_GRIP/throttle) * 142 * delta, 0);

			ySpeed = (float) ((throttle / 12) * Math.cos(Math.toRadians(turnAngle)));
			xSpeed = (float) ((throttle / 12) * Math.sin(Math.toRadians(turnAngle)));

			yPos -= ySpeed * delta * 142; 
			xPos += xSpeed * delta * 142;
		}
	}
	
	public boolean isDead() {
		boolean marginExceeded = HvlMath.distance(closestTrack().xPos, closestTrack().yPos, xPos, yPos) > Track.TRACK_SIZE;
		if(sittingTimer <= 0 || marginExceeded || trackComplete || hitWall) {
			return true;
		}
		return false;
	}
	
	public void die() {
		dead = true;
	}
	
	public Track closestTrack() {
		Track closestTrack = null;
		for(Track fullTrack : Game.trackGen.tracks) {
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
		for(Border allBorders : Game.trackGen.borders) {
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
			finalTrackTime = Game.trackTimer;
			//System.out.println(finalTrackTime);
		}
		
		if(Game.trackGen.borders.size() > 0) {
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
	
	/**
	 * CUSTOM AI STUFF
	 * 
	 * INPUTS:
	 *  
	 * Track type
	 * gear
	 * RPM
	 * speed
	 * turn angle
	 * onTrack
	 * 
	 * OUTPUTS:
	 * 
	 * Gear up
	 * gear down
	 * accelerate
	 * turn right
	 * turn left
	 * brake
	 */
	
	private void updateNetwork() {
		
		int finishIndex = Game.trackGen.tracks.size()-1;
		int playerTrack = Game.trackGen.tracks.indexOf(closestTrack());
		
		if(playerTrack <= finishIndex - 2) {decisionNet.layers.get(0).nodes.get(1).value = Game.trackGen.tracks.get(Game.trackGen.tracks.indexOf(closestTrack())+2).turnDirection * 1.5f;}
		if(playerTrack <= finishIndex - 1) {decisionNet.layers.get(0).nodes.get(0).value = Game.trackGen.tracks.get(Game.trackGen.tracks.indexOf(closestTrack())+1).turnDirection * 2.0f;}
		decisionNet.layers.get(0).nodes.get(2).value = closestTrack().turnDirection * 2.5f;
		if(playerTrack > 0) {decisionNet.layers.get(0).nodes.get(3).value = Game.trackGen.tracks.get(Game.trackGen.tracks.indexOf(closestTrack())-1).turnDirection * 2.0f;}
		if(playerTrack > 1) {decisionNet.layers.get(0).nodes.get(4).value = Game.trackGen.tracks.get(Game.trackGen.tracks.indexOf(closestTrack())-2).turnDirection * 1.5f;}
		
		float yDistanceToCloseTrack = yPos > closestTrack().yPos ? HvlMath.distance(closestTrack().xPos, closestTrack().yPos, xPos, yPos) * Math.signum(ySpeed) : -HvlMath.distance(closestTrack().xPos, closestTrack().yPos, xPos, yPos) * Math.signum(ySpeed);
		float xDistanceToCloseTrack = xPos > closestTrack().xPos ? HvlMath.distance(closestTrack().xPos, closestTrack().yPos, xPos, yPos) * Math.signum(xSpeed): -HvlMath.distance(closestTrack().xPos, closestTrack().yPos, xPos, yPos) * Math.signum(xSpeed);
	
		if(closestTrack().textureSelect == 1 || closestTrack().textureSelect == 124 || closestTrack().textureSelect == 184 ||
				closestTrack().textureSelect == 3 || closestTrack().textureSelect == 136 || closestTrack().textureSelect == 200) {
			    decisionNet.layers.get(0).nodes.get(5).value = HvlMath.map(yDistanceToCloseTrack, -Track.TRACK_SIZE, Track.TRACK_SIZE, -1.0f, 1.0f) * 1.25f;
		} else {decisionNet.layers.get(0).nodes.get(5).value = 0;}
		
		if(closestTrack().textureSelect == 0 || closestTrack().textureSelect == 112 || closestTrack().textureSelect == 148 ||
				closestTrack().textureSelect == 2 || closestTrack().textureSelect == 172 || closestTrack().textureSelect == 160) {
			    decisionNet.layers.get(0).nodes.get(6).value = HvlMath.map(xDistanceToCloseTrack, -Track.TRACK_SIZE, Track.TRACK_SIZE, -1.0f, 1.0f) * 1.25f;
		} else {decisionNet.layers.get(0).nodes.get(6).value = 0;}
		
//		decisionNet.layers.get(0).nodes.get(7).value = HvlMath.map(currentGear, 1, selectedCar.GEAR_COUNT, 0, 1);	
//		decisionNet.layers.get(0).nodes.get(8).value = HvlMath.map(currentRPM, 0, selectedCar.MAX_RPM, 0, 1);
//		decisionNet.layers.get(0).nodes.get(9).value = HvlMath.map(speed, 0, selectedCar.maxSpeedsPerGear[currentGear-1], 0, 1);
//		decisionNet.layers.get(0).nodes.get(10).value = HvlMath.map(turnAngle, -360, 360, 0, 1);
		
		NetworkMain.propogateAsNetwork(decisionNet);
	}
	
	public boolean isShiftingUp() {
		if(decisionNet.lastLayer().nodes.get(0).value > 0.75) {
			return true;
		}
		return false;
	}
	
	public boolean isShiftingDown() {
		if(decisionNet.lastLayer().nodes.get(1).value > 0.75) {
			return true;
		}
		return false;
	}

	public boolean isAccelerating() {
		if(decisionNet.lastLayer().nodes.get(2).value > 0.75) {
			return true;
		}
		return false;
	}
	public boolean isTurningLeft() {
		if(decisionNet.lastLayer().nodes.get(3).value > 0.75) {
			return true;
		}
		return false;
	}

	public boolean isTurningRight() {
		if(decisionNet.lastLayer().nodes.get(4).value > 0.75) {
			return true;
		}
		return false;
	}

	public boolean isBraking() {
		if(decisionNet.lastLayer().nodes.get(5).value > 0.75) {
			return true;
		}
		return false;
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
	
	public void setXPos(float x) {
		xPos = x;
	}
	
	public void setYPos(float y) {
		yPos = y;
	}
	
	public float getFitness() {
		return fitness;
	}
	
	public void setNetwork(Network n) {
		decisionNet = n;
	}
	
	public Object clone() throws CloneNotSupportedException {
	    Player cloned = (Player)super.clone();  
	    return cloned;
	}
	
}
