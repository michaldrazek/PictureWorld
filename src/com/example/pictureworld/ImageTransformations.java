package com.example.pictureworld;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageTransformations {
	private static Bitmap applyMatrix(Bitmap bitmap, ColorMatrix matrix){
		Paint paint = new Paint();
		// kopia wejœciowej bitmapy
		Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
		//ustawienie przekszta³cen kolorystycznych na obiekcie paint
		paint.setColorFilter(new ColorMatrixColorFilter(matrix));
		//canvas z obrabian¹ bitmap¹
		Canvas canvas = new Canvas(bmp);
		//kluczowa funkcja rysujaca zmiany na bitmapie
		canvas.drawBitmap(bitmap, 0, 0, paint);
		//
		return bmp;
	}
	
	public static Bitmap resetToNormal(Bitmap bitmap){
		float[] normal_tab = {
			1.0f, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0
		};
	
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(normal_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
		
	public static Bitmap negative(Bitmap bitmap){
		float[] neg_tab = { 
			-1, 0, 0, 1, 0,
	        0, -1, 0, 1, 0,
	        0, 0, -1, 1, 0,
	        0, 0, 0, 1, 0
	        };
		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(neg_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap redFilter(Bitmap bitmap){
		float[] red_tab = { 
			    2, 0, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 0, 1, 0
            };
		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(red_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap blueFilter(Bitmap bitmap){
		float[] blue_tab = { 
			    0, 0, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 2, 0, 0,
		        0, 0, 0, 1, 0
            };
		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(blue_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap greenFilter(Bitmap bitmap){
		float[] green_tab = { 
			    0, 0, 0, 0, 0,
		        0, 2, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 0, 1, 0
            };
		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(green_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap cyanFilter(Bitmap bitmap){
		float[] cyan_tab = { 
			    0, 0, 0, 0, 0,
		        0, 2, 0, 0, 0,
		        0, 0, 2, 0, 0,
		        0, 0, 0, 1, 0
            };
		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(cyan_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap yellowFilter(Bitmap bitmap){
		float[] yellow_tab = { 
			    2, 0, 0, 0, 0,
		        0, 2, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 0, 1, 0
            };
		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(yellow_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap magentaFilter(Bitmap bitmap){
		float[] magenta_tab = { 
			    2, 0, 0, 0, 0,
		        0, 0, 0, 0, 0,
		        0, 0, 2, 0, 0,
		        0, 0, 0, 1, 0
            };
		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(magenta_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap setAll(Bitmap bitmap, int brightness, int contrast, int saturation){
		float scale = (float) (contrast)/255;
	    float translate = (-.5f * scale + .5f) * 255.f;
	    float value = (float) (saturation)/100;
	    float[] array = new float[] {
	        scale, 0, 0, 0, translate,
	        0, scale, 0, 0, translate,
	        0, 0, scale, 0, translate,
	        0, 0, 0, 1, 0};

		
		float[] brightness_tab = {     
		        1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0 };

		ColorMatrix brightnessMatrix = new ColorMatrix();	
		brightnessMatrix.set(brightness_tab);
		
		ColorMatrix contrastMatrix = new ColorMatrix(array);	

		ColorMatrix concatedMatrix = new ColorMatrix();
		concatedMatrix.setConcat(contrastMatrix, brightnessMatrix);
		
		ColorMatrix saturationMatrix = new ColorMatrix();
	    saturationMatrix.setSaturation(value);
	    
	    ColorMatrix concatted = new ColorMatrix();
	    concatted.setConcat(concatedMatrix, saturationMatrix);
	    
	    return applyMatrix(bitmap, concatted);
	}
	
	public static Bitmap setBrightness(Bitmap bitmap, int brightness){
		float[] bightness_tab = {     
		        1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0 };

		
		ColorMatrix cMatrix = new ColorMatrix();		
		cMatrix.set(bightness_tab);
		
		return applyMatrix(bitmap, cMatrix);
	}
	
	public static Bitmap setContrast(Bitmap bitmap, int contrast){
	    float scale = (float) (contrast)/255;
	    float translate = (-.5f * scale + .5f) * 255.f;
	    float[] array = new float[] {
	        scale, 0, 0, 0, translate,
	        0, scale, 0, 0, translate,
	        0, 0, scale, 0, translate,
	        0, 0, 0, 1, 0};
	    ColorMatrix matrix = new ColorMatrix(array);
	    
	    return applyMatrix(bitmap, matrix);
	}
	
	public static Bitmap setSaturation(Bitmap bitmap, int saturation){
	    float value = (float) (saturation)/100;
	    float[] array = new float[] {
	        1, 0, 0, 0, 0,
	        0, 1, 0, 0, 0,
	        0, 0, 1, 0, 0,
	        0, 0, 0, 1, 0};
	    ColorMatrix matrix = new ColorMatrix(array);
	    matrix.setSaturation(value);

	    return applyMatrix(bitmap, matrix);    
	}
}
