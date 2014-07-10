package it.unisa.grogchallenge;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

public class BitmapResizer {

	private SurfaceView surfaceview;
	private int ratio = -1;	
	private int scaleH = -1;
	private int scaleW = -1;
	
	public BitmapResizer(SurfaceView s){
		this.surfaceview=s;
	}
	
	
	
	 public  int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    	//System.out.println("RRRRRRRRRRRRRRRRR " + reqHeight + " -  " + reqWidth);
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	   // System.out.println("AAAAAAAAAAAAAAA " + height + " -  " + width);
	  /*  if (height > reqHeight || width > reqWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	        	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	        	System.out.println("AAAAAAAAAAAAAAA " + inSampleSize);
	            inSampleSize *= 2;
	        }
	    }*/
	   
	    
	    if (height > reqHeight || width > reqWidth) {
	    	 
	        // Calculate ratios of height and width to requested height and
	        // width
	        final int heightRatio = Math.round((float) height
	                / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will
	        // guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = (heightRatio < widthRatio) ? heightRatio : widthRatio;
	    }
	    this.ratio = inSampleSize;
	    return inSampleSize;
	}

		
		
		public  Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
		        int reqWidth, int reqHeight) {

		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeResource(res, resId, options);

		    // Calculate inSampleSize
		    if(ratio == -1){
		   options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		    }else{
		    	options.inSampleSize=this.ratio;
		    }
		    
		    
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		
		   
		   return BitmapFactory.decodeResource(res, resId, options);
			
		}
		
		
	
		
		public  Bitmap decodeSampledBitmapFromResource(Resources res, int resId) {

		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeResource(res, resId, options);

		    // Calculate inSampleSize
		    if(ratio == -1){
		    	return null;
		    }else{
		    	options.inSampleSize=this.ratio;
		    }
		    
		    
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		  
		    return BitmapFactory.decodeResource(res, resId, options);
			 
		   
		}
		
		
		
	
		
	public SurfaceView getSurfaceview() {
		return surfaceview;
	}

	public void setSurfaceview(SurfaceView surfaceview) {
		this.surfaceview = surfaceview;
	}

	
	public void setRatio(int ratio){
		this.ratio=ratio;
	}
	
}
