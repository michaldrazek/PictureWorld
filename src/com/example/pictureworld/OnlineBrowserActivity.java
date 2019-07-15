package com.example.pictureworld;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pictureworld.MainActivity.GetAllImagesJson;
import com.example.pictureworld.MainActivity.LoadImageTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class OnlineBrowserActivity extends Activity {

	private int heightOfMiniature;
	private int numberOfMiniatures = 3;
	private DisplayMetrics displaymetrics;
	private static final String IMAGE_NAME = "IMAGE_NAME";
	private static final String IMAGE_NAME_SMALL = "IMAGE_NAME_SMALL";
	private static final String IMAGE_SAVE_TIME = "IMAGE_SAVE_TIME";
	private JSONArray allImagesJson = null; //obiekt JSONArray
	private ArrayList<HashMap<String, String>> allImagesList = new ArrayList<HashMap<String, String>>(); //tablica z danymi obrazków
	private Drawable loadedImage;
	private int statusBarHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_browser);
		
		statusBarHeight = getStatusBarHeight();
		displaymetrics = new DisplayMetrics();
		(OnlineBrowserActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		heightOfMiniature = (displaymetrics.heightPixels-statusBarHeight)/numberOfMiniatures;
		
		new GetAllImagesJson().execute();
	}

	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	} 
	
	public class GetAllImagesJson extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpPost httpPost = new HttpPost("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/GetImages.aspx");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = null;
			try {
				httpResponse = httpClient.execute(httpPost);
				String jsonString = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				//jesli jsonString nie jest pusty wtedy parsuje go na obiekt JSON
				JSONObject jsonObj = new JSONObject(jsonString);
				//a potem rozbijam na tablicê obiektów
				allImagesJson = jsonObj.getJSONArray("imageList");
				//teraz mogê pobieraæ dane for-em z elementów tej tablicy

				for (int i = 0; i < allImagesJson.length(); i++) {

				    // obiekty po kolei
				    JSONObject image = allImagesJson.getJSONObject(i);
				    // poszczególne pola
				    String imageName = image.getString(IMAGE_NAME);
				    String imageNameSmall = image.getString(IMAGE_NAME_SMALL);
				    String imageSaveTime = image.getString(IMAGE_SAVE_TIME);
				    
				    // tymczasowa hashmap na jeden rekord
				    HashMap<String, String> imageMap = new HashMap<String, String>();
				    // dodaje dane do mapy
				    imageMap.put(IMAGE_NAME, imageName);    
				    imageMap.put(IMAGE_NAME_SMALL, imageNameSmall);
				    imageMap.put(IMAGE_SAVE_TIME, imageSaveTime);
				    //dodaje dane do listy -
				    allImagesList.add(imageMap);
				}
				//Log.d("YY", allImagesList.toString());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(allImagesList.size()>0){
				for(int i=0; i<allImagesList.size();i++){
					new LoadImageTask().execute("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/small/"+ allImagesList.get(i).get(IMAGE_NAME_SMALL),allImagesList.get(i).get(IMAGE_NAME));
				}	
			}
		}		
	}
	
	public class LoadImageTask extends AsyncTask<String, Void, String>{

		
		
		@Override
		protected String doInBackground(String... params) {
			loadedImage = LoadImageFromWeb(params[0]);
			return params[1];
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//pBar = (ProgressBar) findViewById(R.id.mainActivityProgressBar);
			//pBar.setVisibility(ProgressBar.VISIBLE);
		}
		
		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);
			LinearLayout onLay = (LinearLayout) findViewById(R.id.OnlineLayout);
			ImageView pushed = new ImageView(OnlineBrowserActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			pushed.setLayoutParams(new LinearLayout.LayoutParams(params));
			pushed.getLayoutParams().height = heightOfMiniature;
			pushed.setScaleType(ImageView.ScaleType.CENTER_CROP);
			pushed.setImageDrawable(loadedImage);	
			onLay.addView(pushed);
			pushed.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					new LoadFullPic().execute(result);
				}
			});
		}
		
		public Drawable LoadImageFromWeb(String url) {
	        
		    InputStream inputStream = null;
			try {
				inputStream = (InputStream) new URL(url).getContent();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return Drawable.createFromStream(inputStream, url);
	        
		}
	}
	
	
	public class LoadFullPic extends AsyncTask<String, Void, String>{

		private Drawable fullDrawable;
		
		@Override
		protected String doInBackground(String... params) {
			fullDrawable = LoadImageFromWeb("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/full/" + params[0]);
			return params[0];
		}
		
	
		
		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);
			Dialog builder = new Dialog(OnlineBrowserActivity.this);
		    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    builder.getWindow().setBackgroundDrawable(
		        new ColorDrawable(android.graphics.Color.TRANSPARENT));
		    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
		        @Override
		        public void onDismiss(DialogInterface dialogInterface) {
		            //nothing;
		        }
		    });
		   
		    final ImageView imageView = new ImageView(OnlineBrowserActivity.this);
		    imageView.setImageDrawable(fullDrawable);
		    imageView.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/full/" + result));
					startActivity(browserIntent);
					return false;
				}
			});
		    //fullDrawable.
		    Log.d("YY", "height: "+ displaymetrics.heightPixels);
		    imageView.setImageBitmap(ImageUtils.scaleDrawableToFit(fullDrawable, displaymetrics.widthPixels, displaymetrics.heightPixels-statusBarHeight));
			//imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
		            ViewGroup.LayoutParams.MATCH_PARENT, 
		            ViewGroup.LayoutParams.MATCH_PARENT));
		    builder.show();
		}
		
		public Drawable LoadImageFromWeb(String url) {
	        
		    InputStream inputStream = null;
			try {
				inputStream = (InputStream) new URL(url).getContent();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return Drawable.createFromStream(inputStream, url);
	        
		}
		
		
	}
}
