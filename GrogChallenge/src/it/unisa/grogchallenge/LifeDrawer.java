package it.unisa.grogchallenge;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class LifeDrawer {
		
	private Bitmap life;
	private int x;
	private int y;
	
	
	public LifeDrawer(int x, int y,Bitmap life){
		this.life=life;
		
		this.x=x;
		this.y=y;
	}
	
	//disegna le vite
	public void doDraw(Canvas c,int numLife){
		
		
		
		for(int i = 0;i< numLife;i++){
			
			
			int w = this.life.getWidth();
			int h = this.life.getHeight();
			
			int spazio = w/4;
			
			c.drawBitmap(life, x - ((w+spazio)*i),y,null);
			
			
		}
		
	}
	
	
	
	
}
