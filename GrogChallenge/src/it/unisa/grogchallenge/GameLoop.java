package it.unisa.grogchallenge;

public abstract class GameLoop extends Thread {
		//stati della view
		private boolean pause;
		private boolean running;
	
	public GameLoop(){
		this.running=true;
		this.pause= false;
		
	}
	

	public void run(){
		while(running){
			if(!pause){
					update();
			}
		}
	}
	
	public abstract void update();
	public abstract void init();
	
	public void finish(){
		this.running=false;
	}
	
	public void pauseLoop(){
		pause=true;
	}
	
	public void resumeLoop(){
		this.pause=false;
	}
	
	public void setRunning(boolean running){
		this.running=running;
	}
	
}
