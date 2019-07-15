package com.example.pictureworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class TextPreview extends View {
	private Paint paint;
	private int posY;
	private int posX;
	private String text;
	private int strokeColor;
	private int fillColor;
	private Typeface tf;
	private int fontSize;
	
	public TextPreview(Context context, int posX, int posY, int fontSize, String text, Typeface tf, int strokeColor, int fillColor) {
		super(context);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//paint.reset();            
		paint.setAntiAlias(true); 
		paint.setTextSize(100);       
		paint.setTypeface(tf);
		this.fontSize = 100;
		this.tf = tf;
		this.posY = posY; 
		this.posX = posX;
		this.text = text;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
	}

	public void setTypeface(Typeface tf){
		paint.setTypeface(tf);
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public void setStrokeColor(int color){
		strokeColor = color;
	}
	
	public void setFillColor(int color){
		fillColor = color;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(fillColor);
		canvas.drawText(text, posX, posY+20, paint);

		//teraz krawêdŸ, po wype³nieniu aby by³a ponad nim, rysuje ten sam napis
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setColor(strokeColor);
		canvas.drawText(text, posX, posY+20, paint);
	}
	
	public Typeface getTypeface(){
		return tf;
	}
	
	public int getStrokeColor(){
		return strokeColor;
	}
	
	public int getFillColor(){
		return fillColor;
	}
	
	public String getText(){
		return text;
	}
	
	public int getFontSize(){
		return fontSize;
	}
	
}
