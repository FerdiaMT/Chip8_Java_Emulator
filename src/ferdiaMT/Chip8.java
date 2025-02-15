package ferdiaMT;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Chip8 {
	
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
		memory = new Memory(chip8, display);
		
		
		display.setMinimumSize(new Dimension(DISPLAY_WIDTH * SCALE , DISPLAY_HEIGHT  * SCALE ));
		display.setPreferredSize(new Dimension(DISPLAY_WIDTH  * SCALE , DISPLAY_HEIGHT  * SCALE ));
		display.setMaximumSize(new Dimension(DISPLAY_WIDTH  * SCALE , DISPLAY_HEIGHT * SCALE ));
		
		frame.add(display);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		
		memory.loadROM();
		
		delayTimer.start();
		// want to start ticking here
	}

	public void tick() {
		memory.fetch();
		memory.decode();
		
//		display.render();
//		if(keyPad.keys[0]) {
//			System.out.println("Hello World");
//		}
	}
	
}
