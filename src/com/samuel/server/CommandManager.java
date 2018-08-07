package com.samuel.server;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlTextBox;
import com.samuel.client.MenuManager;

public class CommandManager {

	private static ArrayList<CommandResponse> responseLine;

	public static void initialize(){
		responseLine = new ArrayList<>();
	}

	public static void update(float delta){
		boolean focused = MainServer.main.getFirstArrangerBox().getFirstOfType(HvlArrangerBox.class).getFirstOfType(HvlTextBox.class).isFocused();
		float index = responseLine.size() - 1f;
		for(CommandResponse r : responseLine){
			r.update(delta);
			MainServer.font.drawWord(r.response, 8, Display.getHeight() - 64 - (index * MainServer.font.getLineHeight(r.response)), new Color(0f, 1f, 0f, focused ? (index > 10 ? 0f : HvlMath.limit(HvlMath.map(r.age, 3f, 5f, 1f, 0.3f), 0.3f, 1f)) : HvlMath.map(r.age, 3f, 5f, 1f, 0f)));
			index--;
		}
	}

	public static void executeCommand(String command){
		String[] args = command.split(" ");
		if(command.startsWith("map")){
			if(args.length > 1){
				try{
					int map = Integer.parseInt(command.split(" ")[1]);
					if(map < MenuManager.NUM_TRACKS && map >= 0){
						MainServer.nextMap = map;
						new CommandResponse("Next map now set to: " + map);
					}else new CommandResponse("Can't find that map!");
				}catch(NumberFormatException e){}
			}else if(args.length == 1){
				new CommandResponse("The next map is: " + MainServer.nextMap);
			}else{
				new CommandResponse("Wrong number of arguments!");
			}
		}else if(command.startsWith("players") && args.length == 1){
			String output = "Connected players: ";
			for(String s : MainServer.usernames.values()) output += s + ", ";
			output.substring(0, output.length() - 3);
			new CommandResponse(output);
		}else{
			new CommandResponse("Command not found!");
		}
	}

	private static class CommandResponse{

		private String response;
		private float age;

		private CommandResponse(String responseArg){
			response = responseArg;
			age = 0f;
			responseLine.add(this);
		}

		private void update(float delta){
			age += delta;
		}

	}

}
