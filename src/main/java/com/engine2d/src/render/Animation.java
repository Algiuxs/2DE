package com.engine2d.src.render;

import com.engine2d.src.io.Timer;

public class Animation {
	private Texture[] frames;
	private int pointer;
	
	private double elapsedTime;
	private double currentTime;
	private double lastTime;
	private double fps;
	
	public Animation(int Amount, int fps, String filename) {
		this.pointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0/fps;
		
		this.frames = new Texture[Amount];
		for(int i = 0; i < Amount; i++) {
			this.frames[i] = new Texture("/animation/" + filename + "_" + i + ".png");
		}
		
	}
	
	public void unbind() {
		bind(0);
	}
	
	public void bind(int sampler) {
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;
		
		if(elapsedTime>=fps) {
			elapsedTime = 0;
			pointer++;
		}
		
		if(pointer >= frames.length) {
			pointer = 0;
		}
		this.lastTime = currentTime;
		
		frames[pointer].bind(sampler);
			
	}
}
