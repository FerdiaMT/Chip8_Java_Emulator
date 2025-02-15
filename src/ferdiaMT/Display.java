package ferdiaMT;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.*;

public class Display extends Canvas {

	private int width , height;
	private BufferedImage image;
	private byte[] pixels;
	
	public Display(int displayWidth, int displayHeight) {
		width = displayWidth;
		height = displayHeight;
		image  = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		pixels = ((DataBufferByte) image.getRaster().getDataBuffer() ).getData();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		
		bs.show();
	}
	


	public void clear() {
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		for(int i = 0 ; i < pixels.length; i++) {
			pixels[i] = 0;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		
		bs.show();
		

	}
	//0xF0 this is white

	public void draw(int x, int y, int n) {

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		for(int i = 0 ; i < pixels.length; i++) {
			pixels[i] = 0;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		
		bs.show();
	}

	public void drawPixel(int index) {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		pixels[index] = (byte) 0xF0;
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		
		bs.show();
	}
}
