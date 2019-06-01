package com.samuel.client;
import java.util.ArrayList;

import com.osreboot.hvol.base.HvlGameInfo;
import com.osreboot.hvol.dclient.HvlTemplateDClient2D;
import com.osreboot.ridhvl.display.collection.HvlDisplayModeDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.painter2d.HvlFontPainter2D;
import com.samuel.client.effects.CarEffectApplicator;
import com.samuel.client.effects.MysteryUnlocker;

public class MainClient extends HvlTemplateDClient2D{
	
	public static PlayerInput inputType;
	public static ArrayList<PlayerInput> inputs;
	
	public MainClient(HvlGameInfo gameInfoArg){
		super(144, 1920, 1080, "Zoom Zoom", new HvlDisplayModeDefault(), "localhost", 25565, 0.016f, gameInfoArg);
	}
	static HvlFontPainter2D gameFont;
	public final static int NEEDLE_INDEX = 0;
	public final static int TACH_INDEX = 1;
	public final static int FONT_INDEX = 2;
	public final static int CIRCLE_INDEX = 3;
	public final static int WRX_INDEX = 4;
	public final static int WRX_A_INDEX = 5;
	public final static int GTI_INDEX = 6;
	public final static int GTI_A_INDEX = 7;
	public final static int NSX_INDEX = 8;
	public final static int NSX_A_INDEX = 9;
	public final static int STR_ROAD_INDEX = 10;
	public final static int TURN_ROAD_INDEX = 11;
	public final static int TREE_INDEX = 12;
	public final static int FLOWER_INDEX = 13;
	public final static int FINISH_INDEX = 14;
	public final static int TEST_TRACK_INDEX = 15;
	public final static int CURVE_TRACK_INDEX = 16;
	public final static int OSHELL_TRACK_INDEX = 17;
	public final static int PLASMA1_INDEX = 18;
	public final static int PLASMA2_INDEX = 19;
	public final static int CAMARO_INDEX = 20;
	public final static int CAMARO_A_INDEX = 21;
	public final static int DRIFTER_INDEX = 22;
	public final static int UNDERGLOW_INDEX = 23;
	public final static int YOUR_MOM_INDEX = 24;
	public final static int MINI_INDEX = 25;
	public final static int MINI_A_INDEX = 26;
	public final static int SUPRA_INDEX = 27;
	public final static int SUPRA_A_INDEX = 28;
	public final static int STOCK_INDEX = 29;
	public final static int SHELL_INDEX = 30;
	public final static int RETRO_INDEX = 31;
	public final static int THORNS_INDEX = 32;
	public final static int TIRE_INDEX = 33;
	public final static int DRAG_INDEX = 34;

	@Override
	public void initialize(){		
		getTextureLoader().loadResource("needle"); //0
		getTextureLoader().loadResource("tach"); //1
		getTextureLoader().loadResource("osfont");//2
		getTextureLoader().loadResource("redCircle");//3

		getTextureLoader().loadResource("wrx");//4
		getTextureLoader().loadResource("ALPHAwrx");//5
		getTextureLoader().loadResource("gti");//6
		getTextureLoader().loadResource("ALPHAgti");//7
		getTextureLoader().loadResource("NSX");//8
		getTextureLoader().loadResource("ALPHANSX");//9

		getTextureLoader().loadResource("StraightRoad");//10
		getTextureLoader().loadResource("fRightTurn"); //11
		getTextureLoader().loadResource("Tree");//12
		getTextureLoader().loadResource("flower");//13
		getTextureLoader().loadResource("FinishLine");//14
		getTextureLoader().loadResource("testTrack");//15
		getTextureLoader().loadResource("curveyboi");//16
		getTextureLoader().loadResource("oshell");//17
		getTextureLoader().loadResource("Plasma1");//18
		getTextureLoader().loadResource("Plasma2");//19
		getTextureLoader().loadResource("Camaro");//20
		getTextureLoader().loadResource("ALPHACamaro");//21
		getTextureLoader().loadResource("Drifter");//22
		getTextureLoader().loadResource("Underglow");//23
		getTextureLoader().loadResource("yourmom");//24
		getTextureLoader().loadResource("Mini Cooper");//25
		getTextureLoader().loadResource("ALPHAMini Cooper");//26
		getTextureLoader().loadResource("supra");//27
		getTextureLoader().loadResource("ALPHAsupra");//28
		getTextureLoader().loadResource("stock");//29
		getTextureLoader().loadResource("Shell");//30
		getTextureLoader().loadResource("retrorev");//31
		getTextureLoader().loadResource("Thorns");//32
		getTextureLoader().loadResource("tire");//33
		getTextureLoader().loadResource("drag");//34

		gameFont =  new HvlFontPainter2D(getTexture(FONT_INDEX), HvlFontPainter2D.Preset.FP_INOFFICIAL,.18f,8f,0); //font definition
		MenuManager.initialize();
		CarEffectApplicator.initialize();
		MysteryUnlocker.initialize();
	}

	@Override
	public void update(float delta){
		//gameFont.drawWord(getClient().getTable().toString(), 0, 0, Color.darkGray);
		MenuManager.update(delta);
	}

	@Override
	public void onConnection(){

	}

	@Override
	public void onDisconnection(){
		HvlMenu.setCurrent(MenuManager.ip);
	}
}
