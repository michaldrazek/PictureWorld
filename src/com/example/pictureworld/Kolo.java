package com.example.pictureworld;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class Kolo extends View {
	Context thisContext;
	
	public Kolo(Context context) {
		super(context);
		// 
		thisContext = context;
	}

	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// 
		super.onDraw(canvas);
		//
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) thisContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int x = displaymetrics.widthPixels / 2;	     
        int y = displaymetrics.heightPixels / 2; 
        int r = (int)(displaymetrics.widthPixels * 0.35);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(10);
		paint.setColor(Color.argb(150, 255, 255, 255));
		canvas.drawCircle(x, y, r, paint);
	}

	
}
