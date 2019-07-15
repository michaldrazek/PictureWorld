package com.example.pictureworld;

import java.io.File;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	
	private RelativeLayout rel_lay1;
	private RelativeLayout rel_lay2;
	private RelativeLayout rel_lay3;
	private RelativeLayout rel_lay4;
	private File galleryFolder;
	private String dirName = "MichalDrazek";
	
	private int currentImage = 0;
	private static final String IMAGE_NAME = "IMAGE_NAME";
	private static final String IMAGE_NAME_SMALL = "IMAGE_NAME_SMALL";
	private static final String IMAGE_SAVE_TIME = "IMAGE_SAVE_TIME";
	private JSONArray allImagesJson = null; //obiekt JSONArray
	private ArrayList<HashMap<String, String>> allImagesList; //tablica z danymi obrazków
	private Drawable loadedImage;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //
        allImagesList = new ArrayList<HashMap<String, String>>();
       
        ImageView iv = (ImageView) findViewById(R.id.mainActivityImageView);	
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        
        File parentFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        
        if(Networking.checkConnection(MainActivity.this)){
        	Log.d("YY", "CONNECTED");
        	new GetAllImagesJson().execute();
        } else {
        	Log.d("YY", "NO CONNECTION");
        }
        
        File[] parentFolderFiles = parentFolder.listFiles();
        boolean ifGalleryFolderExists = false;
        for ( File folder: parentFolderFiles ){
        	if(folder.getName()==dirName){
        		ifGalleryFolderExists = true;
        		galleryFolder = folder;
        	}
        }
        
        if(ifGalleryFolderExists){
	    	if(!galleryFolder.isDirectory()){
	    		galleryFolder = new File(parentFolder.getPath(), dirName);
	    		galleryFolder.mkdir();
	    	}
    	} else {
    		galleryFolder = new File(parentFolder.getPath(), dirName);
    		galleryFolder.mkdir();
    	}

        final ImageView leftArrow = (ImageView) findViewById(R.id.mainLeftIV);
        leftArrow.setVisibility(ImageView.INVISIBLE);
        final ImageView rightArrow = (ImageView) findViewById(R.id.mainRightIV);
        leftArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(allImagesList.size()>0){
					if(currentImage==0){
						
					} else if(currentImage==1){				
						currentImage-=1;
						leftArrow.setVisibility(ImageView.INVISIBLE);			
						new LoadImageTask().execute("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/small/"+ allImagesList.get(currentImage).get(IMAGE_NAME_SMALL));
					} else {
						currentImage-=1;
						new LoadImageTask().execute("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/small/"+ allImagesList.get(currentImage).get(IMAGE_NAME_SMALL));					
						rightArrow.setVisibility(ImageView.VISIBLE);	
					}
				}
			}
		});
        
        rightArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(allImagesList.size()>0){
					if(currentImage==allImagesList.size()-1){
						
					} else if(currentImage==allImagesList.size()-2){				
						currentImage+=1;
						rightArrow.setVisibility(ImageView.INVISIBLE);			
						new LoadImageTask().execute("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/small/"+ allImagesList.get(currentImage).get(IMAGE_NAME_SMALL));
					} else {
						currentImage+=1;
						new LoadImageTask().execute("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/small/"+ allImagesList.get(currentImage).get(IMAGE_NAME_SMALL));
						leftArrow.setVisibility(ImageView.VISIBLE);	
					}
				}
			}
		});
        
        ImageView iv1 = (ImageView) findViewById(R.id.mainActivityImageView);
        iv1.setImageResource(R.drawable.tram);
        //
        rel_lay1 = (RelativeLayout) findViewById(R.id.rel_lay1);
        rel_lay1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent camIntent = new Intent(
						MainActivity.this,
						CameraActivity.class					
						);
				camIntent.putExtra("path", galleryFolder.getAbsolutePath());
				startActivity(camIntent);	
				
			}
		});
        //
        rel_lay2 = (RelativeLayout) findViewById(R.id.rel_lay2);
        rel_lay2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent galIntent = new Intent(
						MainActivity.this,
						GalleriesActivity.class					
						);				
				startActivity(galIntent);	
				
			}
		});
        //
        rel_lay3 = (RelativeLayout) findViewById(R.id.rel_lay3);
        rel_lay3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 
				Intent kolIntent = new Intent(
						MainActivity.this,
						KolazActivity.class					
						);				
				startActivity(kolIntent);
			}
		});
        
        rel_lay4 = (RelativeLayout) findViewById(R.id.rel_lay4);
        rel_lay4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent onBrowIntent = new Intent(
						MainActivity.this,
						OnlineBrowserActivity.class					
						);				
				startActivity(onBrowIntent);
			}
		});
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
			//Log.d("YY",  allImagesList.get(currentImage).get(IMAGE_NAME_SMALL));
			new LoadImageTask().execute("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/small/"+ allImagesList.get(currentImage).get(IMAGE_NAME_SMALL));
			}
		}		
	}
	
	public class LoadImageTask extends AsyncTask<String, Void, String>{

		private ProgressBar pBar;
		
		@Override
		protected String doInBackground(String... params) {
			loadedImage = LoadImageFromWeb(params[0]);
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pBar = (ProgressBar) findViewById(R.id.mainActivityProgressBar);
			pBar.setVisibility(ProgressBar.VISIBLE);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pBar.setVisibility(ProgressBar.INVISIBLE);
			ImageView iv = (ImageView) findViewById(R.id.mainActivityImageView);			
			iv.setImageDrawable(loadedImage);
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
