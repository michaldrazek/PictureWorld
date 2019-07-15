package com.example.pictureworld;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextPickerActivity extends Activity {
	private String typefaceName;
	private TextPreview textprev;
	private EditText textEdit;
	private LinearLayout toNaFonty;
	private RelativeLayout preview;
	private int statusBarHeight;
	private int strokeColor = R.color.black;
	private int fillColor = R.color.black;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_text_picker);
		statusBarHeight = getStatusBarHeight();
		preview = (RelativeLayout) findViewById(R.id.text_preview_layout);
		toNaFonty = (LinearLayout) findViewById(R.id.font_layout);
		textEdit = (EditText) findViewById(R.id.edit_text);
		AssetManager assetManager = getAssets();
		String[] lista = null;
		
		
		findViewById(R.id.textPickerStrokeIV).setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				makeNewColorPickerInstance(true);
			}
		});
		
		findViewById(R.id.textPickerFillIV).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				makeNewColorPickerInstance(false);	
			}
		});
		
		findViewById(R.id.textPickerConfirmIV).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO dodac napis do zdjecia
				Intent intent = new Intent();
				if(typefaceName==null){
					intent.putExtra("typeface", "TT0131I_.TTF");
				} else {
					intent.putExtra("typeface", typefaceName);
				}
				
				intent.putExtra("fontSize", textprev.getFontSize());
				intent.putExtra("fillColor", textprev.getFillColor());
				intent.putExtra("strokeColor", textprev.getStrokeColor());
				intent.putExtra("text", textprev.getText());
			    setResult(111, intent);        
				finish();
			}
		});
		
		try {
			lista = assetManager.list("fonts");
		} catch (IOException e) {
			e.printStackTrace();
		} // fonts to nazwa podfolderu w assets
		if(lista != null){
			for(int i=0; i<lista.length; i++){
				TextView temp = new TextView(TextPickerActivity.this);
				temp.setText("za¿ó³æ gêœl¹ jaŸñ");
				final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/"+lista[i]);
				final String tempName = lista[i];
				temp.setTypeface(tf);
				temp.setTextSize(30);
				//temp.setHeight(50);
				toNaFonty.addView(temp);
				temp.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						typefaceName = tempName;
						preview.removeAllViews();
						textprev.setTypeface(tf);	
						preview.addView(textprev);
					
					}
				});
			}
			TextWatcher textWatcher = new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					textprev.setText(s.toString());
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// 
					preview.removeAllViews();
					preview.addView(textprev);
				}
			};
			textprev = new TextPreview(TextPickerActivity.this, 0, statusBarHeight, preview.getHeight(), textEdit.getText().toString(), Typeface.createFromAsset(getAssets(), "fonts/"+lista[0]), strokeColor, fillColor);
			preview.addView(textprev);
			textEdit.addTextChangedListener(textWatcher);
		}

		
	}

    public void setColors(int color, boolean type){
    	if(type){
    		strokeColor = color;
    		textprev.setStrokeColor(color);
    	}else{
    		fillColor = color;
    		textprev.setFillColor(color);
    	}
    	preview.removeAllViews();
		preview.addView(textprev);
    }
	
	private void makeNewColorPickerInstance(boolean type){
		Dialog builder = new Dialog(TextPickerActivity.this);
	    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    builder.getWindow().setBackgroundDrawable(
	        new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    
	    ColorPicker cp = new ColorPicker(TextPickerActivity.this, builder, type);
	    
	    builder.addContentView(cp, new RelativeLayout.LayoutParams(
	            ViewGroup.LayoutParams.MATCH_PARENT, 
	            ViewGroup.LayoutParams.MATCH_PARENT));   
	    
	    builder.show();	
	}
	
	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	} 
	
	
}
