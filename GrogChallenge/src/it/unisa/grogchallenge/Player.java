package it.unisa.grogchallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

public class Player extends AnimazioneScena {
	
	private int numLivelloBancone;
	private LivelloBancone[] livelloBancone;
	public int asse;
	
	
	private long runningTime;
	private int frameRun =3;
	
	private boolean run = false;
	private boolean impegnato = false;
	
	private boolean firstRun=false;
	
	public Player(Context context, int x, int y, Bitmap[] img) {
		super(context, x, y, img);
		// TODO Auto-generated constructor stub
		//this.asse = x - img[0].getWidth() - img[0].getWidth()/2;
		
		this.setX(x);
		this.setY(y);
	}
	
	

	public int getAsse() {
		return asse;
	}



	public void setAsse(int asse) {
		this.asse = asse;
	}



	public LivelloBancone[] getLivelloBancone() {
		return livelloBancone;
	}



	public void setLivelloBancone(LivelloBancone[] livelloBancone) {
		this.livelloBancone = livelloBancone;
	}



	public int getNumLivelloBancone() {
		return numLivelloBancone;
	}



	public void setNumLivelloBancone(int numLivelloBancone) {
		this.numLivelloBancone = numLivelloBancone;
	}



	public void moveTo(int y){
		if(!run){
		if(!impegnato)this.setY(y);
		}
	}
	
	public void running(){
		if(!run && !impegnato){
			run = true;
			runningTime = System.currentTimeMillis();
		}
		
	}
	
	public void stopRunning(){
		run=false;
		frame=0;
		firstRun=true;
		
	}
	
	public boolean isRunning(){
		if(run){
			changeAnimationRunning();
		return run;
		}
		return false;
	}
	
	
	public void changeAnimationRunning(){
		long t = System.currentTimeMillis();
		if(firstRun){
			stepFrameRun();
			firstRun=false;
		}
		if(t-runningTime >= 300){
			runningTime = System.currentTimeMillis();
			stepFrameRun();
		}
	}
	
	private void stepFrameRun(){
		//i frame 3 e 4 sono i frame che alternano la corsa
		frame=frameRun;
		if(frameRun==3){
			frameRun=4;
		}else{
			frameRun=3;
		}
	}
	
	public void versaGrog(){
		this.impegnato=true;
		long t1 = System.currentTimeMillis();
		stepFrame();
		long t2;
		while(((t2 = System.currentTimeMillis()) - t1) < 150){
			
		}
		stepFrame();
		t1 = System.currentTimeMillis();
		while(((t2 = System.currentTimeMillis()) - t1) < 150){
			
		}
		this.livelloBancone[this.numLivelloBancone].addBoccalePieno();
		frame=0;
		impegnato = false;
	}
	
	
}
