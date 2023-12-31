package com.engine2d.src.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class Model {
	private int draw_count;
	private int v_id;
	private int t_id;
	
	private int i_id;
	
	public Model(float[] vertices, float[] tex_coords, int[] indices) {
		draw_count = indices.length;
		
		v_id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, v_id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createBuffer(vertices), GL15.GL_STATIC_DRAW);
		
		t_id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, t_id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createBuffer(tex_coords), GL15.GL_STATIC_DRAW);
		
		i_id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, i_id);
		
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
		buffer.put(indices);
		buffer.flip();
		
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	protected void finalize() {
		GL20.glDeleteBuffers(v_id);
		GL20.glDeleteBuffers(t_id);
		GL20.glDeleteBuffers(i_id);
	}
	
	public void render() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, v_id);
		GL20.glVertexAttribPointer(0, 3, GL15.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, t_id);
		GL20.glVertexAttribPointer(1, 2, GL15.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, i_id);
		GL15.glDrawElements(GL15.GL_TRIANGLES, draw_count, GL15.GL_UNSIGNED_INT, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);

	}
	private FloatBuffer createBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
