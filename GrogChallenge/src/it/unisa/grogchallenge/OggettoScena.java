package it.unisa.grogchallenge;

import java.io.Serializable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public abstract class OggettoScena{
	
	
	private int x;
	private int y;
	private Context context;
	private int width;
	private int height;
	private Rect shape;
	
	

	public OggettoScena(Context context,int x, int y){
		this.shape = new Rect();
		this.context=context;
		this.x=x;
		this.y=y;
	}
	

	public OggettoScena(Context context,int x, int y,int width, int height){
		
		this.context=context;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		shape = new Rect(x, y, x + width, y + height);
		
	}
	
public OggettoScena(Context context,int x, int y,Rect shape){
		
		this.context=context;
		this.x=x;
		this.y=y;
		this.shape=shape;
		this.width=shape.width();
		this.height=shape.height();
	}
	
	public abstract void move();	
	
	public abstract void doDraw(Canvas c);

	
	
	
	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
		this.shape.set(x, y, x + width, y + height);
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
		this.shape.set(x, y, x + width, y + height);
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		this.shape.set(x, y, x + width, y + height);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		this.shape.set(x, y, x + width, y + height);
	}

	
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Rect getShape() {
		return shape;
	}


	public void setShape(Rect shape) {
		this.shape = shape;
	}

}
