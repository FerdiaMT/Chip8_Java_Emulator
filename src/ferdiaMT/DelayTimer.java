package ferdiaMT;

public class DelayTimer implements Runnable {

	private boolean running = false;
	private int fps;
	private Chip8 chip8;
	public int frame=60;
	
	public DelayTimer(Chip8 chip8) {
		this.chip8 = chip8;
	}
	
	
	public void run() {
		
		long lastTime = System.nanoTime();
		long currentTime = lastTime;
		double nsPerSecond = 1000000000/60;
		double threshold = 0;
		double debugTime = System.currentTimeMillis();
		
		while(running) {
			currentTime = System.nanoTime();
			threshold = (currentTime-lastTime)/nsPerSecond;
			

			if(threshold >=1) {
				lastTime = currentTime;
				threshold--;
				tick();
				fps++;
				frame--;
				if(frame<0) {
					frame=60;
				}
			}
			
			if(System.currentTimeMillis()-debugTime >= 1000) {
				debugTime = System.currentTimeMillis();
				//System.out.println(fps);
				fps = 0;
			}
			
		}
		
	}

	private void tick() {
		chip8.tick();
	}

	public void start() {
		running = true;
		Thread thread = new Thread(this);
		thread.start();
	}


	
}
