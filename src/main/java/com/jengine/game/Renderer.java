package com.jengine.game;

import com.jengine.engine.GameItem;
import com.jengine.engine.Utils;
import com.jengine.engine.Window;
import com.jengine.engine.graph.ShaderProgram;
import com.jengine.engine.graph.Transformation;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

	private ShaderProgram shaderProgram;

	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;
	private Matrix4f projectionMatrix;

	private Transformation transformation;

	public Renderer() {
		transformation = new Transformation();
	}

	public void init(Window window) throws Exception {
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Utils.loadResource("/vertex.glsl"));
		shaderProgram.createFragmentShader(Utils.loadResource("/fragment.glsl"));
		shaderProgram.link();

		// create projection matrix
		float aspectRatio = (float) window.getWidth() / window.getHeight();
		projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
		shaderProgram.createUniform("projectionMatrix");

		// Create uniforms for world and projection matrices
		shaderProgram.createUniform("projectionMatrix");
		shaderProgram.createUniform("worldMatrix");

		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, GameItem[] gameItems) {
		clear();

		if(window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shaderProgram.bind();
		shaderProgram.setUniform("projectionMatrix", projectionMatrix);

		for(GameItem gameItem : gameItems) {
			Matrix4f worldMatrix =
					transformation.getWorldMatrix(
							gameItem.getPosition(),
							gameItem.getRotation(),
							gameItem.getScale());
			shaderProgram.setUniform("worldMatrix", worldMatrix);

			gameItem.getMesh().render();
		}

		shaderProgram.unbind();
	}

	public void cleanup() {
		if(shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}

}
