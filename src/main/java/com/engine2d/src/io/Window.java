package com.engine2d.src.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

public class Window {
	private long windowID;
	
	private int width, height;
	
	private boolean fulscreen;
	
	private Input input;
	
	public static void setCallbacks() {
		GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {
			@Override
			public void invoke(int err, long desc) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(desc));
			}
			
		});

	}
	
	public Window() {
		setSize(640, 480);
		setFulscreen(false);
	}
														  
	public void createWindow(String Title) {
		
		windowID = GLFW.glfwCreateWindow(width, height, Title, fulscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
		
		if(windowID == 0) {
			throw new IllegalStateException("Could not Create GLFW window");
		}

		if(!fulscreen) {
			
			GLFWVidMode vid = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			if(vid == null) {
				throw new IllegalStateException("Could not get video mode");
			}else{
				GLFW.glfwSetWindowPos(windowID, (vid.width()-width)/2, (vid.height()-height)/2);
				GLFW.glfwShowWindow(windowID);
			}
			
		}
		GLFW.glfwMakeContextCurrent(windowID);
		
		input = new Input(windowID);
	}

	public boolean shouldClose() {
		return !GLFW.glfwWindowShouldClose(windowID);
	}
	
	public void updateWindow() {
		GLFW.glfwSwapBuffers(windowID);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setFulscreen(boolean fulscreen) {
		this.fulscreen = fulscreen;
	}
	public void update() {
		input.update();
		GLFW.glfwPollEvents();
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public boolean isFulscreen() {
		return fulscreen;
	}

	public long getWindowID() {
		return windowID;
	}

	public Input getInput() {
		return input;
	}
	
}
