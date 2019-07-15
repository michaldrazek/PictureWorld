package com.example.pictureworld;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtils {
	public static Bitmap scaleBitmap(Bitmap bitmap, int width, int height){
		return Bitmap.createScaledBitmap(bitmap, width, height,true);
	}
	
	public static Bitmap decodeBitmap(byte[] array){
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}
	
	public static Bitmap decodeRotateAndCreateBitmap (byte[] temp){
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		if(decodeBitmap(temp)!=null){
			return Bitmap.createBitmap(decodeBitmap(temp), 0, 0, decodeBitmap(temp).getWidth(), decodeBitmap(temp).getHeight(), matrix, true);		
		} else {
			return decodeBitmap(temp);
		}
	}
	
	public static Bitmap rotateBitmap (Bitmap bitmap){
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);			
	}
	
	public static Bitmap flipBitmapVertically (Bitmap bitmap){
		Matrix matrix = new Matrix();
		matrix.setScale(1,-1);
		matrix.postTranslate(bitmap.getWidth(),0);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);			
	}
	
	public static Bitmap flipBitmapHorizontally (Bitmap bitmap){
		Matrix matrix = new Matrix();
		matrix.setScale(-1,1);
		matrix.postTranslate(bitmap.getWidth(),0);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);					
	}
	
	public static Bitmap decodeImageFromPath(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekszta³cania bitmapy
        //options.inPurgeable = true;
        //options.outHeight = 100;
        // options.outWidth = 100;
        options.inSampleSize = 4; // zmniejszenie jakoœci bitmapy
        File imgFile = new File(filePath);
        return BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options); 
	}
	
	public static byte[] convertBitmapToByteArray(Bitmap b){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
	public static Bitmap scaleBitmapToFit(Bitmap bmp, int width, int height){			
		float ratio =  (float) bmp.getWidth() / (float) bmp.getHeight(); // proporcje
		float newHeight;
		float newWidth;
		
        if(ratio < 1.0f){
        	newHeight = (float) height;
        	newWidth = (float) height * ratio;
        	
        }  else {
        	newHeight = (float) width / ratio;
        	newWidth = (float) width;
        }
        
        return Bitmap.createScaledBitmap(bmp, (int) newWidth,(int) newHeight, true);
}
	
	public static Bitmap scaleDrawableToFit(Drawable drawable, int width, int height){
			Bitmap bmp = drawableToBitmap(drawable);
				
			float ratio =  (float) bmp.getWidth() / (float) bmp.getHeight(); // proporcje
			float newHeight;
			float newWidth;
			
	        if(ratio < 1.0f){
	        	newHeight = (float) height;
	        	newWidth = (float) height * ratio;
	        	
	        }  else {
	        	newHeight = (float) width / ratio;
	        	newWidth = (float) width;
	        }
	        
	        return Bitmap.createScaledBitmap(bmp, (int) newWidth,(int) newHeight, true);
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    Bitmap bitmap = null;

	    if (drawable instanceof BitmapDrawable) {
	        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
	        if(bitmapDrawable.getBitmap() != null) {
	            return bitmapDrawable.getBitmap();
	        }
	    }

	    if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
	        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
	    } else {
	        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
	    }

	    Canvas canvas = new Canvas(bitmap);
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);
	    return bitmap;
	}
}
