package ferdiaMT;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Memory {

	Stack stack = new Stack(16);
	private int[] gameBytes;
	byte[] tempBytes;
	private int[] ram;
	private int[] fontFace = { 
			0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
			0x20, 0x60, 0x20, 0x20, 0x70, // 1
			0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
			0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
			0x90, 0x90, 0xF0, 0x10, 0x10, // 4
			0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
			0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
			0xF0, 0x10, 0x20, 0x40, 0x40, // 7
			0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
			0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
			0xF0, 0x90, 0xF0, 0x90, 0x90, // A
			0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
			0xF0, 0x80, 0x80, 0x80, 0xF0, // C
			0xE0, 0x90, 0x90, 0x90, 0xE0, // D
			0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
			0xF0, 0x80, 0xF0, 0x80, 0x80 // F
	};

	private int PC = 0x200; // programCounter , points at current instruction
	private int I = 0; // index, points at current memory slot
	private int[] reg = new int[16];
	Random random = new Random();

	public int code;
	Chip8 chip8;
	private Display display;
	private KeyPad keyPad;
	private DelayTimer delayTimer;

	public Memory(Chip8 chip8, Display display, KeyPad keyPad, DelayTimer delayTimer, String selectedRom) {
		this.chip8 = chip8;
		this.display = display;
		this.keyPad = keyPad;
		this.delayTimer = delayTimer;
		ram = new int[0x1000];
		initFont();
		loadROM(selectedRom);
	}

	private void initFont() {
		for (int i = 0x050; i <= 0x09f; i++) { // 80 to 159 , 80
			ram[i] =  fontFace[i - 0x050];
		}
	}

	public void loadROM(String selectedRom) {
		File file = new File("./ROM/"+selectedRom);;
		try {
			tempBytes = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameBytes = new int[tempBytes.length];
		for (int i = 0; i < tempBytes.length; i++) {
			 int mask= (tempBytes[i]& 0xFF);
			 gameBytes[i] =  mask;
			// System.out.println(Integer.toHexString(mask));
		}
//		System.out.println("Len" + " "+ gameBytes.length);

		for (int i = 0x200; i < (gameBytes.length + 0x200); i++) {
			ram[i] = gameBytes[i - 0x200];
		}

	}

	public void fetch() {
		code = ((ram[PC] << 8) | ram[PC + 1]); // grabs code 2 bytes at a time
		PC += 2;

	}

	private void draw(int x, int y, int n) {
		
		x = x%64;
		y = y%32;
		
		reg[0xf] = 0;

		for (int row = 0; row < n; row++) {
			int spriteByte = ram[I + row]; // Get sprite data from memory

			for (int col = 0; col < 8; col++) {
				if ((spriteByte & (0x80 >> col)) != 0) {
					int screenX = (x + col) % 64;
					int screenY = (y + row) % 32;
					
					int index = screenY * 64 + screenX;
					if(display.getPixel(index)) {
						reg[0xF] = 1;
					}
					

					display.togglePixel(index);
				}
			}
		}

	}

	public void decode() {

		System.out.println(Integer.toHexString(code));
		int firstChunk = (code & 0xF000) >>> 12; // returns a 4 bit code
		// System.out.println(Integer.toHexString(firstChunk));
		int x = (code & 0x0F00) >>> 8;
		int y = (code & 0x00F0) >>> 4;
		int n = (code & 0x000F);
		int nn = (code & 0x00FF);
		int nnn = (code & 0x0FFF);

		switch (firstChunk) {

		case 0x0:
			if (nn == 0xE0) {
				display.clear();
			}
			if (nn == 0xEE) {
				PC = stack.pop();
			}

			break;
		case 0x1:
			PC = nnn;
			break;
		case 0x2:
			stack.push(PC);
			PC = nnn;
			break;
		case 0x3:
			if (reg[x] == nn) {
				PC += 2;
			}
			break;
		case 0x4:
			if (reg[x] != nn) {
				PC += 2;
			}
			break;
		case 0x5:
			if(reg[x] == reg[y]) {
				PC+=2;
			}
			break;
		case 0x6:
			reg[x] = nn;
			break;
		case 0x7:
			reg[x] = (reg[x] + nn) & 0xFF;
			break;
		case 0x8:
			if (n == 0) {
				reg[x] = reg[y];
			}
			if (n == 1) {
				reg[x] = (reg[x] | reg[y]);
			}
			if (n == 2) {
				reg[x] = (reg[x] & reg[y]);
			}
			if (n == 3) {
				reg[x] = (reg[x] ^ reg[y]);
			}
			if (n == 4) {
				reg[x] = (reg[x] + reg[y]);
				if(reg[x]>255) {
					reg[0xF] = 1;
				}else {
					reg[0xF] = 0;
				}
				reg[x] = reg[x] & 0xFF;
			}
			if (n == 5) {
				if(reg[x] > reg[y]) {
					reg[0xF]=1;
				}else {
					reg[0xF]=0;
				}
				reg[x] = (reg[x] - reg[y]) & 0xFF;
			}
			if (n == 6) {
				reg[0xF] = reg[y] & 0x1; 
		        reg[x] = (reg[y] >> 1)& 0xFF;
			}

			if (n == 7) {
				if(reg[x] < reg[y]) {
					reg[0xF]=1;
				}else {
					reg[0xF]=0;
				}
				
				reg[x] = (reg[y] - reg[x]) &0xFF;
			}

			if (n == 0xE) {
				reg[0xF] = (reg[y] >> 7) & 0x1; 
		        reg[x] = (reg[y] << 1) & 0xFF;
			}

			break;
		case 0x9:
			if(reg[x] != reg[y]) {
				PC+=2;
			}
			
			break;
		case 0xa:
			I = nnn;
			break;
		case 0xb:
			PC = nnn + reg[0];
			break;
		case 0xc:
			reg[x] = random.nextInt(0XFF) & nn;
			break;
		case 0xd:
			draw(reg[x], reg[y], n);

			break;
		case 0xE: // EX9E is where im leaving it for tonight
			if (nn == 0x9E) {
				if (keyPad.keys[reg[x]]) {
					PC+=2;
				}
			}
			if (nn == 0xA1) {
				if (!keyPad.keys[reg[x]]) {
					PC+=2;
				}
			}
			break;
		case 0xF:
			if (nn == 0x07) {
				reg[x] = delayTimer.frame;
			}
			if (nn == 0x15) {
				delayTimer.setFrame(reg[x]);
			}
			if (nn == 0x1e) {
				I += reg[x];
			}
			if (nn == 0x0A) { // this is the block until pressed, have to double check this one
	            boolean keyPressed = false;
	            for (int i = 0; i < 16; i++) {
	                if (keyPad.keys[i]) {
	                    reg[x] = i;
	                    keyPressed = true;
	                    break;
	                }
	            }
	            if (!keyPressed) {
	                PC -= 2;
	            }
			}
			if (nn == 0x29) {
				I = reg[x]*5;
			}

			if (nn == 0x33) {
			    ram[I] = reg[x] / 100;
			    ram[I + 1] = (reg[x] / 10) % 10;
			    ram[I + 2] = reg[x] % 10;   
			}

			if (nn == 0x55) {
				for (int i = 0; i <= x; i++) {
					ram[I + i] =reg[i];
				}
				I+=x+1;
			}

			if (nn == 0x65) {
				for (int i = 0; i <= x; i++) {
					reg[i] = ram[I + i] & 0xFF ;
				}
				I+=x+1;
			}

			break;

		default:
			System.out.println("Someone has gotten lazy and forgot to implement" + Integer.toHexString(code));

		}
	}

}
