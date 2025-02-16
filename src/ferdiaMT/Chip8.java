package ferdiaMT;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Chip8 {
	
	private static final String SELECTED_ROM = "Space Invaders.ch8"; // put the fileName of the rom you want to run here
	
	private static final int DISPLAY_WIDTH = 64;
	private static final int DISPLAY_HEIGHT = 32;
	private static final int SCALE  = 10;
	private boolean keys[] = new boolean[16];
	
	private static Memory memory;
	private static Display display;
	static DelayTimer delayTimer;
	public static KeyPad keyPad;
	
	public static void main(String[] args) {
		
		Chip8 chip8 = new Chip8();
		
		JFrame frame = new JFrame("Chip-8");
		
		display = new Display(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		delayTimer = new DelayTimer(chip8);
		keyPad = new KeyPad(display);
		memory = new Memory(chip8, display, keyPad, delayTimer, SELECTED_ROM);
		
		display.setMinimumSize(new Dimension(DISPLAY_WIDTH * SCALE , DISPLAY_HEIGHT  * SCALE ));
		display.setPreferredSize(new Dimension(DISPLAY_WIDTH  * SCALE , DISPLAY_HEIGHT  * SCALE ));
		display.setMaximumSize(new Dimension(DISPLAY_WIDTH  * SCALE , DISPLAY_HEIGHT * SCALE ));
		
		frame.add(display);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		delayTimer.start();
	}

	public void tick() { // this is the fetch, decode and execute cycle, with decode acting as both decode and execute
		memory.fetch();
		memory.decode();
		display.render();
	}
	
}
