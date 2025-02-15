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
	private byte[] gameBytes;
	private byte[] ram;
	private int[] fontFace = 	{
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
			0xF0, 0x80, 0xF0, 0x80, 0x80  // F
		};
	
	private int PC = 0x200; // programCounter , points at current instruction
	private int I = 0; // index, points at current memory slot
	private int[] reg = new int[16];
	Random random = new Random();
	
	public int code;
	Chip8 chip8;
	private Display display;
	
	public Memory(Chip8 chip8 , Display display) {
		this.chip8 = chip8;
		this.display = display;
		ram = new byte[0x1000];
		initFont();
		loadROM();
	}

	private void initFont() {
		for(int i = 0x050 ; i <= 0x09f; i++) { // 80 to 159 , 80 
			ram[i] = (byte)fontFace[i- 0x050];
		}
	}

	public void loadROM() {
		File file = new File("./ROM/IBM.ch8");
				
		try {
			gameBytes = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		for (int i = 0; i < gameBytes.length; i++) {
//			System.out.println(gameBytes[i]);
//		}
//		System.out.println("Len" + " "+ gameBytes.length);
		
		for(int i = 0x200 ; i < (gameBytes.length + 0x200); i++) {
			ram[i] = gameBytes[i-0x200];
		}
		
	}

	public void fetch() {
		code = ( (ram[PC]<<8) | ram[PC+1]); // grabs code 2 bytes at a time
		PC+=2;
		
		
		//System.out.println(Integer.toHexString(15));
	}
	
	
	private void draw(int x, int y, int n) {
		
	    for (int row = 0; row < n; row++) {
	        byte spriteByte = ram[I + row]; // Get sprite data from memory

	        for (int col = 0; col < 8; col++) {
	            if ((spriteByte & (0x80 >> col)) != 0) {
	                int screenX = (x + col) % 64;
	                int screenY = (y + row) % 32;

	                int index = screenY * 64 + screenX;


	                display.drawPixel(index);
	            }
	        }
	    }

	}
	
	public void decode() {
		
		
		int firstChunk = (code & 0xF000) >>> 12; // returns a 4 bit code 
		//System.out.println(Integer.toHexString(firstChunk));
		int x = (code & 0x0F00)>>>8;
		int y = (code & 0x00F0)>>>4;
		int n = (code & 0x000F);
		int nn = (code & 0x00FF);
		int nnn = (code & 0x0FFF);
		
		switch(firstChunk) {
		
			case 0x0:
				if(nnn == 0x00E0) {
					display.clear();
				}
				if(nnn == 0x00EE) {
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
				if(reg[x]==nn) {
					PC+=2;
				}
				break;
			case 0x4:
				if(reg[x]!=nn) {
					PC+=2;
				}
				break;
			case 0x5:
				break;
			case 0x6:
				reg[x]=nn;
				break;
			case 0x7:
				reg[x]+=nn;
				break;
			case 0x8:
				if(n==0) {
					reg[x] = reg[y];
				}
				if(n==1) {
					reg[x] = (reg[x]|reg[y]);
				}
				if(n==2) {
					reg[x] = (reg[x]&reg[y]);
				}
				if(n==3) {
					reg[x] = (reg[x]^reg[y]);
				}
				if(n==4) {
					reg[x] =(reg[x]+reg[y]);
				}
				if(n==5) {
					reg[x] = reg[x] - reg[y];
				}
				if(n==6) {
					reg[x] = reg[y]>>1;
				}
				
				if(n==7) {
					reg[x] = reg[y] - reg[x];
				}
				
				if(n==0xE) {
					reg[x] = reg[y]<<1;
				}
				
				break;
			case 0x9:
				break;
			case 0xa:
				I = nnn;
				break;
			case 0xb:
				PC = nnn+reg[0];
				break;
			case 0xc:
				reg[x] = random.nextInt(0XFF) & nn;
				break;
			case 0xd:
				draw(reg[x] , reg[y] , n);
				
				break;
			case 0xE: //EX9E is where im leaving it for tonight
				break;
			case 0xF:
				break;
			
			default:
				System.out.println("Someone has gotten lazy and forgot to implement" + Integer.toHexString(code));
				
		}
	}
	
	
}
