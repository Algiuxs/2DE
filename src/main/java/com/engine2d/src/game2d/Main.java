package com.engine2d.src.game2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.engine2d.src.entity.Entity;
import com.engine2d.src.io.Timer;
import com.engine2d.src.io.Window;
import com.engine2d.src.render.Camera;
import com.engine2d.src.render.Shader;
//import render.Texture;
import com.engine2d.src.world.TileRenderer;
import com.engine2d.src.world.World;

public class Main {
	public Main(){
		Window.setCallbacks();

		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		Window window = new Window();
		
		//win.setSize(1200, 720);
		window.setFulscreen(false);
		window.createWindow("2D");
		
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		Camera cam = new Camera(window.getWidth(), window.getHeight());
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		//Texture tex = new Texture(null);
		
		TileRenderer tiles = new TileRenderer();
		Entity.initAsset();
		
//		float [] vertices = new float[] {
//				-0.5f, 0.5f, 0,
//				0.5f, 0.5f, 0,
//				0.5f, -0.5f, 0,
//				-0.5f, -0.5f, 0,
//		};
//		
//		float [] texture = new float[] {
//			0,0,
//			1,0,
//			1,1,
//			0,1,
//
//			
//		};
//		
//		int [] indices = new int[] {
//			0,1,2,
//			2,3,0
//		};
//		
//		Model model = new Model(vertices, texture, indices);
		Shader shader = new Shader("shader");
		World world = new World("Test");

		
		double frame_cap = 1.0/60.0;
		
		double frame_time = 0;
		int frames = 0;
		
		double time = Timer.getTime();
		double unprocessed = 0;
				
		while(!window.shouldClose()) {
			System.out.println(window.shouldClose());
			
			boolean can_render = false;
			
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			
			time = time_2;
			
			while(unprocessed >= frame_cap) {
				
				unprocessed -= frame_cap;
				can_render = true;
				
				
				if(window.getInput().isKeyReleased(GLFW.GLFW_KEY_ESCAPE)) {
					GLFW.glfwSetWindowShouldClose(window.getWindowID(), true);

				}			
				
				
				world.Update((float)frame_cap, window, cam);
				
				world.correctCamera(cam, window);
				
				window.update();
				
				if(frame_time >= 1.0) {
					frame_time = 0;
					System.out.println("FPS:" + frames);
					frames = 0;
				}
			}
			
			if(can_render) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			
//				shader.bind();
//				shader.setUniform("sampler", 0);
//				shader.setUniform("projection", cam.getProjection().mul(target));
			
				//model.render();
				//tex.bind(0);
				
				world.render(tiles, shader, cam, window);
				
				window.updateWindow();
				frames ++;
			}
		}
		Entity.deleteAsset();
		//tex.del();
		shader.desctruct();
		GLFW.glfwTerminate();
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
