package it.unisa.grogchallenge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import com.google.android.gms.internal.fo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;


public class LivelloBancone{
	
	private int resto=-1;
	private Bitmap tile;
	
	private Bitmap[] boccaleVuoto;
	private Bitmap[] esplosioneBoccale;
	private Bitmap[] esplosione;

	private Bitmap[] punteggiDraw; //0 = 10, 1 = -10 , 2 = 20, 3 = -20
	
	private Bitmap fumetto;
	private int numLivelloBancone;
	private Bitmap[] proiettile;
	private Bitmap[] nemici;
	private Context ctx;
	private Bitmap[] boccale;
	private Bitmap[] boccaleRotto;
	private Bitmap[] doblone;
	
	private ArrayList<Explosion> dobloni;
	private ArrayList<PointFeedBack> punteggi;
	private ArrayList<Explosion> esplosioniBoccali;
	private ArrayList<Explosion> esplosioni;
	private ArrayList<Explosion> boccaliRotti;
	private ArrayList<Enemy> enemy;
	private ArrayList<AnimazioneScena> boccaliVuoti;
	private ArrayList<AnimazioneScena> boccaliPieni;
	private ArrayList<AnimazioneScena> proiettili;
	private TappableAnimazioneScena barile;
	private AnimazioneScena bancone;
	private int x;
	private int y;
	private AnimazioneScena porta;
	private int screenWidth;
	private Point spawnEnemy;
	private Point spawnBoccali;
	private Player player;
	private SessionGame game;
	private SoundManager sound;
	

	public Bitmap[] getDoblone() {
		return doblone;
	}


	public void setDoblone(Bitmap[] doblone) {
		this.doblone = doblone;
	}


	public ArrayList<Explosion> getDobloni() {
		return dobloni;
	}


	public void setDobloni(ArrayList<Explosion> dobloni) {
		this.dobloni = dobloni;
	}


	
	
	public Bitmap getTile() {
		return tile;
	}


	public void setTile(Bitmap tile) {
		this.tile = tile;
	}
	
	

	public Bitmap[] getBoccaleVuoto() {
		return boccaleVuoto;
	}


	public void setBoccaleVuoto(Bitmap[] boccaleVuoto) {
		this.boccaleVuoto = boccaleVuoto;
	}



	
	
	
	public LivelloBancone(Context ctx,int x, int y, int screenWidth,TappableAnimazioneScena barile, AnimazioneScena bancone,Player p,
			 AnimazioneScena porta,SessionGame g,SoundManager s) {
		this.game=g;
		this.ctx = ctx;
		this.barile = barile;
		this.bancone = bancone;
		this.x = x;
		this.y = y;
		this.porta = porta;
		this.screenWidth = screenWidth;
		this.player=p;
		sound = s;
		this.barile.setX(x + screenWidth - (this.barile.getWidth()));
		this.barile.setY(y - this.barile.getHeight());
		
		
		int spazio = this.barile.getX()-player.getWidth();//-this.barile.getWidth();
		//if(this.bancone.getWidth() < spazio){
			 resto = spazio - this.bancone.getWidth();
			this.bancone.setX(resto);
		//}else{
		//	this.bancone.setX(x);
		//}
			this.bancone.setY(y - this.bancone.getHeight());
			
		
		porta.setX(x);
		porta.setY(y-(this.porta.getHeight()));
	
		
		this.spawnEnemy = new Point(this.porta.getX() + 5,this.bancone.getY()- this.bancone.getHeight() + 10);
		this.spawnBoccali = new Point(this.bancone.getX() + this.bancone.getWidth(), y - bancone.getHeight()
				- (this.bancone.getHeight()/2));
		
		this.boccaliPieni = new ArrayList<AnimazioneScena>();
		this.boccaliVuoti = new ArrayList<AnimazioneScena>();
		this.enemy = new ArrayList<Enemy>();
		this.proiettili = new ArrayList<AnimazioneScena>();
		this.esplosioni = new ArrayList<Explosion>();
		this.esplosioniBoccali = new ArrayList<Explosion>();
		this.boccaliRotti = new ArrayList<Explosion>();
		this.punteggi = new ArrayList<PointFeedBack>();
		this.dobloni= new ArrayList<Explosion>();
	}
	
	public void removePoint(int j){
		this.punteggi.remove(j);
	}
	
	public void addPoint(int point,int x,int y){
		PointFeedBack p = new PointFeedBack(ctx, x, y, this.punteggiDraw);
		if(point == 10){
			
		}else if(point == 20){
			p.stepFrame();
			p.stepFrame();
			
		}else if(point == -10){
			p.stepFrame();
		
		}else if(point == -20){
			p.stepFrame();
			p.stepFrame();
			p.stepFrame();
				
		}else if(point == 50){
			p.stepFrame();
			p.stepFrame();
			p.stepFrame();
			p.stepFrame();
		}
		punteggi.add(p);
	}

	public ArrayList<PointFeedBack> getPunteggi() {
		return punteggi;
	}


	public void setPunteggi(ArrayList<PointFeedBack> punteggi) {
		this.punteggi = punteggi;
	}


	public void setBoccale(Bitmap[] boccale) {
		this.boccale = boccale;
	}
	

	public void setPunteggiBitmap(Bitmap[] point){
		this.punteggiDraw=point;
	}

	public ArrayList<AnimazioneScena> getBoccaliVuoti() {
		return boccaliVuoti;
	}


	public void setBoccaliVuoti(ArrayList<AnimazioneScena> boccaliVuoti) {
		this.boccaliVuoti = boccaliVuoti;
	}


	public ArrayList<AnimazioneScena> getBoccaliPieni() {
		return boccaliPieni;
	}

	
	public void addDoblone(int x){
		this.dobloni.add(new Explosion(ctx, x, this.bancone.getY()-doblone[0].getHeight()/2, this.doblone,10000));
	}
	
	public void setBoccaliPieni(ArrayList<AnimazioneScena> boccaliPieni) {
		this.boccaliPieni = boccaliPieni;
	}

	
	

	public void setBoccaleRotto(Bitmap[] boccaleRotto) {
		this.boccaleRotto = boccaleRotto;
	}


	public void addEnemy(){
		Bitmap[] pg = new Bitmap[3];
		int numBimap = this.nemici.length/3;
		int min = 0;
		int max = numBimap - 1;
		int r;
		if(max == 0){
		 r = 0;
		}else{
		Random rn = new Random();
		 r = rn.nextInt(max - min + 1) + min;
		}
		int partenza = r * 3;
		for(int i =0; i < 3; i++){
			pg[i] = this.nemici[partenza + i];
			
		}
		
		
		Random rn = new Random();
		 min = 1;
		 max = 2;
		r = rn.nextInt(max - min + 1) + min;
		boolean nervoso = false;
		 if(r == 1 )nervoso = true;
		 
		 
		this.enemy.add(new Enemy(ctx, this.spawnEnemy.x,this.spawnEnemy.y,pg,  this,nervoso,this.game,sound));
		
		
		
	}
	
	public TappableAnimazioneScena getBarile(){
		return this.barile;
	}
	
	public AnimazioneScena getBancone(){
		return this.bancone;
	}
	
	public void removeDoblone(int j){
		this.dobloni.remove(j);
	}
	
	public void removeEnemy(int j){
		this.enemy.remove(j);
	}
public void addBoccalePieno(int x, int y){
		
		AnimazioneScena boccale = new AnimazioneScena(this.ctx, x,y,
												this.boccale);
		this.boccaliPieni.add(boccale);
	}
	public void addBoccalePieno(){
		
		AnimazioneScena boccale = new AnimazioneScena(this.ctx, this.spawnBoccali.x, this.spawnBoccali.y,
												this.boccale);
		this.boccaliPieni.add(boccale);
	}
	
	public void removeBoccalePieno(int j){
		this.boccaliPieni.remove(j);
	}
	
	public void addBoccaleVuoto(int x,int y){
		//BUG FIX
		//dimezzo l'altezza della bottiglia vuota altrimenti collide sempre con il player
		AnimazioneScena b = new AnimazioneScena(ctx, x, y, boccaleVuoto);
			
		this.boccaliVuoti.add(b);
		
	}
	
	public void removeBoccaleVuoto(int j){
		this.boccaliVuoti.remove(j);
	}
	

	public int getNumLivelloBancone() {
		return numLivelloBancone;
	}


	public void setNumLivelloBancone(int numLivelloBancone) {
		this.numLivelloBancone = numLivelloBancone;
	}

	
	
	
	public void doDraw(Canvas c){
		this.barile.doDraw(c);
		
		//disegno i nemici
		for(int i =0;i< this.enemy.size();i++){
			Enemy en = enemy.get(i);
			if(en.isFlash()){
				if(en.isFlashActive()){
					en.doDraw(c);
				}
				en.flashAnimation();
			}else{
				if(en.isSparando()){
					c.drawBitmap(fumetto,en.getX() + en.getWidth(),en.getY(), null);
				}
				en.doDraw(c);
			}
		}
		
		
		this.bancone.doDraw(c);
		
		
		
		if(resto != -1){
			int i = 0;
			int m = 0;
			while((m = (i*tile.getWidth())) <= resto){
				
				c.drawBitmap(this.tile, m,bancone.getY(), null);
				
				i++;
			}
			
		}
		if(player.getNumLivelloBancone()==this.numLivelloBancone){
			player.doDraw(c);
		}
		
		//disegno i boccali pieni
		for(int i=0;i< this.boccaliPieni.size();i++){
			
			this.boccaliPieni.get(i).doDraw(c);
			
		}
		
		//disegno i boccali vuoti
		for(int i=0;i< this.boccaliVuoti.size();i++){
			
			this.boccaliVuoti.get(i).doDraw(c);
			
		}
		
		//disegno i proiettili
		for(int i=0;i< this.proiettili.size();i++){
			
			this.proiettili.get(i).doDraw(c);
			
		}
		//disegno i boccalirotti
		for (int i = 0; i < this.boccaliRotti.size(); i++) {
			
			if(boccaliRotti.get(i).isActive()){
				this.boccaliRotti.get(i).doDraw(c);
				}else{
					boccaliRotti.remove(i);
				}
			
		}
		
		//disegno le esplosioni
		for(int i=0;i< this.esplosioni.size();i++){
			if(esplosioni.get(i).isActive()){
			this.esplosioni.get(i).doDraw(c);
			}else{
				esplosioni.remove(i);
			}
		}
		
		//disegno le esplosioniBoccali
		for(int i=0;i< this.esplosioniBoccali.size();i++){
			if(esplosioniBoccali.get(i).isActive()){
			this.esplosioniBoccali.get(i).doDraw(c);
			}else{
				esplosioniBoccali.remove(i);
			}
		}
		//disegno i punteggi volanti
		for(int i=0;i< this.punteggi.size();i++){
			punteggi.get(i).doDraw(c);
		}
		
		//disegno i dobloni
		for (int i = 0; i < this.dobloni.size(); i++) {
			if(dobloni.get(i).isFlash()){
				if(dobloni.get(i).isFlashActive()){
				dobloni.get(i).doDraw(c);
				}
				dobloni.get(i).flashAnimation();
			}else{
				dobloni.get(i).doDraw(c);
			}
		}
		
		this.porta.doDraw(c);
	}
	
	public AnimazioneScena getPorta() {
		return porta;
	}


	public void setPorta(AnimazioneScena porta) {
		this.porta = porta;
	}


	
	
	public void addProiettile(int x, int y){
		this.proiettili.add(new AnimazioneScena(ctx,x, this.spawnBoccali.y, this.proiettile));
	}
	
	public void removeProiettile(int j){
		this.proiettili.remove(j);
	}


	public Bitmap[] getNemici() {
		return nemici;
	}


	public void setNemici(Bitmap[] nemici) {
		this.nemici = nemici;
	}


	public ArrayList<Enemy> getEnemy() {
		return enemy;
	}


	public void setEnemy(ArrayList<Enemy> enemy) {
		this.enemy = enemy;
	}





	public Bitmap[] getProiettile() {
		return proiettile;
	}





	public void setProiettile(Bitmap[] proiettile) {
		this.proiettile = proiettile;
	}





	public ArrayList<AnimazioneScena> getProiettili() {
		return proiettili;
	}





	public void setProiettili(ArrayList<AnimazioneScena> proiettili) {
		this.proiettili = proiettili;
	}
	
	

	public Bitmap getFumetto() {
		return fumetto;
	}





	public void setFumetto(Bitmap fumetto) {
		this.fumetto = fumetto;
	}





	public ArrayList<Explosion> getEsplosioniBoccali() {
		return esplosioniBoccali;
	}





	public void setEsplosioniBoccali(ArrayList<Explosion> esplosioniBoccali) {
		this.esplosioniBoccali = esplosioniBoccali;
	}





	public ArrayList<Explosion> getEsplosioni() {
		return esplosioni;
	}





	public void setEsplosioni(ArrayList<Explosion> esplosioni) {
		this.esplosioni = esplosioni;
	}





	public Bitmap[] getEsplosioneBoccale() {
		return esplosioneBoccale;
	}





	public void setEsplosioneBoccale(Bitmap[] esplosioneBoccale) {
		this.esplosioneBoccale = esplosioneBoccale;
	}





	public Bitmap[] getEsplosione() {
		return esplosione;
	}





	public void setEsplosione(Bitmap[] esplosione) {
		this.esplosione = esplosione;
	}

		
	public void addEsplosione(int x,int y){
		this.esplosioni.add(new Explosion(ctx, x, y, esplosione));
		
	}
	
	
	public void addBoccaleRotto(int x,int y){
		this.boccaliRotti.add(new Explosion(ctx, x, y, boccaleRotto));
	}
	
	public void removeEsplosione(int j){
		this.esplosioni.remove(j);
	}
	
	
	public void addEsplosioneBoccale(int x, int y){
		this.esplosioniBoccali.add(new Explosion(ctx, x, y, esplosioneBoccale));
	}
	
	public void removeEsplosioneBoccale(int j){
		this.esplosioniBoccali.remove(j);
	}





	public Point getSpawnEnemy() {
		return spawnEnemy;
	}





	public void setSpawnEnemy(Point spawnEnemy) {
		this.spawnEnemy = spawnEnemy;
	}





	public Point getSpawnBoccali() {
		return spawnBoccali;
	}





	public void setSpawnBoccali(Point spawnBoccali) {
		this.spawnBoccali = spawnBoccali;
	}





	public Bitmap[] getBoccaleRotto() {
		return boccaleRotto;
	}
	
	
	public void init(){
		this.boccaliPieni = new ArrayList<AnimazioneScena>();
		this.boccaliVuoti = new ArrayList<AnimazioneScena>();
		this.enemy = new ArrayList<Enemy>();
		this.proiettili = new ArrayList<AnimazioneScena>();
		this.esplosioni = new ArrayList<Explosion>();
		this.esplosioniBoccali = new ArrayList<Explosion>();
		this.punteggi= new ArrayList<PointFeedBack>();
		this.dobloni = new ArrayList<Explosion>();
	}
	
}
