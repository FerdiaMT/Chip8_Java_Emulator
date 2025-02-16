package ferdiaMT;

public class DelayTimer implements Runnable {

	private boolean running = false;
	private int fps;
	private Chip8 chip8;
	public int frame=60;
	
	public DelayTimer(Chip8 chip8) {
		this.chip8 = chip8;
	}
	
	public void setFrame(int x) {
		frame = x;
	}
	
	
	public void run() {
		
		long lastTime = System.nanoTime();
		long lastTime2 = lastTime;
		long currentTime = lastTime;
		double nsPerSecond = 1000000000/60;
		double tickPerSecond = 1000000000/500;
		double threshold = 0;
		double debugTime = System.currentTimeMillis();
		double thresholdTick = 0.0d;
		
		while(running) {
			
			currentTime = System.nanoTime();
			threshold = (currentTime-lastTime)/nsPerSecond;
			thresholdTick = (currentTime-lastTime2)/tickPerSecond;
			
			
			
			if(threshold >=1) {
				
				lastTime = currentTime;
				threshold--;
				
				fps++;
				if(frame>=1) {
					frame--;
				}
			}
			
			if(thresholdTick >=1) {
				lastTime2 = currentTime;
				threshold--;
				tick();
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
