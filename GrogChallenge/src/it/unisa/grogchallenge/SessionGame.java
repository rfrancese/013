package it.unisa.grogchallenge;



import java.util.ArrayList;
import java.util.Locale;

import java.util.HashMap;

import java.util.Random;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;



import org.json.JSONException;



import com.google.android.gms.internal.di;





import android.app.Activity;

import android.app.AlertDialog;

import android.app.Dialog;

import android.app.ProgressDialog;

import android.content.Context;

import android.content.DialogInterface;

import android.content.DialogInterface.OnDismissListener;

import android.content.Intent;

import android.content.res.Resources;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.Point;

import android.graphics.Rect;

import android.graphics.Typeface;

import android.media.AudioManager;

import android.media.MediaPlayer;

import android.media.MediaPlayer.OnCompletionListener;

import android.media.SoundPool;



import android.os.Handler;

import android.os.Looper;

import android.os.Message;

import android.support.v4.util.LruCache;

import android.support.v4.view.MotionEventCompat;

import android.util.AttributeSet;

import android.util.DisplayMetrics;

import android.util.Log;

import android.view.KeyEvent;

import android.view.LayoutInflater;

import android.view.MotionEvent;

import android.view.Surface;

import android.view.SurfaceHolder;

import android.view.SurfaceView;

import android.view.View;

import android.view.Window;

import android.view.WindowManager;

import android.view.View.OnClickListener;

import android.view.animation.DecelerateInterpolator;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;

import android.widget.MediaController.MediaPlayerControl;

import android.widget.TextView;

import android.widget.Toast;



public class SessionGame extends SurfaceView implements SurfaceHolder.Callback, DialogInterface.OnDismissListener{

	

/*	private LruCache<String, Bitmap> mMemoryCache;

	

	 // Get max available VM memory, exceeding this amount will throw an

    // OutOfMemory exception. Stored in kilobytes as LruCache takes an

    // int in its constructor.

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);



    // Use 1/8th of the available memory for this memory cache.

    final int cacheSize = maxMemory / 8;



	



	

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {

        if (getBitmapFromMemCache(key) == null) {

            mMemoryCache.put(key, bitmap);

        }

    }



    public Bitmap getBitmapFromMemCache(String key) {

        return mMemoryCache.get(key);

    }*/

    

   

//////////////////////////////////////////////////////////////////////////////

	private GameActivity activity;

	

		private String email;

		private ConnectJsonUtil server;

	

		

	  //DB

    private DBGrogChallenge db;

    public void setDb(DBGrogChallenge db){

    	this.db=db;

    }

    public DBGrogChallenge getDb(){

    	return this.db;

    }

    



  public GameActivity getActivity() {

		return activity;

	}



	public void setActivity(GameActivity activity) {

		this.activity = activity;//

	

	}


	//variabili per il tutorial
	private long timeTutorial;
  	private boolean tutorialState = false;
  	private boolean tapBarile = false;
  	private boolean lancioBottiglia =false;
  	private boolean prendiBottiglia = false;
  	private static final int NUM_BANCONE_TUTORIAL=3;
  	private int stepTutorial = 0;
  	private long timeStep;	
  	
	//stati della view
  	public static final int POWER_OFF = 900;
  	
  	public static final int TUTORIAL = 800;
  	
	public static final int CLASSIFICA = 700;

  	public static final int GAMEOVER = 600;

    public static final int RUN = 500;

  	public static final int PAUSE = 400;

  	public static final int SPLASH=100;

	public static final int MENU=200;

	public static final int LOADED = 300;

  	private int state;

  	private int lastState;

  	

  	private Point spawnPlayer;

  		

  	private boolean running = true;

  	private boolean caricato = false;


  	
  

  	//time

  	long animTime;

  	

  	

  	private int speedBoccaleVuoto;

	private  int speedBullet;

  	private  int enemySpeed;

  	private  int speed;	

  	

  	/*

  	private  int speedBullet = velocity;

  	private  int enemySpeed = 2;

  	private  int speed = speedBullet;	

 

  	*/

  	

  	private static final int FINALTIME=60;

  	private long timeMemory;

  	private int time = FINALTIME;
  	
  	private long timeGameOver;
  	
  	
  	private long spawnTime;
  	
  	private  final int FPS=1000/20; // frame x second

  	private  int punteggio = 0;

  	private  int record = 0;

  	private  int life = 1;

  	private int punteggioGameOver=0;

  	



	private String nickName;

  

	

	

	

  //Bitmap e Drawable

  	private Bitmap splashLogo;

  	private Bitmap intro;

  	private Bitmap[] play;

  
  	private Bitmap[] looseLifeBitmap;

  	private AnimazioneScena cassaA;

	private Bitmap[] cassaBitmapA;
	
  	private AnimazioneScena cassaB;

	private Bitmap[] cassaBitmapB;

	
  	private Typeface typeFace;

  	//oggetti scena

  	private AnimazioneScena tapToPlay;

	
  	private Explosion looseLife;

  	

  	private boolean first;

  	

  	//tasto back del leyout della classifica

  	private Dialog dlg;

  	

  	private AnimazioneScena pause;

  	

  	

  	private int lastBanconeSpawn=0;

  	private int countLastBancone=0;

  	

  	//numero livelliBancone

  	public static int numBanconi = 4;

	

	//Bitmap sfondo

	private Bitmap sfondo;

	private Bitmap gameOver;

	

	//oggetti scena

	private LivelloBancone[] livelloBancone;

	private Player player;


	//private LifeDrawer lifeDrawer;

	

	private GameLoop collider;

	private Thread thread;

	
	
	



	private View v;

	private BitmapResizer resizer;

	private SurfaceHolder holder;

	private Context ctx;

	private Resources res;

	

	private class MyHandler extends Handler{

		public MyHandler(Looper mainLooper) {

			super(mainLooper);

		}



		public void handleMessage(Message msg){

			if(msg.what==1){

				SessionGame.this.doDraw();

			}else if(msg.what==2){

				//System.out.println("TENTATIVO DI ACCESSO ALLA CLASSIFICA");

				try {

					if(server.isOnline()){

					//System.out.println("APRO LA CLASSIFICA");

						initClassifica();

					}else{

						state=MENU;

					}

					ready = true;

					

				} catch (Exception e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}else if(msg.what==3){

				

				//state=PAUSE;

				showPause();

			}else if(msg.what==4){
				showTutorial();
			}

		}

	}

	

	

	private SoundManager sound;//classe che gesisce i suoni

	

	private int statePause = 0;

	

	private MyHandler handler = new MyHandler(Looper.getMainLooper());

	

	private boolean touchedUP = false;

	private boolean touched = false;

	private float touched_x;

	private float touched_y;

	private boolean ready = true;

	

	public SessionGame(Context context, AttributeSet attrs) {

		super(context, attrs);

		

		this.holder = this.getHolder();

		holder.addCallback(this);

		

		this.ctx=context;

		res = this.ctx.getResources();

		initSpeed();

		//inizializzo la memoria cache

	/*	 mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

		        @Override

		        protected int sizeOf(String key, Bitmap bitmap) {

		            // The cache size will be measured in kilobytes rather than

		            // number of items.

		            return bitmap.getByteCount() / 1024;

		        }

		    };

		    */





		//initialization

		

		

		//init sound

		sound = new SoundManager(ctx);

		
		typeFace=Typeface.createFromAsset(res.getAssets(),"fonts/PressStart2P.ttf");
		

		//inizializzo gli oggetti da mettere nella scena    

		this.livelloBancone = new LivelloBancone[this.numBanconi];  

		  

		//this.state=PAUSE;

		this.resizer= new BitmapResizer(this);

		

		this.db=new DBGrogChallenge(ctx);

		

		

		createThread();

		

		

	}

	

	public void setConnectJson(ConnectJsonUtil c){

		this.server=c;

	}

	

	public void createThread(){

		//Log.d("GROGCHALLENGE","init Thread");

this.collider = new GameLoop() {

			

	

			@Override

			public void init() {

				// TODO Auto-generated method stub

				

				

			}

	

			@Override

			public void update() {

				// TODO Auto-generated method stub

				if(touched){//se c'è stato un tocco sul display

					if(state == RUN){
						Rect leftScreen = new Rect(0,0, getWidth()/2, getHeight());
						Rect pauseShape = pause.getShape();

						int x = (int)touched_x;

						int y = (int)touched_y;
 


						if(pauseShape.contains(x,y)){

							touched=false;

							handler.sendMessage(handler.obtainMessage(3));

						}else  if(leftScreen.contains(x, y)){

							

								//muovo in avanti il player

								player.running();

								touched=false;

								//Log.d("PLAYER","corre: " + player.getX());

							

						}else{

							//controllo se ho tappato sui barili
							
							
							touched=false;

							
							for(int i = 0;i < numBanconi;i++){

								Rect shape = livelloBancone[i].getBarile().getShape();

								if( shape.contains(x,y) ){
									
									livelloBancone[i].getBarile().tap();


									if(player.getNumLivelloBancone() == livelloBancone[i].getNumLivelloBancone()){

										//verso il grog 

										player.moveTo(shape.bottom - player.getHeight());

										player.setNumLivelloBancone(livelloBancone[i].getNumLivelloBancone());

										player.versaGrog();

										sound.playLancioBottiglia();

									}else{

									

									player.moveTo(shape.bottom - player.getHeight());

									player.setNumLivelloBancone(livelloBancone[i].getNumLivelloBancone());
									sound.playJump();
										

									}

								}else{
									
									touchedUP=true;

									player.stopRunning();

								}

								

							}

						}

					}else if (state == GAMEOVER){
						long t = System.currentTimeMillis();
						
						if(t-timeGameOver>3000){
							refresh();
						//Log.d("TAP", "stato GAMEOVER");

						ready = false;

						touched = false;
						ready = true;

						activity.loopMusicReset();

						activity.loopMusicPlay();

						state=MENU;

						}

						

					}else if(state == MENU){
						Rect shapeCassaA = cassaA.getShape();

						Rect shapeCassaB = cassaB.getShape();
						if(shapeCassaA.contains((int)touched_x,(int) touched_y)){

							//tap sul bottone classifica
							
							ready = false;

							touched = false;

							state=CLASSIFICA;
							
							handler.sendMessage(handler.obtainMessage(2));

						}else if(shapeCassaB.contains((int)touched_x,(int) touched_y) ){
							//lancio il tutorial grafico
							touched= false;
							handler.sendMessage(handler.obtainMessage(4));
							
						}else{
						
							ready = false;

							touched = false;

							first=true;

							activity.loopMusicStop();
							
							//devo far partire il gioco
							if(tutorialState){
								
								state=TUTORIAL;
								timeTutorial=System.currentTimeMillis();
							}else{
								state=RUN;

							}
							ready=true;

						}

						

					}else if(state == TUTORIAL){
						if(stepTutorial != 5){	
						
							Rect shapeBarile = livelloBancone[NUM_BANCONE_TUTORIAL].getBarile().getShape();
						
							if(!tapBarile){
								if(stepTutorial == 1 && shapeBarile.contains((int)touched_x,(int) touched_y)){
									tapBarile=true;
									touched = false;
									stepTutorial++;
									//sposto il player
									player.moveTo(shapeBarile.bottom - player.getHeight());

									player.setNumLivelloBancone(NUM_BANCONE_TUTORIAL);
									sound.playJump();
									tutorial.stepFrame();
									livelloBancone[NUM_BANCONE_TUTORIAL].addEnemy();
								}
								
							}else if(!lancioBottiglia){
								if(player.getNumLivelloBancone() == NUM_BANCONE_TUTORIAL){
									//se il player è sul barile del tutorial
									if(shapeBarile.contains((int)touched_x,(int) touched_y)){
										lancioBottiglia=true;
										touched = false;
										
										//lancio la bottiglia
										//verso il grog 
										
										player.versaGrog();

										sound.playLancioBottiglia();
									}
									
								}
							
								
							}else if(!prendiBottiglia && stepTutorial == 4){
									
									
								int x = (int)touched_x;

								int y = (int)touched_y;

								//Rect shapeBancone = livelloBancone[i].getBancone().getShape();

								Rect leftScreen = new Rect(0,0, getWidth()/2, getHeight());

								 if(leftScreen.contains(x, y)){

										if(player.getNumLivelloBancone() == livelloBancone[NUM_BANCONE_TUTORIAL].getNumLivelloBancone()){

											//muovo in avanti il player

											player.running();

											touched=false;

											//Log.d("PLAYER","corre: " + player.getX());

										}
							
								 }
						
							}
							
							
						}else{
							tutorialState=false;
							if(db.getUtentiCount()>0){
								Utente utente = db.getAll().get(0);//avrò solo un utente in locale
									if(utente.getFirst()==0){
										//è la prima volta che viene aperta la sessione di gioco
										
										utente.setFirst(1);
										db.update(utente);
							  		 }
									
								}
							state=RUN;
							
							
						}		
							
					}//FINE TUTORIAL

					

					

				}

				if(touchedUP ){

					if(player!= null){

					player.stopRunning();

						touchedUP=false;

					}

				

				}
				try {

					Thread.sleep(1000/200);

				} catch (InterruptedException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}
				

			}//fine update



			

		};

		

		

		this.thread = new Thread(new Runnable() {

			private long animTimeCasse;

			@Override

			public void run() {

				// TODO Auto-generated method stub

					long t1 = System.currentTimeMillis();

					 first = true;

						while(running){//gestione loop
							if(state!= PAUSE){
										if(state== RUN){
			
											//metodo che sposta gli oggetti sulla scena			
			
											   muovi();
			
											   		if(first){
											   		spawnTime = System.currentTimeMillis();
											   		animTime = System.currentTimeMillis();		
			
											   		timeMemory = animTime;
			
											   		first=false;
			
													}else{
			
														spawn(first,t1);
			
													}
			
											   		long t = System.currentTimeMillis();
			
											   		if(t-timeMemory >= 1000){
											   			
											   			
											   			
														timeMemory = System.currentTimeMillis();
			
														time--;
			
														
														
														//timeBonus++;
			
													}
			
													/*if(timeBonus >= 5){
			
														readyBonus=true;
			
													}*/
			
													if(life<= 0 || time <= 0){
			
														
			
														callGameOver();
			
																
			
													}
			
													handler.sendMessage(handler.obtainMessage(1));
			
												
			
													
			
										}//FINE STATO RUN	
			
										
			
										else if(state==GAMEOVER){
											
											
											
											if(System.currentTimeMillis() - animTime > 700){
			
												 animTime = System.currentTimeMillis();		
			
												 //aggiorna le animazioni ogni 700 millisecondi
			
												 tapToContinue.stepFrame();
												
			
												}
			
											handler.sendMessage(handler.obtainMessage(1));
			
										}//FINE STATO GAMEOVER
			
										
			
										else if(state==LOADED){
			
											long t2 = System.currentTimeMillis();	
			
											if(t2-t1 > 4000){
			
											//splashLogo.recycle();
			
											//splashLogo=null;
			
												state=MENU;
			
												ready=true;
			
											}
			
											handler.sendMessage(handler.obtainMessage(1));
			
										}
			
									
			
									
			
										else if(state==SPLASH){
			
											
			
											if(!caricato){
			
												caricaIntro();
			
												carica();
			
												handler.sendMessage(handler.obtainMessage(1));
			
										    	
			
												//dati
			
												loadDati();
			
												
			
												
			
												
			
												caricato = true;
			
												state=LOADED;
			
											}
			
												handler.sendMessage(handler.obtainMessage(1));
			
											
			
											state=LOADED;
			
										}
			
										
			
										else if(state==MENU){
			
											if(first){
			
												activity.loopMusicPlay();
			
											animTime = System.currentTimeMillis();		
											animTimeCasse = System.currentTimeMillis();	
											first=false;
			
											}
			
											if(System.currentTimeMillis() - animTime > 700){
			
											 animTime = System.currentTimeMillis();		
			
											 //aggiorna le animazioni ogni 700 millisecondi
			
											tapToPlay.stepFrame();
			
											
											}
											if(System.currentTimeMillis() - animTimeCasse > 2000){
			
												 animTimeCasse = System.currentTimeMillis();		
			
												 //aggiorna le animazioni ogni 700 millisecondi
			
												
			
												cassaA.stepFrame();
												cassaB.stepFrame();
			
												}
			
											handler.sendMessage(handler.obtainMessage(1));
			
										}else if(state==CLASSIFICA){
			
											
			
											//handler.sendMessage(handler.obtainMessage(1));
			
										}else if(state==TUTORIAL){
											runTutorial();
											handler.sendMessage(handler.obtainMessage(1));
										}
			
										
			
										
			
										
			
										try {
			
											Thread.sleep(FPS);
			
										} catch (InterruptedException e) {
			
											// TODO Auto-generated catch block
			
											e.printStackTrace();
			
										}
										
							}		
						}//FINE WHILE

			

			}

			

		});

		

	

		

		

	}

	

	public void start(){

		

		this.running=true;

		collider.init();

		collider.setRunning(true);

		collider.start();

		this.thread.start();

	}

	

	public void stop(){

		this.running=false;

		try {

			this.thread.join();

			this.collider.finish();

			this.collider.join();

		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

	

	private int countPause=1;
	
	public void pause(){

		Log.d("GROGCHALLENGE", "state: " + state +  " count:" + countPause);
		countPause++;
		if(state == RUN || state == MENU || state == TUTORIAL ){
		this.lastState=this.state;
		}
		
		
		this.collider.pauseLoop();
		
		Log.d("GROGCHALLENGE", "laststate: " + lastState);
		
		this.state=PAUSE;
		//Log.d("GROGCHALLENGE", "laststate: " + lastState);
		collider.pauseLoop();

	}

	

	

	

	

	public int getLastState() {

		return lastState;

	}

	public void setLastState(int lastState) {

		this.lastState = lastState;

	}
	
	
	

	public void resume(){
		
				ready = true;
		
				Log.d("GROGCHALLENGE", "laststate: " + lastState);
		
				if(this.lastState != 0 && this.lastState != GAMEOVER){
		
					this.state=this.lastState;
		
					if(state==MENU){
		
					activity.loopMusicPlay();
					
					}else if(state==RUN){
		
						state=PAUSE;
		
						if(dlg==null){
		
							showPause();
		
						}else{
		
							if(!dlg.isShowing()){
		
								showPause();
		
							}
		
						}
		
					}else if(state==TUTORIAL){
						state=PAUSE;
						
						if(dlg==null){
		
							showPause();
		
						}else{
		
							if(!dlg.isShowing()){
		
								showPause();
		
							}
		
						}
					}
		
					
		
					
		
				}
		
				else state = SPLASH;
		
				if(this.collider != null){
		
					this.collider.resumeLoop();
		
				}
		
	}//fine metodo resume

	

	@Override

	public void surfaceChanged(SurfaceHolder holder, int format, int width,

			int height) {

		// TODO Auto-generated method stub

		

		

	}



	@Override

	public void surfaceCreated(SurfaceHolder holder) {

		// TODO Auto-generated method stub

		//Log.d("GROGCHALLENGE","surface created");

		 if (this.thread.getState() == Thread.State.TERMINATED)

		    {

			 	

			 this.createThread();

			 

		    }

		

		DisplayMetrics displayMetrics = new DisplayMetrics();

		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut

		wm.getDefaultDisplay().getMetrics(displayMetrics);

		

		

				//CARICO IL LOGO

				int h=SessionGame.this.getHeight();

				int w = SessionGame.this.getWidth();

				

				final float scale = getResources().getDisplayMetrics().density;

				

				w = (int) (w/scale);

				h= (int) (h/scale);

				

				//System.out.println("AAAAAA: " + w + " - " + h);

				sfondo= resizer.decodeSampledBitmapFromResource(res,R.drawable.sfondo,w,h);

				

				splashLogo= BitmapFactory.decodeResource(res,R.drawable.logo);

			

			

		    	start();//metodo che avvia i thread

		    	

		    	

		    	

		    

	}



	

	

	@Override

	public void surfaceDestroyed(SurfaceHolder holder) {

		this.stop();

		 

		boolean retry=true;

	        while (retry) {

	            try {

	               this.thread.join();

	               this.collider.join();

	                retry = false;

	            } catch (InterruptedException e) {

	            }

	        }

		

	       

	        

	}



	private Bitmap[] tapToContinueBitmap;
	private AnimazioneScena tapToContinue;
	
	private Bitmap[] tutorialBitmap;
	private AnimazioneScena tutorial;
	private Bitmap[] frecciaTutorialBitmap;
	private AnimazioneScena frecciaTutorial;
	
	private AnimazioneScena dobloniPunteggio;
	
	private AnimazioneScena clessidra;
	
	  public void carica(){
		  
		  Bitmap[] clessidraBitmap = new Bitmap[1];
		  clessidraBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.clessidra);
		  clessidra= new AnimazioneScena(ctx, getWidth()/2, 0, clessidraBitmap);
		  
		  Bitmap[] dobloniPunteggioBitmap = new Bitmap[1];
		  dobloniPunteggioBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.dobloni_punteggio);
		  dobloniPunteggio = new AnimazioneScena(ctx, 0, 0, dobloniPunteggioBitmap);
		  
		  
		Bitmap[] dobloniBitmap= new Bitmap[1];
		  dobloniBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.dobloni);
		  
		  
		  //////dati per il tutorial
		  
		  frecciaTutorialBitmap = new Bitmap[2];
		  frecciaTutorialBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.freccia_1);

		  frecciaTutorialBitmap[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.freccia_2);
		  
		  frecciaTutorial = new AnimazioneScena(ctx, 0,0,frecciaTutorialBitmap);
		  
		  tutorialBitmap = new Bitmap[6];
		  	String lang = Locale.getDefault().getDisplayLanguage();
			//Log.d("LANGUAGE",lang);
			
			if(lang.equals("italiano")){
			
			
		  tutorialBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_1);

		  tutorialBitmap[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_2);

		  tutorialBitmap[2] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_3);

		  tutorialBitmap[3] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_4);

		  tutorialBitmap[4] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_5);

		  tutorialBitmap[5] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_6);
			}else{
				 tutorialBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_1_eng);

				  tutorialBitmap[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_2_eng);

				  tutorialBitmap[2] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_3_eng);

				  tutorialBitmap[3] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_4_eng);

				  tutorialBitmap[4] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_5_eng);

				  tutorialBitmap[5] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tutorial_6_eng); 
				
			}
		  this.tutorial=new AnimazioneScena(ctx, 0, getHeight()-tutorialBitmap[0].getHeight(), tutorialBitmap);
		  
		  //////////////////////
		  
		  this.tapToContinueBitmap = new Bitmap[2];
		  tapToContinueBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tap_to_continue1);
		  tapToContinueBitmap[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.tap_to_continue2);
		  this.tapToContinue = new AnimazioneScena(ctx, this.getWidth()/2 - tapToContinueBitmap[0].getWidth()/2, this.getHeight()-tapToContinueBitmap[0].getHeight(), tapToContinueBitmap);
		  
		  
		  this.bonusPoint = new Bitmap[1];

		  bonusPoint[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.piu100);

		  //looseLifeBitmap = new Bitmap[1];

		 // looseLifeBitmap[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.loose_life);

		  

		  Bitmap[] pause = new Bitmap[1];

		  

		  pause[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.pause);

		  this.pause = new AnimazioneScena(ctx, pause[0].getWidth()/2, this.getHeight() - ((int)(pause[0].getHeight()*1.5)) , pause);

		  

		  this.gameOver = resizer.decodeSampledBitmapFromResource(res, R.drawable.game_over);

		  

		//  Bitmap skull = resizer.decodeSampledBitmapFromResource(res, R.drawable.life);

		//  this.lifeDrawer = new LifeDrawer(this.getWidth()-skull.getWidth(), 0, skull);

		  

		  Bitmap tile = resizer.decodeSampledBitmapFromResource(res, R.drawable.bancone_tile);

		  

		  Bitmap[] esplosione = new Bitmap[1];

		  esplosione[0]= resizer.decodeSampledBitmapFromResource(res, R.drawable.esplosione);

		  Bitmap[] esplosioneBoccale  = new Bitmap[1];

		  esplosioneBoccale[0]= resizer.decodeSampledBitmapFromResource(res, R.drawable.esplosione_boccale);

		  

		  

		  Bitmap fumetto = resizer.decodeSampledBitmapFromResource(res, R.drawable.fumetto);	

		  Bitmap[] proiettile = new Bitmap[1];

		  

		  proiettile[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.proiettile);

		  

		  Bitmap[] nemici = new Bitmap[15];

		  nemici[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.capitano);



		  nemici[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.capitano_beve);



		  nemici[2] = resizer.decodeSampledBitmapFromResource(res, R.drawable.capitano_sparo);

		  

		  nemici[3] = resizer.decodeSampledBitmapFromResource(res, R.drawable.grasso);



		  nemici[4] = resizer.decodeSampledBitmapFromResource(res, R.drawable.grasso_beve);



		  nemici[5] = resizer.decodeSampledBitmapFromResource(res, R.drawable.grasso_spara);

		  

		  nemici[6] = resizer.decodeSampledBitmapFromResource(res, R.drawable.vecchio);



		  nemici[7] = resizer.decodeSampledBitmapFromResource(res, R.drawable.vecchio_beve);



		  nemici[8] = resizer.decodeSampledBitmapFromResource(res, R.drawable.vecchio_spara);

		  

		  nemici[9] = resizer.decodeSampledBitmapFromResource(res, R.drawable.nero);



		  nemici[10] = resizer.decodeSampledBitmapFromResource(res, R.drawable.nero_beve);



		  nemici[11] = resizer.decodeSampledBitmapFromResource(res, R.drawable.nero_spara);

		  

		  nemici[12] = resizer.decodeSampledBitmapFromResource(res, R.drawable.scheletro);



		  nemici[13] = resizer.decodeSampledBitmapFromResource(res, R.drawable.scheletro_beve);



		  nemici[14] = resizer.decodeSampledBitmapFromResource(res, R.drawable.scheletro_sparo);

		  

		  //carico le bitmap

		  Bitmap[] barileSprite = new Bitmap[2];

		  barileSprite[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.barile);
		  barileSprite[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.barile2);
		  

		  Bitmap[] banconeSprite = new Bitmap[1];

		  banconeSprite[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.bancone);

		 

		  Bitmap[] portaSprite = new Bitmap[1];

		  portaSprite[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.porta);

		 

		  Bitmap[] boccale = new Bitmap[1];

		  boccale[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.boccale);

		  Bitmap[] boccaleRotto = new Bitmap[1];

		  boccaleRotto[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.boccale_rovesciato);

		  

		  Bitmap[] punteggi = new Bitmap[5];

		  punteggi[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.piu10);



		  punteggi[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.meno10);



		  punteggi[2] = resizer.decodeSampledBitmapFromResource(res, R.drawable.piu20);



		  punteggi[3] = resizer.decodeSampledBitmapFromResource(res, R.drawable.meno20);

		  punteggi[4] = resizer.decodeSampledBitmapFromResource(res, R.drawable.piu50);


		  int x = 0;

		  int y = 0;

		  int screenWidth = this.getWidth();

		  

		  int spazioDisp = getHeight() - getHeight()/5;

		  

		  int P = spazioDisp/numBanconi;

		  

		  Bitmap[] boccaleVuoto = new Bitmap[1];

		  boccaleVuoto[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.bottiglia_vuota);

		  

		  Bitmap[] playerSprites = new Bitmap[5];

		  playerSprites[0] =resizer.decodeSampledBitmapFromResource(res, R.drawable.locandiere);

		  playerSprites[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.locandiere_girato);

		  playerSprites[2] = resizer.decodeSampledBitmapFromResource(res, R.drawable.locandiere_versa);

		  playerSprites[3] = resizer.decodeSampledBitmapFromResource(res, R.drawable.locandiere_corsa1);

		  playerSprites[4] = resizer.decodeSampledBitmapFromResource(res, R.drawable.locandiere_corsa2);

		  

		  this.player = new Player(ctx,0,0, playerSprites);

		 

		  

		  for(int i = 0; i< this.numBanconi; i++){

			  y = getHeight() - (i*P + i*3);

			 

			  

			TappableAnimazioneScena barile = new TappableAnimazioneScena(ctx, 0, 0, barileSprite);

			  

			  

			  AnimazioneScena bancone = new AnimazioneScena(ctx, 0, 0, banconeSprite);

			  

			  AnimazioneScena porta = new AnimazioneScena(ctx, 0, 0, portaSprite);	  

			  

			  

			this.livelloBancone[i] = new LivelloBancone(ctx,x, y, screenWidth, barile, bancone,player, porta,this,sound);

			

			this.livelloBancone[i].setNumLivelloBancone(i);

			

			//passo il riferimento alle bitmap dei boccali

			this.livelloBancone[i].setBoccale(boccale);

			this.livelloBancone[i].setBoccaleRotto(boccaleRotto);

			

			//passo il riferimento alle bitmap dei nemici

			this.livelloBancone[i].setNemici(nemici);

			

			//passo il riferimento alla bitmap del proiettile

			this.livelloBancone[i].setProiettile(proiettile);

			

			//passo il riferimento al fumetto ( non è array)

			this.livelloBancone[i].setFumetto(fumetto);

			

			//passo il riferimento ai veri tipi di esplosioni

			this.livelloBancone[i].setEsplosione(esplosione);

			this.livelloBancone[i].setEsplosioneBoccale(esplosioneBoccale);

			
			this.livelloBancone[i].setDoblone(dobloniBitmap);
			//aggiungo il tile

			this.livelloBancone[i].setTile(tile);

			this.livelloBancone[i].setBoccaleVuoto(boccaleVuoto);

			this.livelloBancone[i].setPunteggiBitmap(punteggi);

		  }

		  

		 

			  //carico il player

			  

			 

		  TappableAnimazioneScena barileIniziale = this.livelloBancone[0].getBarile();

			  Rect shape = barileIniziale.getShape();

			  spawnPlayer = new Point(shape.left-playerSprites[0].getWidth() - (playerSprites[0].getWidth()/2) , shape.bottom - playerSprites[0].getHeight());

			 player.setLivelloBancone(livelloBancone);

			 player.setX(spawnPlayer.x);

			 player.setY(spawnPlayer.y);

			 player.setAsse(spawnPlayer.x);

			  player.setNumLivelloBancone(this.livelloBancone[0].getNumLivelloBancone());

		

		//setto la freccia per il tutorial
			  
			  TappableAnimazioneScena barileTutorial = livelloBancone[NUM_BANCONE_TUTORIAL].getBarile();
		
		this.frecciaTutorial.setX(barileTutorial.getX());
		this.frecciaTutorial.setY(barileTutorial.getY()+barileTutorial.getHeight());

			

		}

	

	

	

	

	public void doDraw(){

		 Canvas c = null;

         try {

        	 

        	 Surface surface = holder.getSurface();

        	 if (surface != null && surface.isValid()) {  // !important - to check the availability of surface

        	 

        	 

	             c = holder.lockCanvas();

	             synchronized (holder) {

		            	 if(c != null){

				            		if(state == RUN){

					            			drawRun(c);					            			

						            	}else if(state == GAMEOVER){

						            		
						            		
						            		long t = System.currentTimeMillis();
			            					
						            		if(t-timeGameOver>2000){

						            			Rect r = new Rect(0,0,this.getWidth(),this.getHeight());

						            			c.drawBitmap(gameOver, null,r,null);	

						            			drawPunteggi(c);
						            		}else{
						            			//disegno la scena ferma
						            			drawRun(c);
						            		}
			            					if(t-timeGameOver>3000){
			            						
			            						this.tapToContinue.doDraw(c);
			            					}

			            					

						            		

						            	}else if(this.state==this.SPLASH || this.state==this.LOADED){

				            				if(this.splashLogo != null){
				            						
				            					c.drawColor(Color.WHITE);

				            					c.drawBitmap(splashLogo, getWidth()/2-splashLogo.getWidth()/2, this.getHeight()/2-splashLogo.getHeight()/2, null);

				            					//Rect r = new Rect(0,0,this.getWidth(),this.getHeight());

				            					//c.drawBitmap(splashLogo, null,r,null);	

				            					//System.out.println("splash screen");

				            				}

				            				

				            			}else if(this.state==this.MENU){

				            				Rect r = new Rect(0,0,this.getWidth(),this.getHeight());

			            					

				            				c.drawColor(Color.WHITE);//refresh di bianco del canvas 

				            				

				            				//c.drawBitmap(intro, 0, 0,null);

				            					c.drawBitmap(intro, null, r, null);

				            				tapToPlay.doDraw(c);

				            				

				            				this.cassaA.doDraw(c);
				            				this.cassaB.doDraw(c);

				            			}else if(this.state==this.CLASSIFICA){

				            				

				            			}else if(this.state==TUTORIAL){
				            				
				            				drawRun(c);		
				            				
				            				//rettangolo nero
				            				Rect rb = new Rect(tutorial.getX(),tutorial.getY(),this.getWidth(),this.getHeight());
				            				Paint paint = new Paint();
				            				 paint.setStyle(Paint.Style.FILL);
				            				    paint.setColor(Color.BLACK); 
				            				    c.drawRect(rb, paint);
				            				
				            				
				            				this.tutorial.doDraw(c);
				            				if(stepTutorial==1 || stepTutorial == 2){
				            					frecciaTutorial.doDraw(c);
				            				}
				            				if(stepTutorial == 5){
				            					//disegno il "tap to continue"
				            					
				            				}
				            				
				            			}

				            		

				            		

				            		

				            		

				            		

		            	 }

	            }

	            	

        	 }

         } catch(Exception e){

        	 

         }finally {

             // do this in a finally so that if an exception is thrown

             // during the above, we don't leave the Surface in an

             // inconsistent state

       

             if (c != null) {

            	

                 holder.unlockCanvasAndPost(c);

            	 

             }

             

         }

	}

		

	// Given an action int, returns a string description

	public static String actionToString(int action) {

	    switch (action) {

	                

	        case MotionEvent.ACTION_DOWN: return "Down";

	        case MotionEvent.ACTION_MOVE: return "Move";

	        case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";

	        case MotionEvent.ACTION_UP: return "Up";

	        case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";

	        case MotionEvent.ACTION_OUTSIDE: return "Outside";

	        case MotionEvent.ACTION_CANCEL: return "Cancel";

	    }

	    return "";

	}

	

	public boolean isTutorialState() {
		return tutorialState;
	}

	public void setTutorialState(boolean tutorialState) {
		this.tutorialState = tutorialState;
	}

	@Override

	  public boolean onTouchEvent(MotionEvent event) {
/*
		String DEBUG_TAG = "TOUCH EVENT";

	   // TODO Auto-generated method stub

		int action = MotionEventCompat.getActionMasked(event);

		// Get the index of the pointer associated with the action.

		int index = MotionEventCompat.getActionIndex(event);

		int xPos = -1;

		int yPos = -1;



		//Log.d(DEBUG_TAG,"The action is " + actionToString(action));

		            

		if (event.getPointerCount() > 1) {

		    Log.d(DEBUG_TAG,"Multitouch event"); 

		    // The coordinates of the current screen contact, relative to 

		    // the responding View or Activity.  

		    if(player.isRunning())touchedUP=true;

		    

		   xPos = (int)MotionEventCompat.getX(event, index);

		    yPos = (int)MotionEventCompat.getY(event, index);

		    

		} else {

		    // Single touch event

		   Log.d(DEBUG_TAG,"Single touch event"); 

		  xPos = (int)MotionEventCompat.getX(event, index);

		    yPos = (int)MotionEventCompat.getY(event, index);

		}

		

		

	  

	  

		 touched_x = xPos;

		   touched_y = yPos;*/

		   
		 touched_x = event.getX();

		   touched_y = event.getY();
	   

	   int act = event.getAction();

	  /* switch(action){

	   case MotionEvent.ACTION_DOWN:

	    touched = true;

	    break;

	   case MotionEvent.ACTION_MOVE:

	    touched = true;

	    break;

	   case MotionEvent.ACTION_UP:

	    touched = false;

	    break;

	   case MotionEvent.ACTION_CANCEL:

	    touched = false;

	    break;

	   case MotionEvent.ACTION_OUTSIDE:

	    touched = false;

	    break;

	   default:

	   }*/

	   

	 

	  if(ready){ 

		  

	   if(act == MotionEvent.ACTION_DOWN){
		
		   touched = true;
		   
	   }else if(act == MotionEvent.ACTION_UP){

		  // touched=false;

		   touchedUP=true;

		   

	   }else{

		   touched = false;

		   

	   }

	   

	   

	  }

	   

	return true;

	   

	}

	

	

	



	public int getState() {

		return this.state;

	}



	public void setState(int state) {

		this.state=state;

	}

	





	public void refresh(){

		

		//Log.d("GROGCHALLENGE", "refresh");

		//rimetto il player nella posizione di partenza

		player.setX(spawnPlayer.x);

		player.setY(spawnPlayer.y);

		 player.setNumLivelloBancone(this.livelloBancone[0].getNumLivelloBancone());

		 	intervalSpawn=INTERVAL_SPAWN;
		 	currentIntervalCount=INTERVAL_COUNT;
		 	intervalSpawnRandom=INTERVAL_SPAWN_RANDOM;
		  touched=false;

		  //svuoto i banconi

		  for(int i = this.numBanconi-1;i >= 0;i--){

				this.livelloBancone[i].init();

			}

		  //readyBonus=false;

		 // timeBonus =0;

		  this.life=1;

		  this.punteggio=0;

		  this.time=FINALTIME;

		//  this.state=RUN;

		  //la clessidra non deve lampeggiare
		  if(clessidra.isFlash()){
			  clessidra.setFlash(false);
		  }

	}

	

	

	

	

	private void muovi(){

		

		for(int i=0;i< numBanconi;i++){

			//int limit = livelloBancone[i].getBancone().getX() + livelloBancone[i].getBancone().getShape().width();

			int limitPlayer =spawnPlayer.x;

			 

			LivelloBancone livello = livelloBancone[i];

			int limit = livello.getBancone().getX() + livello.getBancone().getWidth();

			int limitBomb = spawnPlayer.x;

			

			//muovo i proiettili

			ArrayList<AnimazioneScena> proiettili = livello.getProiettili();

			AnimazioneScena bancone = livello.getBancone();

			for(int h = 0;h <proiettili.size();h++){

				AnimazioneScena proiettile = proiettili.get(h);

				int x = proiettile.getX();

				if(x <= limitBomb){

					proiettile.setX(x + speedBullet);

					

					Rect r = proiettile.getShape();

					if(livello.getNumLivelloBancone()==player.getNumLivelloBancone()){

						if(r.intersect(player.getShape())){

							//il player è stato colpito dal proiettile 
							sound.playExplosion();
							//showLooseLife();

							livello.addEsplosione(r.left,r.top);

							livello.removeProiettile(h);

							life--;

							//int xP = player.getX();

							//int yP = player.getY();

							//livello.addPoint(-20, xP, yP);

							//decreasePunteggio(-20);

							

						}

					}

				}else{
					
					//la bomba cade a terra ed esplode
					sound.playExplosion();
					livello.addEsplosione(x, proiettile.getY());

					livello.removeProiettile(h);

				}

				

			}

			
			//boccali pieni
			ArrayList<AnimazioneScena> boccaliPieni = livello.getBoccaliPieni();

			for(int j=0;j < boccaliPieni.size();j++){

				AnimazioneScena boccale = boccaliPieni.get(j);

				int x =boccale.getX();

				boccale.setX(x - speedBottigliaPiena);

				AnimazioneScena porta= livello.getPorta();

				if(boccale.getX() <=  porta.getX() + porta.getWidth()){

					//il boccale è uscito fuori dalla porta

					livello.removeBoccalePieno(j);

					int yP = porta.getY();

					int xP = porta.getX() + porta.getWidth();

					livello.addPoint(-20, xP, yP);

					decreasePunteggio(-20);

					this.increaseTime(-15);

					sound.playBottigliaRotta();

					
					

					livello.addBoccaleRotto(xP + boccale.getWidth(),livello.getBancone().getY());

				}

				//controllo le collisioni con i proiettili

				ArrayList<AnimazioneScena> proiettili2 = livello.getProiettili();

				for(int h = 0;h <proiettili2.size();h++){

					

						Rect r = proiettili2.get(h).getShape();

						if(boccale.getShape().intersect(r) || boccale.getShape().contains(r)){

							//il proiettile ha colpito un boccale 
							sound.playExplosion();
							livello.removeBoccalePieno(j);

							livello.removeProiettile(h);

							livello.addEsplosioneBoccale(r.left, r.top);

						}

				}

				

			}

			//boccali vuoti

			ArrayList<AnimazioneScena> boccaliVuoti = livello.getBoccaliVuoti();

			for(int h=0; h <  boccaliVuoti.size();h++){

				AnimazioneScena boccaleVuoto= boccaliVuoti.get(h);

				int x = boccaleVuoto.getX();

				

				if(player.getNumLivelloBancone()==livello.getNumLivelloBancone()){
					if(player.getShape().intersect(boccaleVuoto.getShape()) || player.getShape().contains(boccaleVuoto.getShape()) ){

					//il player ha preso il boccale 
					//può anche aver avanzato sul bancone
					
					livello.removeBoccaleVuoto(h);

					punteggio +=10;

					int xP = player.getX();

					int yP = player.getY();

					livello.addPoint(10, xP, yP);

					decreasePunteggio(10);
					
					continue;
					}
				}else if(x >= limit){//il boccale è al limite del bancone

					

					/*if(player.getNumLivelloBancone() == livello.getNumLivelloBancone()){

						//il player ha preso il boccale

						livello.removeBoccaleVuoto(h);

						punteggio +=10;

					}

				}else{*/

						//il boccale cade a terra

					int xP = boccaleVuoto.getX();

					int yP = boccaleVuoto.getY();

						livello.addPoint(-20, xP, yP);

						livello.addBoccaleRotto(xP, yP + livello.getBancone().getHeight());

						livello.removeBoccaleVuoto(h);

						decreasePunteggio(-20);

						this.increaseTime(-15);

						sound.playBottigliaRotta();

						continue;

					}
					boccaleVuoto.setX(x + speedBoccaleVuoto);
			}

			

			//nemici

			ArrayList<Enemy> nemici = livello.getEnemy();

			for(int j=0;j < nemici.size();j++){

				

				Enemy enemy = nemici.get(j);

				//boccali

				ArrayList<AnimazioneScena> boccaliPieni2 = livello.getBoccaliPieni();

				for(int k=0;k < boccaliPieni2.size();k++){

					

					//controllo sulle collisioni tra i nemici e i boccali
					Rect r = boccaliPieni2.get(k).getShape();
					if(enemy.getShape().intersect(r) || enemy.getShape().contains(r)){

							if(!enemy.isBevendo()){//se non sta già bevendo

								enemy.bevi();

								livello.removeBoccalePieno(k);

							}

					}

					

				}

				enemy.move(enemySpeed);

						

						int x = enemy.getX()+enemy.getWidth()/2;

						if(x >= limit - enemy.getWidth()){

							//il nemico è arrivato alla fine del bancone

							//showLooseLife();
							enemy.setFlash(true);
							int yE = enemy.getY();

							//livello.addPoint(-20, x, yE);

							//decreasePunteggio(-20);

							//livello.removeEnemy(j);//remove enemy

							//this.increaseTime(-10);

							

							life--;

						}else if(x <= 0){

							//sono riuscito a cacciare il nemico
							
							
							
							Point p = livello.getSpawnBoccali();

							AnimazioneScena porta = livello.getPorta();

							int xP = porta.getX() + porta.getWidth();

							int yP = porta.getY();

							livello.addPoint(10, xP, yP);

							livello.removeEnemy(j);//tolgo il nemico

							livello.addBoccaleVuoto(0, p.y);

							this.increaseTime(3);

							decreasePunteggio(10);
							
							boolean bonus = true;

							for(int v = 0; v < this.livelloBancone.length;v++){

								ArrayList<Enemy> en = livelloBancone[v].getEnemy();

								if(en.size() > 0){

									bonus = false;

									break;

								}

							}

							if(bonus){

								decreasePunteggio(100);

								increaseTime(10);

								showBonusPoint();

								sound.playEsceCento();

								//readyBonus=false;

								//timeBonus=0;

							}
							
						}

				

				

				

				

			}
			
			
			//dobloni 
			ArrayList<Explosion> dobloni = livello.getDobloni();
			
						
			for (int j = 0; j < dobloni.size(); j++) {
				Explosion doblone = dobloni.get(j);
				Rect shape = doblone.getShape();
				
				int x = doblone.getX();
				
				if(x < (livello.getBancone().getX() + livello.getBancone().getWidth() - doblone.getWidth())){
										doblone.setX(x+speedDobloni);
				}
				if(!doblone.isActive()){
					livello.removeDoblone(j);
				}else if(player.getNumLivelloBancone() == livello.getNumLivelloBancone()){
					if(player.getShape().intersect(shape) || player.getShape().contains(shape)){
					livello.addPoint(50, doblone.getX(),doblone.getY());
					punteggio = punteggio + 50;
					sound.playPikUpCoin();
					livello.removeDoblone(j);
					}
				}else{
					long t = System.currentTimeMillis();
					if(t-doblone.getInitExplosion()>=6000){
						doblone.setFlash(true);
					}
					
				}
			}
			

			ArrayList<PointFeedBack> point = livello.getPunteggi();

			for (int l =0; l< point.size();l++) {

				PointFeedBack p = point.get(l);

				int y = p.getY();

				p.setY(y-4);

				if(p.getY() <= 0){

					livello.removePoint(l);

				}

			}

			

			

		}

		

		/*if(readyBonus){

			boolean bonus = true;

			for(int i = 0; i < this.livelloBancone.length;i++){

				ArrayList<Enemy> enemy = livelloBancone[i].getEnemy();

				if(enemy.size() > 0){

					bonus = false;

					break;

				}

			}

			if(bonus){

				decreasePunteggio(100);

				increaseTime(10);

				showBonusPoint();

				sound.playEsceCento();

				readyBonus=false;

				timeBonus=0;

			}

		}
*/
		
		if(player.isRunning()){

			if(player.getX() >= 0 + player.getWidth()){

			int move = player.getX();

			player.setX(move-speed);

			}

		}else{

			player.setX(player.asse);

		}
		

	}

	

	/*private void showLooseLife(){

		int x = this.getWidth()/2 - looseLifeBitmap[0].getWidth()/2;

		int y = this.getHeight()/2 - looseLifeBitmap[0].getHeight()/2;

		this.looseLife = new Explosion(ctx, x,y, looseLifeBitmap);

	}*/

	private void showBonusPoint(){

		int x = this.getWidth()/2 - bonusPoint[0].getWidth()/2;

		int y = this.getHeight()/2 - bonusPoint[0].getHeight()/2;

		this.bonusP = new Explosion(ctx, x,y, bonusPoint);

	}

	

	//private int timeBonus =0;

	//private boolean readyBonus=false;

	private Bitmap[] bonusPoint;

	private Explosion bonusP;
	
	private final int INTERVAL_SPAWN = 3000;
	private final int INTERVAL_COUNT = 500;
	
	private long intervalSpawn = INTERVAL_SPAWN;
  	private long INTERVAL_SPAWN_RANDOM = 2000;
	private int currentIntervalCount= INTERVAL_COUNT;
	
	private long intervalSpawnRandom = INTERVAL_SPAWN_RANDOM;
	
	private void spawn(boolean first,long tfirst){
			long t = System.currentTimeMillis();
			
			if(t-spawnTime > intervalSpawn){
				spawnTime=System.currentTimeMillis();
				spawnBanconeRandom();
				//Log.d("SPAWN", "nemico spawnato ogni: " + intervalSpawn + " ms");
				if(punteggio>= currentIntervalCount){
					currentIntervalCount = currentIntervalCount + INTERVAL_COUNT;
					
					if(intervalSpawn > 2000){
						intervalSpawn = intervalSpawn - 100;
					}
				}
				
			}
			
			if(punteggio > 3000 && punteggio < 6000){
				intervalSpawnRandom = 1500;
			}else if(punteggio > 6000){
				intervalSpawnRandom = 1000;
			}

			if(punteggio >= 1500 && t - animTime > intervalSpawnRandom){
				
				animTime = System.currentTimeMillis();

				//probabilità che arrivi un nemico

				int max = 5;

				int min = 0;

				

				Random rn = new Random();

				int r = rn.nextInt(max - min + 1) + min;

			

				if(r  == 2 || r == 5 || r== 0  ){

				spawnBanconeRandom();
					

					

				}

				

			}

	}


public void spawnBanconeRandom(){
	//spawn nemico, devo scegliere da quale bancone
	Random rn = new Random();
	int max = livelloBancone.length-1;

	 int min = 0;

	

	int num = rn.nextInt(max - min + 1) + min;



		//ho scelto il bancone

		if(num == lastBanconeSpawn){

			countLastBancone++;

			if(countLastBancone == 2){

				livelloBancone[num].addEnemy();

				lastBanconeSpawn = num;

				countLastBancone = 0;

			}

		}else{

		livelloBancone[num].addEnemy();

		lastBanconeSpawn = num;

		countLastBancone = 0;

		}

}


	public void caricaIntro(){

			

			

			//////////////////////////LOAD BG

	    	intro=resizer.decodeSampledBitmapFromResource(res, R.drawable.bg_menu);

			/////////////////////////

			

			

			////////////////LOAD TAP TO PLAY

			play = new Bitmap[2];

			play[0]=resizer.decodeSampledBitmapFromResource(res, R.drawable.tap1);

			play[1]=resizer.decodeSampledBitmapFromResource(res, R.drawable.tap2);

			int x=0,y=0;

			int w = play[0].getWidth();

			int h = play[0].getHeight();

			

			//devo posizionare il TapToPlay al centro dello schermo

			x = getWidth()/2 - w/2;

			y = getHeight()/2 - h/2;

			

			

			tapToPlay = new AnimazioneScena(ctx, x, y, play);

			/////////////////////////////

			

			///carico le casse
			
			this.cassaBitmapA = new Bitmap[2];
			cassaBitmapA[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.cassa_score_a_2);

			cassaBitmapA[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.cassa_score_a_1);
			cassaA = new AnimazioneScena(ctx, 0, getHeight()-cassaBitmapA[0].getHeight(), cassaBitmapA);
			
			this.cassaBitmapB = new Bitmap[2];
			cassaBitmapB[0] = resizer.decodeSampledBitmapFromResource(res, R.drawable.cassa_score_b_1);

			cassaBitmapB[1] = resizer.decodeSampledBitmapFromResource(res, R.drawable.cassa_score_b_2);
			cassaB = new AnimazioneScena(ctx, getWidth()/2, getHeight()-cassaBitmapB[0].getHeight(), cassaBitmapB);


			

	}


	

	

	private int determineMaxTextSize(String str, float maxWidth)

	{

	    int size = 0;       

	    Paint paint = new Paint();



	    do {

	        paint.setTextSize(++ size);

	    } while(paint.measureText(str) < maxWidth);



	    return size;

	} //End getMaxTextSize()

	

	public void drawPunteggi(Canvas c){

		int paddingX = getWidth()/10;

		 Paint paint = new Paint();

	        paint.setColor(Color.WHITE);
	       
	        paint.setTypeface(typeFace);
	        
	        String strPunteggio = "Punteggio: " + punteggioGameOver;
	        String str = "Punteggio: 00000000000";
	      int size = determineMaxTextSize(str, this.getWidth()/4);

	     
	      
	        paint.setTextSize(size);

	        int offsetY = getHeight() / 4;

	        int y = offsetY * 3;

	       c.drawText(strPunteggio, paddingX,y, paint);

	       

	       String strRecord = "Record: " + record;

		       
		       paint.setTextSize(size);

		       int offsetX = getWidth()/4;

		       int x = offsetX * 3;

		       c.drawText(strRecord, x-paddingX,y, paint);

		       

	}

	

	

	public void initClassifica() throws Exception{
	
		this.lastState = state;

		if(server.isOnline()){

		if(db.getAll().get(0).getNome().equals(server.LOCAL_NAME)){

			//è la prima volta che viene aperta l'applicazione

			dlg= new Dialog(ctx);

				dlg.getWindow();

				LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				//View v = li.inflate(R.layout.dialog_classifica, null, false);

				 v = li.inflate(R.layout.insert_name, null, false);

				Button button = (Button) v.findViewById(R.id.buttonOk);

				button.setOnClickListener(new OnClickListener() {

				EditText nome = (EditText) v.findViewById(R.id.editText1);	

		        TextView errore = (TextView) v.findViewById(R.id.textView3);  

				@Override

		            public void onClick(View v) {

					String str = nome.getText().toString();

						HashMap<String, String> utente = null;
						boolean flag = false;
						try {
							
							if(!(str.equals(""))){
								if(Pattern.matches("[a-zA-Z_0-9]*",str)){
									Log.d("MATCH", str);
									flag = true;
									utente = server.checkName(str);
								}else{
									errore.setText("Non puoi registrarti con questo nome");
								}
							
							}else{
								errore.setText("Non puoi registrarti con questo nome");
							}
								
							if(flag){

				            	if(utente.get("result").equals("1") || str.equals("local")){
		
				            		errore.setText("Non puoi registrarti con questo nome");
		
				            	}else{
		
				            		nickName=nome.getText().toString();
		
				            		dlg.dismiss();
		
				            		try {
		
		
		
				            			//registro l'utente sul server
		
				            			//con il nickname scelto da lui
		
				            			Utente utente2 = db.getAll().get(0);
		
				            			String[] emails = server.getAccountNames();
		
				            			String email = emails[0];
		
				            			server.setEmail(email);
		
				            			utente2.setNome(nickName);
		
				            			utente2.setEmail(email);
				            			server.addPunteggio(nickName, utente2.getPunteggio(),utente2.getEmail());
				            			
				            			db.update(utente2);
		
				            			Toast.makeText(ctx, "Registration was successful", 3000).show();
										//showClassifica();
		
									} catch (Exception e) {
		
										// TODO Auto-generated catch block
		
										e.printStackTrace();
		
									}
		
				            	}
				            	
							}		

							
							
							
						} catch (JSONException e) {

							// TODO Auto-generated catch block

							e.printStackTrace();

						} catch (InterruptedException e) {

							// TODO Auto-generated catch block

							e.printStackTrace();

						} catch (ExecutionException e) {

							// TODO Auto-generated catch block

							e.printStackTrace();

						}

						
		            }

		        });

				Button cancel = (Button) v.findViewById(R.id.buttonCancel);

				cancel.setOnClickListener(new OnClickListener() {

					@Override

			            public void onClick(View v) {

			            	

						dlg.dismiss();

			            state=MENU;

			            }

			        });

				

				//dlg.setTitle("CLASSIFICATION");

				

				

				

					dlg.setContentView(v);

					dlg.setOnDismissListener(this);

					

					dlg.show();

					



					

					

			

		}else{

			showClassifica();

		}

		

		

		}
		
		

	}

	

	

	

		@Override

		public void onDismiss(DialogInterface dialog) {

			//Log.d("DIALOG", "dismiss");

			state=MENU;

		}

		

	

	

	public void showClassifica() throws Exception{

	if(server.isLoaded()){

		

		//dialog della classifica

			dlg = new Dialog(ctx);

			dlg.getWindow();

			//LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			

			 LayoutInflater li = getActivity().getLayoutInflater();

			//View v = li.inflate(R.layout.dialog_classifica, null, false);

			View v = li.inflate(R.layout.classifica, null, false);

			/*Button button = (Button) v.findViewById(R.id.backButton);

			button.setOnClickListener(new OnClickListener() {

	

	            @Override

	            public void onClick(View v) {

	            dlg.dismiss();

	            state = MENU;

	            }

	        });*/

			dlg.setOnDismissListener(this);

			dlg.setTitle("CLASSIFICATION");

			

			//ListView modeList = (ListView) v.findViewById(R.id.listView2);

			//ArrayList<String> planetList = new ArrayList<String>();

			//ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(ctx, R.layout.simplerow, planetList);

			 

			

			
			LinearLayout layout = 	(LinearLayout) v.findViewById(R.id.linear);

            
			try {

				HashMap<String, String> topTen = server.getClassifica();

				for(int i=0; i<topTen.size();i++){

					//compongo la stringa dell'utente nella toplist

					String str = "" + (i+1);

					String user = topTen.get("" + (i + 1));

					String[] s = user.split("#");

					str = str + " " + s[0] + " : " + s[1];

					//modeAdapter.add(str);
					
					TextView tv = new TextView(ctx);
					
					tv.setTypeface(typeFace);
					tv.setTextColor(Color.WHITE);
					tv.setText(str);
					tv.setPadding(10,10,10,10);
					layout.addView(tv);
					

				}

				dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

				

				Utente utente = server.getUtente();

				

				TextView nome = (TextView) v.findViewById(R.id.textPosition);
				nome.setTypeface(typeFace);
				nome.setTextColor(Color.WHITE);
				TextView point = (TextView) v.findViewById(R.id.textPoint);
				point.setTypeface(typeFace);
				point.setTextColor(Color.WHITE);
				String str = utente.getNome() + ": " + utente.getPosition();

				nome.setText(str);

				str = "Score: " + utente.getPunteggio();

				point.setText(str);

				//modeList.setAdapter(modeAdapter);

			
				TextView topTenText = (TextView) v.findViewById(R.id.textTopTen);
				topTenText.setTypeface(typeFace);
				topTenText.setTextColor(Color.BLACK);
				

				

				

				dlg.setContentView(v);

				

				dlg.show();

									WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

			    lp.copyFrom(dlg.getWindow().getAttributes());

			    lp.width = WindowManager.LayoutParams.MATCH_PARENT;

			    lp.height = WindowManager.LayoutParams.MATCH_PARENT;

			

				

			    dlg.getWindow().setAttributes(lp);

				

				

			} catch (Exception e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			

	

	}else{
		Toast.makeText(ctx, "the server is not responding", Toast.LENGTH_LONG).show();
		state=MENU;
		
	}

			

			

}



	







private void initSpeed(){

	int density= (int) getResources().getDisplayMetrics().densityDpi;

	//Log.d("DENSITY","" +  density);

	
	
	 speedBullet = getPercent(density, 6);
	 
  	enemySpeed = getPercent(density, 1);

  	speed = getPercent(density, 3);	

  	speedBoccaleVuoto = getPercent(density, 1);
  
  	speedBottigliaPiena = getPercent(density, 8);
  	Log.d("ENEMY SPEED", ""+enemySpeed);
  	if(enemySpeed <= 1){
  	  	enemySpeed = getPercent(density, 2);

  	}
  	if(speedBoccaleVuoto <= 1){
  		speedBoccaleVuoto = getPercent(density, 2);
  	  		
  	}
  	
  	speedDobloni=speedBoccaleVuoto;
  	
}

private int speedBottigliaPiena;



public void decreasePunteggio(int p){
	if(p != 100 && p > 0){
		sound.playPikUpCoin();
	}
	if((punteggio  + p) > 0){

		punteggio = punteggio + p;

	}else{

		punteggio = 0;

	}

	

}



private int getPercent(int num,int perc){

	return num*perc/100;

}












	public void showPause(){
		if(state != PAUSE){
				lastState = state;
		}
		state=PAUSE;
	

	//dialog della classifica

		dlg = new Dialog(ctx);

		dlg.getWindow();

		LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		

		// LayoutInflater li = getActivity().getLayoutInflater();

		//View v = li.inflate(R.layout.dialog_classifica, null, false);

		View v = li.inflate(R.layout.pause_layout, null, false);
		
		TextView strPause = (TextView) v.findViewById(R.id.textViewPause);
		
		
		Button buttonExit = (Button) v.findViewById(R.id.buttonExit);
		
		buttonExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop();
	        	activity.onBackPressed();
			}
		});
		
		buttonExit.setTypeface(typeFace);
		
		

		Button buttonTutorial = (Button) v.findViewById(R.id.buttonTutorial);
		
		buttonTutorial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTutorial();
				statePause=3;
			}
		});
		
		buttonTutorial.setTypeface(typeFace);
		
		Button buttonMenu = (Button) v.findViewById(R.id.buttonMenu);

		buttonMenu.setOnClickListener(new OnClickListener() {

			

            @Override

            public void onClick(View v) {

            dlg.dismiss();

            refresh();

            statePause=1;

            }

		});

		
		buttonMenu.setTypeface(typeFace);
		strPause.setTypeface(typeFace);

		Button buttonResume = (Button) v.findViewById(R.id.buttonResume);

		buttonResume.setOnClickListener(new OnClickListener() {



            @Override

            public void onClick(View v) {

            dlg.dismiss();

           statePause=2;

            }

		});

		buttonResume.setTypeface(typeFace);
		
			dlg.setOnDismissListener(new OnDismissListener() {

				

				@Override

				public void onDismiss(DialogInterface dialog) {

					// TODO Auto-generated method stub

					if(statePause==1){

						state=MENU;


						activity.loopMusicReset();

						activity.loopMusicPlay();

					}else if(statePause==2){
						state=lastState;
						Log.d("MENU PAUSA", "torno nello stato: " + state);
					}else if(statePause==3){

					}else{
						state=lastState;
						Log.d("MENU PAUSA", "torno nello stato: " + state);

					}

					

				}

			});

		

			
			

			dlg.setContentView(v);

			

			dlg.show();

			/*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

		    lp.copyFrom(dlg.getWindow().getAttributes());

		    lp.width = WindowManager.LayoutParams.MATCH_PARENT;

		    lp.height = WindowManager.LayoutParams.MATCH_PARENT;

		    dlg.getWindow().setAttributes(lp);*/

			

			

	

		

		

		}///FINE SHOWPAUSE
	
	
	private Dialog dlgTutorial;
	
	public void showTutorial(){

		if(state != PAUSE){
			lastState = state;
	}
	state=PAUSE;

	//dialog della classifica

		dlgTutorial = new Dialog(ctx);

		dlgTutorial.getWindow();

		LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		dlgTutorial.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		String lang = Locale.getDefault().getDisplayLanguage();
		//Log.d("LANGUAGE",lang);
		View v = null;
		if(lang.equals("italiano")){
		v = li.inflate(R.layout.tutorial, null, false);
		}else{
			v = li.inflate(R.layout.tutorial_eng, null, false);
				
		}
		
	
		
			dlgTutorial.setOnDismissListener(new OnDismissListener() {

				

				@Override

				public void onDismiss(DialogInterface dialog) {

					// TODO Auto-generated method stub

						//showPause();
						//if(lastState==MENU)state=MENU;
							state=lastState;		

				}

			});

		

			
			

			dlgTutorial.setContentView(v);

			

			dlgTutorial.show();

			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

		    lp.copyFrom(dlgTutorial.getWindow().getAttributes());

		    lp.width = WindowManager.LayoutParams.MATCH_PARENT;

		    lp.height = WindowManager.LayoutParams.MATCH_PARENT;

		    dlgTutorial.getWindow().setAttributes(lp);
			
			

			

			

	

		

		

		}

	

	

	


	

	

	

	

	public int getPunteggio() {

		return punteggio;

	}

	public void setPunteggio(int punteggio) {

		this.punteggio = punteggio;

	}

	public int getLife() {

		return life;

	}

	public void setLife(int life) {

		this.life = life;

	}

	public LivelloBancone[] getLivelloBancone() {

		return livelloBancone;

	}

	public void setLivelloBancone(LivelloBancone[] livelloBancone) {

		this.livelloBancone = livelloBancone;

	}





	public void callGameOver(){

		//game over
		if(time==0){
			clessidra.setFlash(true);//faccio lampeggiare la clessidra
		}
		
		
		state=GAMEOVER;
		sound.playGameOver();
		timeGameOver=System.currentTimeMillis();

		//update del punteggio

		Utente utente = db.getAll().get(0);

		record = utente.getPunteggio();

		if(punteggio > utente.getPunteggio()){

			record = punteggio;

			utente.setPunteggio(punteggio);

		}

		

		db.update(utente);

		

		

		if(!(db.getAll().get(0).getNome().equals(server.LOCAL_NAME))){

			//il nome non è locale

			//devo aggiornare il db

			try {
				if(server.isLoaded()){
				server.addPunteggio(utente.getNome(),utente.getPunteggio(), utente.getEmail());
				}
			} catch (Exception e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			

		}

		

		

    db.close();

   punteggioGameOver = punteggio;

    //Log.d("PUNTEGGIO", "" + punteggioGameOver);

    //refresh();

	}

	

	

	public void increaseTime(int k){

		if(time + k <= 0){
			time = 0;
			callGameOver();

		}else{
			if(time + k >= 120){
				time = 120;
			}else{
				time = time + k;
			}
		}

	}

	

	
	private int speedDobloni;
	

	public void loadDati(){
		//Log.d("SERVER", "load dati");
		server.getMailFromDevice();
		
		if(db.getUtentiCount()>0){

			//ho un record

			if(db.getAll().get(0).getNome().equals(server.LOCAL_NAME)){

				//ho un record con un nome locale

				if(server.isOnline()){

					//se ho la connessione
					//Log.d("LOAD DATI", "recordOnline");
					server.recordOnline();

					

				}

				

			}else{

				try {
					if(server.isOnline()){

						//Log.d("LOAD DATI", "load utente");
					server.loadUtente();
					}
				} catch (InterruptedException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				} catch (ExecutionException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}

			}

			

			

			

		}else{

			

			//non ho record

			if(server.isOnline()){

				//se ho connessione

				//Log.d("LOAD DATI", "no record");
				server.noRecord();

			}else{

				//inserisco nel db locale

				
				server.checkTutorial();
			}

			

		}

			

			
		/*if(db.getUtentiCount()>0){
			Utente utente = db.getAll().get(0);//avrò solo un utente in locale
				if(utente.getFirst()==0){
					//è la prima volta che viene aperta la sessione di gioco
					 //Log.d("GROGCHALLENGE","fase di tutorial");
					 this.tutorialState=true;
					// utente.setFirst(1);
					// db.update(utente);
		  		 }
				
			}else if(db.getUtentiCount()==0){
			
					//è la prima volta che viene aperta la sessione di gioco
					//e ci sono stati problemi di connessione per cui non si è riuscito
					//a memorizzare ancora nulla
					 //Log.d("GROGCHALLENGE","fase di tutorial");
					 this.tutorialState=true;
								
			}*/
			

		

		

	}

	

	

	

	

	public void drawRun(Canvas c){
		if(this.sfondo != null){

			//sfondo

			Rect r = new Rect(0,0,this.getWidth(),this.getHeight());

			c.drawBitmap(sfondo, null,r,null);	

			

			

			

			//disegno la scena

			for(int i = this.numBanconi-1;i >= 0;i--){

				this.livelloBancone[i].doDraw(c);

			}

			

			this.dobloniPunteggio.doDraw(c);
			
			//disegno il punteggio
			Paint paint = new Paint();
			paint.setTypeface(typeFace);
	        paint.setColor(Color.WHITE); 

	        paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);

	        String strPunteggio = "" + punteggio;
	        
	        String str = "00000000";

	        int size = determineMaxTextSize(str, this.getWidth()/5);

	    
	        
	        paint.setTextSize(size);

	        Rect bounds = new Rect();

	        paint.getTextBounds(str, 0,str.length(), bounds);

	        c.drawText(strPunteggio, 0+dobloniPunteggio.getWidth(),bounds.height()+bounds.height()/5, paint);

	       
			if(clessidra.isFlash()){
				if(clessidra.isFlashActive()){
				       clessidra.doDraw(c);
			
				}
				clessidra.flashAnimation();
			}else{
			    clessidra.doDraw(c);
			
			}

	       //disegno il tempo
	        if(time < 11)paint.setColor(Color.RED);

			else if(time < 31)paint.setColor(Color.YELLOW);

	        String strTime = "" + time;

	         str = "00000000";

	      size = determineMaxTextSize(str, this.getWidth()/5);

	      
	        paint.setTextSize(size);

	       bounds = new Rect();

	        paint.getTextBounds(str, 0,str.length(), bounds);



	        
	        
	       c.drawText(strTime,clessidra.getX()+clessidra.getWidth(),bounds.height()+bounds.height()/5, paint);

	       

	       

			//disegno le vite

			//this.lifeDrawer.doDraw(c, life);

			

			

			/*if(looseLife != null){

				if(looseLife.isActive()){

					looseLife.doDraw(c);

				}

			}*/

			if(bonusP != null){

				if(bonusP.isActive()){

					bonusP.doDraw(c);

				}

			}

			

			this.pause.doDraw(c);

		}

		
	}

	
 private void runTutorial(){
	 
	 
	 long t = System.currentTimeMillis();
		if(stepTutorial == 0 && t-timeTutorial > 8000){
			stepTutorial++;
			tutorial.stepFrame();
		}
		if(stepTutorial ==2 || stepTutorial == 3 || stepTutorial == 4){
			LivelloBancone livello = livelloBancone[NUM_BANCONE_TUTORIAL];
			
			if(player.isRunning()){

				if(player.getX()>= 0 + player.getWidth()){

				int move = player.getX();

				player.setX(move-speed);

				}

			}else{

				player.setX(player.asse);

			}
			
			//nemici

			ArrayList<Enemy> nemici = livello.getEnemy();

			for(int j=0;j < nemici.size();j++){

				

				Enemy enemy = nemici.get(j);

				enemy.move(enemySpeed);
				//boccali
				
				ArrayList<AnimazioneScena> boccaliPieni2 = livello.getBoccaliPieni();

				for(int k=0;k < boccaliPieni2.size();k++){

					

					//controllo sulle collisioni tra i nemici e i boccali

					if(enemy.getShape().intersect(boccaliPieni2.get(k).getShape())){

							if(!enemy.isBevendo()){//se non sta già bevendo

								enemy.bevi();
								
								if(isTutorialState()){
									stepTutorial++;
									tutorial.stepFrame();
									timeStep = System.currentTimeMillis();
								}

								livello.removeBoccalePieno(k);

							}

					}

					

				}
				
				int x = enemy.getX();
				if(x <= 0){

					//sono riuscito a cacciare il nemico

					Point p = livello.getSpawnBoccali();

					AnimazioneScena porta = livello.getPorta();

					int xP = porta.getX() + porta.getWidth();

					int yP = porta.getY();

					livello.addPoint(10, xP, yP);

					livello.removeEnemy(j);//tolgo il nemico

					livello.addBoccaleVuoto(0, p.y);

					increaseTime(2);

					decreasePunteggio(10);

				}
				
				//nella fase di tutorial se il nemico arriva alla fine del bancone
				//lo tolgo e ne metto uno nuovo all'inizio
				if(enemy.getX() >= livello.getBancone().getX()+ livello.getBancone().getWidth()-enemy.getWidth()){
					nemici.remove(j);
					livello.addEnemy();
				}

			}
			
			//muovo la bottiglia che torna
			
			ArrayList<AnimazioneScena> bottiglieVuote = livello.getBoccaliVuoti();
			for (int i = 0; i < bottiglieVuote.size(); i++) {
				AnimazioneScena bottigliaVuota = bottiglieVuote.get(i);
				int x = bottigliaVuota.getX();

				bottigliaVuota.setX(x + speedBoccaleVuoto);

				//se il player nella fase di tutorial prende la bottiglia vuota
				if(bottigliaVuota.getShape().intersect(player.getShape())){
					bottiglieVuote.remove(i);
					if(stepTutorial==3){
						stepTutorial=5;
						tutorial.stepFrame();
						tutorial.stepFrame();
						
					}else{
					stepTutorial++;
					tutorial.stepFrame();
					}
				}
				
			}
			

			//boccali pieni
			ArrayList<AnimazioneScena> boccaliPieni = livello.getBoccaliPieni();

			for(int j=0;j < boccaliPieni.size();j++){

				AnimazioneScena boccale = boccaliPieni.get(j);

				int x =boccale.getX();

				boccale.setX(x - speedBottigliaPiena);

				AnimazioneScena porta= livello.getPorta();

				if(boccale.getX() <=  porta.getX() + porta.getWidth()){

					//il boccale è uscito fuori dalla porta

					livello.removeBoccalePieno(j);

					int yP = porta.getY();

					int xP = porta.getX() + porta.getWidth();

					livello.addPoint(-10, xP, yP);

					decreasePunteggio(-10);

					increaseTime(-10);

					sound.playBottigliaRotta();

					
					

					livello.addBoccaleRotto(xP + boccale.getWidth(),livello.getBancone().getY());

				}

				}
				
			ArrayList<PointFeedBack> point = livello.getPunteggi();

			for (int l =0; l< point.size();l++) {

				PointFeedBack p = point.get(l);

				int y = p.getY();

				p.setY(y-4);

				if(p.getY() <= 0){

					livello.removePoint(l);

				}

			}
			
		}else{
			muovi();
		}
		
		t = System.currentTimeMillis();
		if(stepTutorial==3 && t-timeStep > 8000){
			stepTutorial++;
			tutorial.stepFrame();
			
			
		}
		if(stepTutorial == 5){
			refresh();
		}
		
		if(System.currentTimeMillis() - animTime > 700){

			 animTime = System.currentTimeMillis();		

			 //aggiorna le animazioni ogni 700 millisecondi

			frecciaTutorial.stepFrame();
			

			}
		
		
		
	 
	 
 }
	

	

}



