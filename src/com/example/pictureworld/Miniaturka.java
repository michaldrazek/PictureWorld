package com.example.pictureworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Miniaturka extends ImageView {
	Context thisContext;
	final Paint paint;
	final int size;
	final Bitmap miniatura;
    final byte[] fullPicture; 
	
	public Miniaturka(Context context, byte[] dane, int size) {
		super(context);
		// 
		thisContext = context;
		this.size = size;
		this.fullPicture = dane;
		miniatura = ImageUtils.scaleBitmap(ImageUtils.decodeRotateAndCreateBitmap(fullPicture), size, size);
		//this.setScaleType(ImageView.ScaleType.CENTER_CROP);
		this.setImageBitmap(miniatura);
		LinearLayout.LayoutParams imageStyle = new LinearLayout.LayoutParams(size, size);
		imageStyle.topMargin = 5;
		imageStyle.leftMargin = 5;
		this.setLayoutParams(imageStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setColor(Color.argb(255, 255, 255, 255));	
		
	}

	public void setPosition(int x, int y) {
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// 
		super.onDraw(canvas);	
		canvas.drawRect( 0,  0, size , size , paint);		
	}

}