package ferdiaMT;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyPad implements KeyListener{
	
	public boolean keys[] = new boolean[16];
	
	
	public KeyPad(Display display) {
		display.addKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		keyPress(e , true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		keyPress(e,false);
	}
	
	public void keyPress(KeyEvent e , boolean b) {
		
		if(e.getKeyCode() == KeyEvent.VK_1) {
			keys[0]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_2) {
			keys[1]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_3) {
			keys[2]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_4) {
			keys[3]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_Q) {
			keys[4]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			keys[5]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_E) {
			keys[6]=b;
			System.out.println(b);
		}
		if(e.getKeyCode() == KeyEvent.VK_R) {
			keys[7]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			keys[8]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			keys[9]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			keys[0xA]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_F) {
			keys[0xB]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			keys[0xC]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_X) {
			keys[0xD]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_C) {
			keys[0xE]=b;
		}
		if(e.getKeyCode() == KeyEvent.VK_V) {
			keys[0xF]=b;
		}
		
	}
	
	public boolean[] returnKey() {
		return keys;
	}

}
