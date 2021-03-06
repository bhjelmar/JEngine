package com.jengine.engine.graph;

import lombok.Getter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

	@Getter
	private int vaoId;
	private int posVboId;
	private final int idxVboId;
	private final int colorVboId;
	@Getter
	private final int vertexCount;

	public Mesh(float[] positions, float[] colors, int[] indices) {
		FloatBuffer posBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			vertexCount = indices.length;

			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			// position VAO
			posVboId = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, posVboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			// Colour VBO
			colorVboId = glGenBuffers();
			FloatBuffer colourBuffer = MemoryUtil.memAllocFloat(colors.length);
			colourBuffer.put(colors).flip();
			glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
			glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
			MemoryUtil.memFree(colourBuffer);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

			// index VBO
			idxVboId = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if(posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if(indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public void render() {
		// Draw the mesh
		glBindVertexArray(getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(posVboId);
		glDeleteBuffers(idxVboId);

		// delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

}
