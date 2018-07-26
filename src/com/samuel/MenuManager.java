package com.samuel;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.input.HvlInput;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlComponentDrawable;
import com.osreboot.ridhvl.menu.component.HvlLabel;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.HvlTextBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.collection.HvlLabeledButton;
import com.samuel.cars.AcuraNSX;
import com.samuel.cars.GolfGTI;
import com.samuel.cars.SubaruWRX;
import com.samuel.tracks.TestTrack;

public class MenuManager {
	public static HvlMenu game, menuCar, ip, menuMap;
	static HvlInput changeToGame;
	static Car selectedCar;
	static Level selectedTrack;
	
	public static void initialize() {
		game = new HvlMenu();
		menuCar = new HvlMenu();
		menuMap = new HvlMenu();
		ip = new HvlMenu();
		
		HvlComponentDefault.setDefault(HvlLabeledButton.class, new HvlLabeledButton.Builder().setWidth(100).setHeight(100).setFont(Main.gameFont).setTextColor(Color.white).setTextScale(0.25f).setOnDrawable(new HvlComponentDrawable() {
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height,Color.lightGray);
				
			}
		}).setOffDrawable(new HvlComponentDrawable() {
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height,Color.darkGray);	
			}
		}).setHoverDrawable(new HvlComponentDrawable() {
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height,Color.gray);
			}
		}).build());
		
		HvlComponentDefault.setDefault(HvlLabel.class, new HvlLabel.Builder().setColor(Color.white).setFont(Main.gameFont).setScale(0.8f).build());
		
		ip.add(new HvlArrangerBox.Builder().setStyle(ArrangementStyle.VERTICAL).setWidth(250).setHeight(400).setX((Display.getWidth()/2)-125).setY((Display.getHeight()/2)-200).build());

		ip.getFirstArrangerBox().add(new HvlSpacer(30, 30));
		ip.getFirstArrangerBox().add(new HvlLabel.Builder().setText("IP").build());
		ip.getFirstArrangerBox().add(new HvlSpacer(10, 10));

		
		ip.getFirstArrangerBox().add(new HvlTextBox.Builder().setWidth(200).setHeight(50).setFont(Main.gameFont).setTextColor(Color.darkGray).setTextScale(0.8f).setOffsetY(20).setOffsetX(20).setText("").setFocusedDrawable(new HvlComponentDrawable() {	
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.lightGray);	
			}
		}).setUnfocusedDrawable(new HvlComponentDrawable() {
			
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.green);	
			}
		}).build());
		
		ip.getFirstArrangerBox().add(new HvlSpacer(30, 30));
		ip.getFirstArrangerBox().add(new HvlLabel.Builder().setText("Username").build());
		ip.getFirstArrangerBox().add(new HvlSpacer(10, 10));

		
		ip.getFirstArrangerBox().add(new HvlTextBox.Builder().setWidth(200).setHeight(50).setFont(Main.gameFont).setTextColor(Color.darkGray).setTextScale(0.8f).setOffsetY(20).setOffsetX(20).setText("").setFocusedDrawable(new HvlComponentDrawable() {	
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.lightGray);	
			}
		}).setUnfocusedDrawable(new HvlComponentDrawable() {
			
			@Override
			public void draw(float delta, float x, float y, float width, float height) {
				hvlDrawQuad(x,y,width,height, Color.green);	
			}
		}).build());
		ip.getChildOfType(HvlArrangerBox.class, 0).add(new HvlSpacer(30, 30));
	
		ip.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Start Game").setTextScale(0.8f).setWidth(200).setClickedCommand(new HvlAction1<HvlButton>() {
			
			@Override
			public void run(HvlButton a) {
				if(!ip.getFirstArrangerBox().getFirstOfType(HvlTextBox.class).getText().equals("") && !ip.getFirstArrangerBox().getChildOfType(HvlTextBox.class, 1).getText().equals("")){
					String IP = ip.getFirstArrangerBox().getFirstOfType(HvlTextBox.class).getText();
					String USER_NAME = ip.getFirstArrangerBox().getChildOfType(HvlTextBox.class, 1).getText();
					HvlMenu.setCurrent(menuCar);
				}
			}
		}).build());

		
		menuCar.add(new HvlArrangerBox.Builder().setStyle(HvlArrangerBox.ArrangementStyle.HORIZONTAL).setX(Display.getWidth()/2).setY(Display.getHeight()/2).build());
		menuCar.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new SubaruWRX();
				HvlMenu.setCurrent(menuMap);
			}

		}).build());
		menuCar.getFirstArrangerBox().add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new GolfGTI();
				HvlMenu.setCurrent(menuMap);
			}

		}).build());
		HvlMenu.setCurrent(menuCar);
		menuCar.getFirstArrangerBox().add(new HvlSpacer(100,50));
		menuCar.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedCar = new AcuraNSX();
				HvlMenu.setCurrent(menuMap);
			}

		}).build());
		
		menuMap.add(new HvlArrangerBox.Builder().setStyle(HvlArrangerBox.ArrangementStyle.HORIZONTAL).setX(Display.getWidth()/2).setY(Display.getHeight()/2).build());
		menuMap.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setWidth(200).setHeight(200).setClickedCommand(new HvlAction1<HvlButton>(){

			@Override
			public void run(HvlButton a) {
				selectedTrack = new TestTrack();				
				HvlMenu.setCurrent(game);
				Game.initialize();
			}

		}).build());
		menuCar.getFirstArrangerBox().add(new HvlSpacer(100,50));
		HvlMenu.setCurrent(ip);
		
	}
	public static void update(float delta) {
		HvlMenu.updateMenus(delta);
		if(HvlMenu.getCurrent() == menuCar){
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+50, 100, 100, Main.getTexture(Main.WRX_INDEX));
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getY()+50, 100, 100, Main.getTexture(Main.GTI_INDEX));
			hvlDrawQuadc(menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getY()+50, 100, 100, Main.getTexture(Main.NSX_INDEX));

			Main.gameFont.drawWordc("Subaru WRX", menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+150, Color.red, 0.7f);
			Main.gameFont.drawWordc("Volkswagen GTI", menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 1).getY()+150, new Color(19,101,0), 0.7f);
			Main.gameFont.drawWordc("Acura NSX", menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getX()+50, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 2).getY()+150, new Color(129,0,0), 0.7f);

		} else if(HvlMenu.getCurrent() == game) {
			Game.update(delta);
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				HvlMenu.setCurrent(ip);
			}
		} else if(HvlMenu.getCurrent() == menuMap) {
			hvlDrawQuadc(menuMap.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+100, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+50, 200, 200, Main.getTexture(Main.TEST_TRACK_INDEX));

			Main.gameFont.drawWordc("Test Track", menuMap.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getX()+100, menuCar.getFirstArrangerBox().getChildOfType(HvlLabeledButton.class, 0).getY()+200, Color.white, 0.7f);
		}
		
	}
}
