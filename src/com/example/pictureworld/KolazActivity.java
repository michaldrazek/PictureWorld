package com.example.pictureworld;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.support.v4.app.NavUtils;
import android.content.Intent;

public class KolazActivity extends Activity {
	private RelativeLayout layout1;
	private RelativeLayout layout2;
	private RelativeLayout layout3;
	private RelativeLayout layout4;
	private RelativeLayout layout5;
	private RelativeLayout layout6;
	private int statusBarHeight;
	private DisplayMetrics displaymetrics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kolaz);
        displaymetrics = new DisplayMetrics();
		(KolazActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);	
        statusBarHeight = getStatusBarHeight();
        layout1 = (RelativeLayout) findViewById(R.id.lay01);
        layout2 = (RelativeLayout) findViewById(R.id.lay02);
        layout3 = (RelativeLayout) findViewById(R.id.lay03);
        layout4 = (RelativeLayout) findViewById(R.id.lay04);
        layout5 = (RelativeLayout) findViewById(R.id.lay05);
        layout6 = (RelativeLayout) findViewById(R.id.lay06);
      //lay01
        layout1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, Integer>> lista = new ArrayList<HashMap<String,Integer>>();

				// hashmap - para klucz - wartoœæ (nale¿y powtórzyæ tyle razy ile jest elementów kola¿u - czyli obrazków):		
				int szerX = displaymetrics.widthPixels;	     
		        int szerY = displaymetrics.heightPixels - statusBarHeight; 
		        HashMap<String,Integer> mapa01 = new HashMap<String, Integer>();
				mapa01.put("x",0);
				mapa01.put("y",0);
				mapa01.put("width", szerX/2);
				mapa01.put("height", szerY);
				mapa01.put("position", 1);
				lista.add(mapa01);
				HashMap<String,Integer> mapa02 = new HashMap<String, Integer>();
				mapa02.put("x",szerX/2);
				mapa02.put("y",0);
				mapa02.put("width", szerX/2);
				mapa02.put("height", szerY);
				mapa02.put("position", 2);
				lista.add(mapa02);
				//przes³anie listy do drugiej aktywnoœci
				
				Intent pickerIntent = new Intent(
						KolazActivity.this,	
						PickerActivity.class						
						);	
				pickerIntent.putExtra("lista", lista);
				pickerIntent.putExtra("size", lista.size());
				pickerIntent.putExtra("type", 1);
				startActivity(pickerIntent);
			}
		});
        //lay02
        layout2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, Integer>> lista = new ArrayList<HashMap<String,Integer>>();

				// hashmap - para klucz - wartoœæ (nale¿y powtórzyæ tyle razy ile jest elementów kola¿u - czyli obrazków):		
				int szerX = displaymetrics.widthPixels;	     
		        int szerY = displaymetrics.heightPixels - statusBarHeight; 
		        HashMap<String,Integer> mapa01 = new HashMap<String, Integer>();
				mapa01.put("x",0);
				mapa01.put("y",0);
				mapa01.put("width", szerX);
				mapa01.put("height", szerY/2);
				mapa01.put("position", 1);
				lista.add(mapa01);
				HashMap<String,Integer> mapa02 = new HashMap<String, Integer>();
				mapa02.put("x",0);
				mapa02.put("y",szerY/2);
				mapa02.put("width", szerX);
				mapa02.put("height", szerY/2);
				mapa02.put("position", 2);
				lista.add(mapa02);
				//przes³anie listy do drugiej aktywnoœci
				
				Intent pickerIntent = new Intent(
						KolazActivity.this,	
						PickerActivity.class						
						);	
				pickerIntent.putExtra("lista", lista);
				pickerIntent.putExtra("size", lista.size());
				pickerIntent.putExtra("type", 2);
				startActivity(pickerIntent);
			}
		});
        //lay03
        layout3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, Integer>> lista = new ArrayList<HashMap<String,Integer>>();

				// hashmap - para klucz - wartoœæ (nale¿y powtórzyæ tyle razy ile jest elementów kola¿u - czyli obrazków):		
				int szerX = displaymetrics.widthPixels;	     
		        int szerY = displaymetrics.heightPixels - statusBarHeight; 
		        HashMap<String,Integer> mapa01 = new HashMap<String, Integer>();
				mapa01.put("x",0);
				mapa01.put("y",0);
				mapa01.put("width", szerX);
				mapa01.put("height", szerY/3*2);
				mapa01.put("position", 1);
				lista.add(mapa01);
				HashMap<String,Integer> mapa02 = new HashMap<String, Integer>();
				mapa02.put("x",0);
				mapa02.put("y",szerY/3*2);
				mapa02.put("width", szerX/2);
				mapa02.put("height", szerY/3);
				mapa02.put("position", 2);
				lista.add(mapa02);
				HashMap<String,Integer> mapa03 = new HashMap<String, Integer>();
				mapa03.put("x",szerX/2);
				mapa03.put("y",szerY/3*2);
				mapa03.put("width", szerX/2);
				mapa03.put("height", szerY/3);
				mapa03.put("position", 3);
				lista.add(mapa03);
				//przes³anie listy do drugiej aktywnoœci
				
				Intent pickerIntent = new Intent(
						KolazActivity.this,	
						PickerActivity.class						
						);	
				pickerIntent.putExtra("lista", lista);
				pickerIntent.putExtra("size", lista.size());
				pickerIntent.putExtra("type", 3);
				startActivity(pickerIntent);
			}
		});
        //lay04
        layout4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, Integer>> lista = new ArrayList<HashMap<String,Integer>>();

				// hashmap - para klucz - wartoœæ (nale¿y powtórzyæ tyle razy ile jest elementów kola¿u - czyli obrazków):		
				int szerX = displaymetrics.widthPixels;	     
		        int szerY = displaymetrics.heightPixels - statusBarHeight; 
		        HashMap<String,Integer> mapa01 = new HashMap<String, Integer>();
				mapa01.put("x",0);
				mapa01.put("y",0);
				mapa01.put("width", szerX);
				mapa01.put("height", szerY/3);
				mapa01.put("position", 1);
				lista.add(mapa01);
				HashMap<String,Integer> mapa02 = new HashMap<String, Integer>();
				mapa02.put("x",0);
				mapa02.put("y",szerY/3);
				mapa02.put("width", szerX);
				mapa02.put("height", szerY/3);
				mapa02.put("position", 2);
				lista.add(mapa02);
				HashMap<String,Integer> mapa03 = new HashMap<String, Integer>();
				mapa03.put("x",0);
				mapa03.put("y",szerY/3*2);
				mapa03.put("width", szerX);
				mapa03.put("height", szerY/3);
				mapa03.put("position", 3);
				lista.add(mapa03);
				//przes³anie listy do drugiej aktywnoœci
				
				Intent pickerIntent = new Intent(
						KolazActivity.this,	
						PickerActivity.class						
						);	
				pickerIntent.putExtra("lista", lista);
				pickerIntent.putExtra("size", lista.size());
				pickerIntent.putExtra("type", 4);
				startActivity(pickerIntent);
			}
		});
        layout5.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, Integer>> lista = new ArrayList<HashMap<String,Integer>>();

				// hashmap - para klucz - wartoœæ (nale¿y powtórzyæ tyle razy ile jest elementów kola¿u - czyli obrazków):		
				int szerX = displaymetrics.widthPixels;	     
		        int szerY = displaymetrics.heightPixels - statusBarHeight; 
				HashMap<String,Integer> mapa01 = new HashMap<String, Integer>();
				mapa01.put("x",0);
				mapa01.put("y",0);
				mapa01.put("width", (int) szerX/2);
				mapa01.put("height", (int) szerY);
				mapa01.put("position", 1);
				lista.add(mapa01);
				HashMap<String,Integer> mapa02 = new HashMap<String, Integer>();
				mapa02.put("x", (int) szerX/2);
				mapa02.put("y", 0);
				mapa02.put("width", (int) szerX/2);
				mapa02.put("height", (int) szerY/3);
				mapa02.put("position", 2);
				lista.add(mapa02);
				HashMap<String,Integer> mapa03 = new HashMap<String, Integer>();
				mapa03.put("x", (int) szerX/2);
				mapa03.put("y", (int) szerY/3);
				mapa03.put("width", (int) szerX/2);
				mapa03.put("height", (int) szerY/3);
				mapa03.put("position", 3);
				lista.add(mapa03);
				HashMap<String,Integer> mapa04 = new HashMap<String, Integer>();
				mapa04.put("x", (int) szerX/2);
				mapa04.put("y", (int) szerY/3*2);
				mapa04.put("width", (int) szerX/2);
				mapa04.put("height", (int) szerY/3);
				mapa04.put("position", 4);
				lista.add(mapa04);

				//przes³anie listy do drugiej aktywnoœci
	
				Intent pickerIntent = new Intent(
						KolazActivity.this,	
						PickerActivity.class						
						);	
				pickerIntent.putExtra("lista", lista);
				pickerIntent.putExtra("size", lista.size());
				pickerIntent.putExtra("type", 5);
				startActivity(pickerIntent);
			}
		});
        //lay06
        layout6.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, Integer>> lista = new ArrayList<HashMap<String,Integer>>();

				// hashmap - para klucz - wartoœæ (nale¿y powtórzyæ tyle razy ile jest elementów kola¿u - czyli obrazków):		
				int szerX = displaymetrics.widthPixels;	     
		        int szerY = displaymetrics.heightPixels - statusBarHeight; 
				HashMap<String,Integer> mapa01 = new HashMap<String, Integer>();
				mapa01.put("x",0);
				mapa01.put("y",0);
				mapa01.put("width", (int) szerX/3*2);
				mapa01.put("height", (int) szerY/4*3);
				mapa01.put("position", 1);
				lista.add(mapa01);
				HashMap<String,Integer> mapa02 = new HashMap<String, Integer>();
				mapa02.put("x", (int) szerX/3*2);
				mapa02.put("y", 0);
				mapa02.put("width", (int) szerX/3);
				mapa02.put("height", (int) szerY/4);
				mapa02.put("position", 2);
				lista.add(mapa02);
				HashMap<String,Integer> mapa03 = new HashMap<String, Integer>();
				mapa03.put("x", (int) szerX/3*2);
				mapa03.put("y", (int) szerY/4);
				mapa03.put("width", (int) szerX/3);
				mapa03.put("height", (int) szerY/4);
				mapa03.put("position", 3);
				lista.add(mapa03);
				HashMap<String,Integer> mapa04 = new HashMap<String, Integer>();
				mapa04.put("x", (int) szerX/3*2);
				mapa04.put("y", (int) szerY/2);
				mapa04.put("width", (int) szerX/3);
				mapa04.put("height", (int) szerY/4);
				mapa04.put("position", 4);
				lista.add(mapa04);
				HashMap<String,Integer> mapa05 = new HashMap<String, Integer>();
				mapa05.put("x", (int) szerX/3*2);
				mapa05.put("y", (int) szerY/4*3);
				mapa05.put("width", (int) szerX/3);
				mapa05.put("height", (int) szerY/4);
				mapa05.put("position", 5);
				lista.add(mapa05);
				HashMap<String,Integer> mapa06 = new HashMap<String, Integer>();
				mapa06.put("x", (int) szerX/3);
				mapa06.put("y", (int) szerY/4*3);
				mapa06.put("width", (int) szerX/3);
				mapa06.put("height", (int) szerY/4);
				mapa06.put("position", 6);
				lista.add(mapa06);
				HashMap<String,Integer> mapa07 = new HashMap<String, Integer>();
				mapa07.put("x", 0);
				mapa07.put("y", (int) szerY/4*3);
				mapa07.put("width", (int) szerX/3);
				mapa07.put("height", (int) szerY/4);
				mapa07.put("position", 7);
				lista.add(mapa07);

				//przes³anie listy do drugiej aktywnoœci
	
				Intent pickerIntent = new Intent(
						KolazActivity.this,	
						PickerActivity.class						
						);	
				pickerIntent.putExtra("lista", lista);
				pickerIntent.putExtra("size", lista.size());
				pickerIntent.putExtra("type", 6);
				startActivity(pickerIntent);
			}
		});
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
