package com.engine2d.src.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {
	public int id;
	public int width;
	public int height;
	
	public Texture(String filename) {

		BufferedImage Texture;
		try {
			Texture = ImageIO.read(new File("./src/main/java/com/engine2d/res/"+filename));
			width = Texture.getWidth();
			height = Texture.getHeight();
			int[] pixels_raw = new int [width * height * 4];
			pixels_raw = Texture.getRGB(0, 0, width, height, null, 0, width);
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			for(int w = 0; w < width; w++) {
				for(int h = 0; h < height; h++) {
					int pixel=pixels_raw[w * width + h];
					pixels.put((byte)((pixel >> 16) & 0xFF));//Red
					pixels.put((byte)((pixel >> 8) & 0xFF));//Green
					pixels.put((byte)(pixel & 0xFF));		//Blue
					pixels.put((byte)((pixel >> 24) & 0xFF));//Alpha
				}
			}
			pixels.flip();
			id = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}

//	protected void finalize() throws Throwable{
	//	try{
	//		GL13.glDeleteTextures(id);
	//	}finally {
	//		super.finalize();
	//	}
	//	
	//}
	public void del(){
		GL13.glDeleteTextures(id);
	}

	public void bind(int Sampler) {
		if(Sampler>=0 && Sampler <=31) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + Sampler);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		}

	}
	
}
