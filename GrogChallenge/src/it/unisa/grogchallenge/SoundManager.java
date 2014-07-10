package it.unisa.grogchallenge;



import android.content.Context;

import android.media.AudioManager;

import android.media.SoundPool;



public class SoundManager {

	/*
	 * int play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
	Role: music player
	The first parameter: loaded music file ID
	The second parameter: the volume of the left channel, range: 0.0 to 1.0
	The third parameter: the volume of the right channel, the same range
	The fourth parameter: the music streaming priority, 0 is the lowest priority
	The fifth parameter: the number of music playback, -1: an infinite loop, 0: normal, greater than 0: indicates the number of cycles.
	Sixth parameter: Playback speed. Range: 0.5 to 2.0, 1.0 to normal playback
	 * 
	 * 
	 */
	
	private static final int RATE = 1;
	private static final int LOOP = 0;
	private static final int PRIORITY = 1;
	private static final int VOLUME_LEFT=2;
	private static final int VOLUME_RIGHT=2;
	
	//suoni

		 //SoundPool  

	   private SoundPool sp;  

	   //id  
	   
	   
	   private int soundId_gameOver;

	   private int soundId_bottigliaRotta;

	   private int soundId_lancioBottiglia;

	   private int soundId_esceCento;

	   private int soundId_arr;

	   private int soundId_pikUpCoin;
	   
	   private int soundId_jump;
	   private int soundId_explosion;
	   

	   public SoundManager(Context context){

		   

		 //init suoni

			//SoundPool  

	        sp = new SoundPool(10,AudioManager.STREAM_MUSIC,100);  

	        //ID 
	        
	        soundId_jump = sp.load(context,R.raw.jump,PRIORITY);

	        soundId_bottigliaRotta = sp.load(context, R.raw.bottiglia_rotta, PRIORITY);  

	       soundId_lancioBottiglia= sp.load(context, R.raw.lancio_bottiglia, PRIORITY);

	 	   soundId_esceCento= sp.load(context, R.raw.esce_cento, PRIORITY);

	 	   soundId_arr= sp.load(context, R.raw.arr, PRIORITY);

		   soundId_pikUpCoin=sp.load(context, R.raw.pickup_coin, PRIORITY);
		   soundId_explosion =sp.load(context, R.raw.explosion, PRIORITY);
		   
		   soundId_gameOver=sp.load(context, R.raw.game_over, PRIORITY);
	   }

	   public void playExplosion(){
		   sp.play(soundId_explosion,VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);

	   }
	   
	   public void playGameOver(){

		   sp.play(soundId_gameOver,VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);

	   }

	   
	   public void playBottigliaRotta(){

		   sp.play(soundId_bottigliaRotta,VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);

	   }

	   

	   public void playArr(){

		   sp.play(soundId_arr, VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);

		}

	   

	   

	   public void playEsceCento(){

		   sp.play(soundId_esceCento, VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);

			  

	   }

	   

	   public void playLancioBottiglia(){

		   sp.play(soundId_lancioBottiglia, VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);

	   }

	   
	   public void playPikUpCoin(){
		   sp.play(soundId_pikUpCoin, VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);
	   }
	   
	   public void playJump(){
		   sp.play(soundId_jump,VOLUME_LEFT,VOLUME_RIGHT,PRIORITY,LOOP,RATE);
	   }
	   

}