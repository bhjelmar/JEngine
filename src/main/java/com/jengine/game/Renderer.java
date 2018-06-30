package com.jengine.game;

import com.jengine.engine.Utils;
import com.jengine.engine.Window;
import com.jengine.engine.graph.Mesh;
import com.jengine.engine.graph.ShaderProgram;
import lombok.NoArgsConstructor;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@NoArgsConstructor
public class Renderer {

	private ShaderProgram shaderProgram;

	public void init() throws Exception {
		shaderProgram = new ShaderProgram();
		shaderProgram.createVertexShader(Utils.loadResource("/vertex.glsl"));
		shaderProgram.createFragmentShader(Utils.loadResource("/fragment.glsl"));
		shaderProgram.link();
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void render(Window window, Mesh mesh) {
		clear();

		if(window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shaderProgram.bind();

		// Draw the mesh
		glBindVertexArray(mesh.getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);

		shaderProgram.unbind();
	}

	public void cleanup() {
		if(shaderProgram != null) {
			shaderProgram.cleanup();
		}
	}

}
