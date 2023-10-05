package com.engine2d.src.io;

import org.lwjgl.glfw.GLFW;

public class Input {
	private long window;
	
	private boolean keys[]; 
	
	public Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW.GLFW_KEY_LAST];
		for(int i = 32; i < GLFW.GLFW_KEY_LAST; i++) {
			keys[i] = false;
		}
	}

	public boolean isKeyDown(int key) {
		return GLFW.glfwGetKey(window, key ) == 1;
	}
	
	public boolean isKeyPressed(int key) {
		return (isKeyDown(key)) && !keys[key];
	}
		
	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key)) && keys[key];
	}
	
	public boolean isMouseDown(int button) {
		return GLFW.glfwGetMouseButton(window, button ) == 1;
	}

	public void update() {
		for(int i = 32; i < GLFW.GLFW_KEY_LAST; i++) {
			keys[i] = isKeyDown(i);
		}
	}
}
