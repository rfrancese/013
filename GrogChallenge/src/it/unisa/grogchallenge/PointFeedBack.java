package it.unisa.grogchallenge;

import android.content.Context;
import android.graphics.Bitmap;

public class PointFeedBack extends AnimazioneScena {
	private long time;
	public PointFeedBack(Context context, int x, int y, Bitmap[] img) {
		super(context, x, y, img);
		// TODO Auto-generated constructor stub
		time = System.currentTimeMillis();
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	
	
	
}
