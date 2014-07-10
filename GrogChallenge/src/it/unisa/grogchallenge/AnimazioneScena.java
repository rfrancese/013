package it.unisa.grogchallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class AnimazioneScena extends OggettoScena {

	Bitmap img[];
	 int frame;
	 
	 
	public int getFrame() {
		return frame;
	}


	public void setFrame(int frame) {
		this.frame = frame;
	}


	public AnimazioneScena(Context context, int x, int y,Bitmap[] img) {
		super(context, x, y);
		// TODO Auto-generated constructor stub
		this.img=img;
		
		int h = img[0].getHeight();
		int w = img[0].getWidth();
		this.setWidth(w);
		this.setHeight(h);
		Rect shape = new Rect(x,y,x+w,y+h);
		this.setShape(shape);
		
		frame=0;

	}


	
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	
	
	public Bitmap[] getImg() {
		return img;
	}


	public void setImg(Bitmap[] img) {
		this.img = img;
	}
	
	public void stepFrame(){
		if((frame + 1)==this.img.length){
			frame=0;
		}else{
			frame++;
		}
	}

	@Override
	public void doDraw(Canvas c) {
		// TODO Auto-generated method stub

		Paint p = new Paint();
		p.setDither(true);
		p.setAntiAlias(true);
		c.drawBitmap(this.img[frame],this.getX(),this.getY(),p);
		//c.drawBitmap(this.img[frame],null,this.getShape(),null);
		
		
	}
	
	private boolean flash= false;
	private long timeFlash;
	private boolean flashActive;
	void startFlash(){
		flash = true;
		timeFlash = System.currentTimeMillis();
		flashActive=true;
	}
	
	public void flashAnimation(){
		if(flash){
			if(System.currentTimeMillis() - timeFlash > 300){
				timeFlash = System.currentTimeMillis();
				if(flashActive){
					flashActive=false;
				}else{
					flashActive=true;
				}
			}
		}
	}
	
	public boolean isFlash() {
		return flash;
	}

	public void setFlash(boolean flash) {
		this.flash = flash;
	}

	public boolean isFlashActive() {
		return flashActive;
	}

	public void setFlashActive(boolean flashActive) {
		this.flashActive = flashActive;
	}
	
}
