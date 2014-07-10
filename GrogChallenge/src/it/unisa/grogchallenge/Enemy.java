package it.unisa.grogchallenge;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;


public class Enemy extends AnimazioneScena {
	private boolean sparando=false;
	private boolean bevendo=false;
	private int rage;
	private long timeSpawn;
	private long tempoBevuta;
	private long inizioBevuta;
	private long inizioSparo;
	private LivelloBancone livelloBancone;
	private int ultimatum =0;
	private boolean nervoso;
	private SessionGame game;
	private int asseY;
	
	private SoundManager sound;
	
	private boolean firstNervoso=true;
	
	
	private boolean avanza = true;
	private long passo;
	private long fermo;
	
	
	private boolean drawUp;
	private int offset = -5;
	private long timeDrawUp;
	
	
	public boolean isDrawUp() {
		return drawUp;
	}

	public void setDrawUp(boolean drawUp) {
		this.drawUp = drawUp;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isAvanza() {
		return avanza;
	}

	public void setAvanza(boolean avanza) {
		this.avanza = avanza;
	}

	public Enemy(Context context, int x, int y, Bitmap[] img,LivelloBancone lb,boolean nervoso,SessionGame g,SoundManager s) {
		super(context, x, y, img);
		rage = 0;
		this.asseY = y;
		timeSpawn = System.currentTimeMillis();
		this.livelloBancone = lb;
		this.nervoso=nervoso;
		this.game= g;
		passo = timeSpawn;
		this.sound=s;
	}

	public void move(int speed){
		
		if(!sparando){
			
				if(!bevendo ){
						long t = System.currentTimeMillis();
						if(nervoso && !(game.isTutorialState())){
							
							
							if(t-timeSpawn > 3000){
								this.rage++;
								
								int max = 20;
								int min = 1;
								
								Random rn = new Random();
								int r = rn.nextInt(max - min + 1) + min;
								
								
								if(r <= rage){
									ultimatum++;
									
									if(ultimatum >= 2){//il nemico spara
										ultimatum = 0;
										rage = 0;
										this.spara();
									}
								}
							}
							
						}
						
						avanza(speed);
						
				}else{
					//sta bevendo ancora
					long t = System.currentTimeMillis();
					
						if(t-inizioBevuta > tempoBevuta){
							//ha finito di bere
							Point p = this.livelloBancone.getSpawnBoccali();
							this.livelloBancone.addBoccaleVuoto(this.getX(), p.y);
							this.bevendo= false;
							this.setFrame(0);
						}
					
					
					int x = this.getX();
					this.setX(x - speed*4);
				}
			
		}else{
			if(firstNervoso){
				//prima volta che si innervosisce
				sound.playArr();
				firstNervoso=false;
			}
			long t = System.currentTimeMillis();
			if(t - this.inizioSparo > 2000){
			//devo far partire il proiettile;
				
			this.livelloBancone.addProiettile(this.getShape().centerX(), this.getShape().bottom);
			sparando = false;
			//this.setFrame(0);
			this.nervoso=false;
			}
			
			avanza(speed);
		}
		
		
		
	}
	
	
	
	
	

	public void avanza(int speed){
		long t = System.currentTimeMillis();
		
		if(avanza){
			
			int x = this.getX();
			this.setX(x + speed);
			if(this.getY() != this.asseY)this.setY(asseY);
			if(t-passo >= 1000){
				avanza = false;
				fermo = System.currentTimeMillis();
				timeDrawUp=fermo;
			}
		}else{
			if(t-fermo >= 500){
				avanza = true;
				passo=System.currentTimeMillis();
			}
		}
	
		if(!avanza){
			if(drawUp){
				if(t-timeDrawUp>150){	
					setY(this.getY() + offset);
					drawUp = false;
					timeDrawUp=System.currentTimeMillis();
				}
			}else{
				if(t-timeDrawUp>150){	
					setY(this.getY() - offset);
					drawUp = true;
					timeDrawUp=System.currentTimeMillis();
				}
			}
		}	
		
	}
	
	
	public void bevi(){
		if(sparando){
		this.sparando=false;
		this.setFrame(0);
		nervoso = false;
		}
		if(!bevendo){
		int max = 5;
		int min = 2;
		
		Random rn = new Random();
		int r = rn.nextInt(max - min + 1) + min;
		if(game.isTutorialState()){
			r = 100;
		}
		this.tempoBevuta = r * 1000;
		this.inizioBevuta = System.currentTimeMillis();
		this.bevendo=true;
		this.setFrame(1);//il secondo frame rappresenta il nemico che beve (convenzione mia :) )
			if(!game.isTutorialState()){
			//random per lo spawn di dobloni
			max = 4;
			min = 1;
			
			
			r = rn.nextInt(max - min + 1) + min;
			Log.d("RANDOM DOBLONI", "" + r);
			if(r==1){
				livelloBancone.addDoblone(this.getX());
			}
			}
		}
		
	}
	
	
	public void spara(){
		if(!bevendo){
			this.sparando = true;
			//this.setFrame(2); //il terzo frame è il frame che rappresenta il nemico che spare 
			this.inizioSparo = System.currentTimeMillis();
			
		}
		
	}

	public boolean isSparando() {
		return sparando;
	}

	public void setSparando(boolean sparando) {
		this.sparando = sparando;
	}

	public boolean isBevendo() {
		return bevendo;
	}

	public void setBevendo(boolean bevendo) {
		this.bevendo = bevendo;
	}

	public int getRage() {
		return rage;
	}

	public void setRage(int rage) {
		this.rage = rage;
	}
	
	
	
	
}
