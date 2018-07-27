package com.samuel.client.effects;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawLine;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.painter.HvlRenderFrame;
import com.osreboot.ridhvl.painter.HvlRenderFrame.FBOUnsupportedException;
import com.osreboot.ridhvl.painter.HvlShader;
import com.samuel.client.MainClient;

public class CarEffectApplicator {

	private static HvlRenderFrame maskFrame;
	private static HvlShader maskShader;

	public static void initialize(){
		try{
			maskFrame = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		}catch(FBOUnsupportedException e){
			e.printStackTrace();
		}
		maskShader = new HvlShader(HvlShader.PATH_SHADER_DEFAULT + "MaskShader" + HvlShader.SUFFIX_FRAGMENT);
	}

	public static void drawCar(CarEffect effect, float xArg, float yArg, float rotationArg, int carTextureArg, Color color){
		if(effect == null){
			hvlRotate(xArg, yArg - 30, rotationArg);
			hvlDrawQuadc(xArg, yArg, 100, 100, MainClient.getTexture(carTextureArg));
			hvlDrawQuadc(xArg, yArg, 100, 100, MainClient.getTexture(carTextureArg + 1), color);
			hvlResetRotation();
		}else if(effect == CarEffect.DRIFTER){
			hvlRotate(xArg, yArg - 30, rotationArg);
			hvlDrawQuadc(xArg, yArg, 100, 100, MainClient.getTexture(carTextureArg));
			maskFrame.doCapture(new HvlAction0(){
				@Override
				public void run(){
					hvlRotate(xArg, yArg, -rotationArg);
					hvlDrawQuadc(xArg, yArg, 2000, 2000, new Color(color.r - 0.5f, color.g - 0.5f, color.b - 0.5f));
					for(float f = 0; f < 10; f++){
						float offset = HvlMath.randomFloatBetween(-1000f, 1000f);
						hvlDrawLine(xArg + offset, yArg - 1000f, xArg + offset, yArg + 1000f, color, HvlMath.randomIntBetween(600, 1600));
					}
					hvlResetRotation();
				}
			});
			maskShader.doShade(new HvlAction0(){
				@Override
				public void run(){
					maskShader.sendRenderFrame("frame1", 2, maskFrame);
					hvlDrawQuadc(xArg, yArg, 100, 100, MainClient.getTexture(carTextureArg + 1), Color.white);
				}
			});
			hvlResetRotation();
		}
	}

}
