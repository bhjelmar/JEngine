package com.jengine.game;

import com.jengine.engine.GameEngine;
import com.jengine.engine.IGameLogic;
import org.lwjgl.opengl.*;

import org.lwjgl.Version;
import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

	public static void main(String[] args) {
		try {
			boolean vSync = true;
			IGameLogic gameLogic = new DummyGame();
			GameEngine gameEngine = new GameEngine("GAME", 600, 400, vSync, gameLogic);
			gameEngine.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
