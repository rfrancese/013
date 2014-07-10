package it.unisa.grogchallenge;

import android.content.Context;
import android.graphics.Bitmap;

public class Explosion extends AnimazioneScena{
	private long initExplosion;
	private long interval = 500;
	public long getInitExplosion() {
		return initExplosion;
	}
	public void setInitExplosion(long initExplosion) {
		this.initExplosion = initExplosion;
	}
	public Explosion(Context context, int x, int y, Bitmap[] img) {
		super(context, x, y, img);
		this.initExplosion= System.currentTimeMillis();
		
	}
	public Explosion(Context context, int x, int y, Bitmap[] img,long time) {
		super(context, x, y, img);
		this.initExplosion= System.currentTimeMillis();
		this.interval = time;
		
	}
	
	
	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public boolean isActive(){
		long t = System.currentTimeMillis();
		if(t-initExplosion > interval){
			return false;
		}
		else return true;
	}
	
}
