package com.pepebecker;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class PBUtils {
	private static long lastFrame;
	
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public static int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	         
	    return delta;
	}
	
	public static int loadTexture(String string) {
		int tex;

		BufferedImage img = null;

		try {
			img = ImageIO.read(Camera.class.getResourceAsStream(string));
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		int width = img.getWidth();
		int height = img.getHeight();
		int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 3);
		tex = GL11.glGenTextures();

		for (int i = 0; i < pixels.length; i++) {
			byte r = (byte) ((pixels[i] >> 16) & 0xFF);
			byte g = (byte) ((pixels[i] >> 8) & 0xFF);
			byte b = (byte) (pixels[i] & 0xFF);

			buffer.put(r);
			buffer.put(g);
			buffer.put(b);
		}

		buffer.flip();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				buffer);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		return tex;
	}
	
	public static FloatBuffer asFloatBuffer(float[] values) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
}