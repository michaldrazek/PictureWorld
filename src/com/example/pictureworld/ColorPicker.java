package com.example.pictureworld;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ColorPicker extends RelativeLayout {
	private ImageView iv;
	private Bitmap bmp;
	private int kolorek;
	private boolean type;
	
	public ColorPicker(final Context context, final Dialog instance, final boolean type) {//true - stroke; false - outline
		super(context);
		// 
		this.type = type;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		final int screenSize = size.x<size.y?size.x:size.y;
		LayoutParams heh = new LayoutParams(screenSize, screenSize);
		LayoutParams k = new LayoutParams(screenSize/5, screenSize/5);
		LayoutParams k2 = new LayoutParams(screenSize/3, screenSize/3);
		iv = new ImageView(context);
		iv.setId(100000000);
		iv.setLayoutParams(heh);
		this.addView(iv);
		iv.setImageResource(R.drawable.color_wheel);
		iv.setDrawingCacheEnabled(true);
		LayoutParams par1 = new LayoutParams(k);
		LayoutParams par2 = new LayoutParams(k);
		LayoutParams par3 = new LayoutParams(k2);
		ImageView confirm = new ImageView(context);
		confirm.setImageResource(R.drawable.confirm_white);	
		par1.addRule(ALIGN_PARENT_RIGHT);
		par1.addRule(BELOW, iv.getId());
		par1.topMargin = 50;
		confirm.setLayoutParams(par1);
		ImageView abort = new ImageView(context);
		abort.setImageResource(R.drawable.abort_white);
		par2.addRule(ALIGN_PARENT_LEFT);
		par2.topMargin = 50;
		par2.addRule(RelativeLayout.BELOW, iv.getId());
		abort.setLayoutParams(par2);
		final ImageView colPrev = new ImageView(context);
		par3.addRule(CENTER_HORIZONTAL);
		par3.addRule(RelativeLayout.BELOW, iv.getId());
		colPrev.setLayoutParams(par3);
			
		confirm.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {				
				((TextPickerActivity) context).setColors(kolorek, type);
				instance.cancel();
			}
		});
		
		abort.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				instance.cancel();
			}
		});
		
		this.addView(confirm);
		this.addView(colPrev);
		this.addView(abort);
		
		iv.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 //wykrycie odpowiedniego eventa (down, up, move)
				v.performClick();
		        switch(event.getAction()){                
		                    
		            case MotionEvent.ACTION_MOVE:
		                //pobranie Bitmapy z obrazka:
		                bmp = v.getDrawingCache();
		                
		                //aby nie przekroczyc x i y bitmapy
		                if((int) event.getX() < 0 || (int) event.getY() < 0 ||(int) event.getX() >= bmp.getWidth() || (int) event.getY() >= bmp.getHeight()){
		                	return false;
		                }
		                //pobranie koloru piksela z odpowiedniego miejsca bitmapy:
		                if(bmp.getPixel((int) event.getX(), (int) event.getY())!=0){
		                	 kolorek = bmp.getPixel((int) event.getX(), (int) event.getY());
				                //tu sprawdzaj i ustawiaj na bie¿¹co podgl¹d koloru:
				                colPrev.setBackgroundColor(kolorek);
		                }
		                break;

		            case MotionEvent.ACTION_DOWN:
		                break;
		            case MotionEvent.ACTION_UP:                    
		                break;
		        }                
				return true;
			}
			
		});
		
		
	}
	
	public int getColor(){
		return kolorek;
	}
}
