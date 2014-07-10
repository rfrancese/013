package it.unisa.grogchallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class TappableAnimazioneScena extends AnimazioneScena {

	public TappableAnimazioneScena(Context context, int x, int y, Bitmap[] img) {
		super(context, x, y, img);
		// TODO Auto-generated constructor stub
	}

	private long startTamp;
	private boolean tapped;
	
	
	public void tap(){
		setFrame(1);
		startTamp = System.currentTimeMillis();
		tapped = true;
	
	}
	
	@Override
	public void doDraw(Canvas c){
		if(tapped){
			if(System.currentTimeMillis()- startTamp>200){
				setFrame(0);
				tapped=false;
			}
		}
		Paint p = new Paint();
		p.setDither(true);
		p.setAntiAlias(true);
		Bitmap[] img = this.getImg();
		c.drawBitmap(img[getFrame()],this.getX(),this.getY(),p);
		//c.drawBitmap(this.img[frame],null,this.getShape(),null);
		
		
	}
	
	
	
}
