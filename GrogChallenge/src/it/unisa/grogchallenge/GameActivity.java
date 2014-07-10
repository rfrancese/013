package it.unisa.grogchallenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class GameActivity extends Activity implements OnCompletionListener{
	
	private DBGrogChallenge db;
	SessionGame game;
	
	public ConnectJsonUtil server;
	private static final String URL="http://www.rustygears.net/GrogServer/GROG_CHALLENGE/GrogServer.php";
	//private static final String URL="http://192.168.1.21/GrogServer/GROG_CHALLENGE/GrogServer.php";

	
	//
	private int mediaState = 0;
	//
	private MediaPlayer mediaPlayer;
	//
	private int currentTime;
	//
	private int musicMaxTime;
	//
	private int currentVol;
	//?
	private int setTime = 5000;
	//
	private AudioManager am;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_game);
		this.db=new DBGrogChallenge(this);
		this.game =(SessionGame) this.findViewById(R.id.sessionGame1);
		this.game.setActivity(this);
		this.server = new ConnectJsonUtil(GameActivity.this, URL,this.db,game);
		game.setConnectJson(server);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mediaPlayer = MediaPlayer.create(this, R.raw.pirate_loop);
		//
		mediaPlayer.setLooping(true);	
		//
		musicMaxTime = mediaPlayer.getDuration();
		//
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//
		//setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//
		
		currentVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(
	            AudioManager.STREAM_MUSIC,
	            currentVol,
	            0);
		mediaPlayer.setOnCompletionListener(this);
		 
		
			
		
		
		game.setDb(db);
     
		
	}

	 @Override
	    public void onConfigurationChanged(Configuration newConfig) 
	    {
		 	
	       super.onConfigurationChanged(newConfig);
	       
	       
	    }
	
	
	
	 @Override
	    protected void onResume() {
		 	super.onResume();
	        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        Log.d("GROGCHALLENGE", "onResume");
	        game.resume();
	        
	 }
	 @Override
	    protected void onDestroy() {//l'app è distrutta
		 
	        super.onDestroy();
	        if(mediaPlayer != null){
		        if(mediaPlayer.isPlaying()){  
		            mediaPlayer.stop();  
		        }  
	        }
	        this.game.stop();
	    }
	 
	 
	 
	 @Override
	    protected void onPause() {
	        super.onPause();
	        this.loopMusicPause();
	        Log.d("GROGCHALLENGE", "onPause");
	       
	        this.game.pause();
	        
	 	}
	 
	 
	 

	 
	 
	 
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event){
		 if(keyCode==KeyEvent.KEYCODE_BACK){
			  
			  	if(game.getState()==game.CLASSIFICA){
			  		game.setState(game.MENU);
			  	}else if(game.getState()==game.RUN){
			  		game.showPause();
			  	}else{
				  newBackDialog();
			  	}
			  
			  
		  }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){  
		    	Log.d("VOLUME", "+1");
		    	
		        am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol + 1, AudioManager.FLAG_PLAY_SOUND);
		        currentVol++;
		    //  
		    } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){  
		    	Log.d("VOLUME", "-1");
		    	am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol - 1, AudioManager.FLAG_PLAY_SOUND);  
		    	currentVol--;
		    	//  
		    }
		 	//return false lascia propagare l'evento
		 	return false;

	      
	   }
	 

	 
		public void newBackDialog(){
			game.pause();
			
			 // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Are you sure you want to exit?")
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                		game.stop();
	        	        	onBackPressed();
	                   }
	               })
	               .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                	   game.resume();
	                   }
	               }).setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						game.resume();
					}
				});
	           	   
	        // Create the AlertDialog object and return it
	         builder.create();
	         builder.show();
			
		}
	 
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			if(mediaPlayer == mp){  
		        Log.v("wwj", "Play completed");  
		    }  
			
		}
		
		
		public void loopMusicPlay(){
			if(mediaPlayer!=null){
				if(!mediaPlayer.isPlaying())
				mediaPlayer.start();
			}
		}
		
		public void loopMusicStop(){
			if(mediaPlayer!=null){
				
			if(mediaPlayer.isPlaying())
				mediaPlayer.stop();
			
			}
		}
		
		public void loopMusicPause(){
			if(mediaPlayer!=null){
				
			if(mediaPlayer.isPlaying())
				mediaPlayer.pause();
			}
		}
		
		public void loopMusicReset(){
			if(mediaPlayer!=null){
					
				mediaPlayer.reset();
				AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.pirate_loop);
				try {
					mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
					mediaPlayer.prepare();
					afd.close();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		@Override
		public void onBackPressed() {
		    super.onBackPressed();   
		    //    finish();

		} 

		
		
}
