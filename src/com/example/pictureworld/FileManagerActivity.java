package com.example.pictureworld;

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FileManagerActivity extends Activity {
	private File thisFolder;
	private FrameLayout thisLayout;
	private DisplayMetrics displaymetrics;
	private int numberOfMiniatures = 3;
	private int sizeOfMiniature;
	private ProgressDialog pDialog;
	private String pathOfLastClicked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_file_manager);
		String path = getIntent().getStringExtra("thisFolderPath");
		thisFolder = new File(path);
		thisLayout = (FrameLayout) findViewById(R.id.FileManagerFrLay);
		displaymetrics = new DisplayMetrics();
		(FileManagerActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		sizeOfMiniature = displaymetrics.widthPixels<displaymetrics.heightPixels?displaymetrics.widthPixels/numberOfMiniatures:displaymetrics.heightPixels/numberOfMiniatures;
		
		addMiniatures();
		pDialog = new ProgressDialog(FileManagerActivity.this);
		pDialog.setMessage("komunikat");
		pDialog.setCancelable(false); 
	}
	
	private void addMiniatures(){
		 for(final File file: thisFolder.listFiles()){
        	if(!file.isDirectory()){
        		MiniPhoto temp = new MiniPhoto(FileManagerActivity.this, sizeOfMiniature, sizeOfMiniature, ImageUtils.decodeImageFromPath(file.getAbsolutePath()));
        		temp.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								FileManagerActivity.this,
								EditorActivity.class
								);
						intent.putExtra("picturePath", file.getAbsolutePath());
						
						FileManagerActivity.this.startActivityForResult(intent, 111);
						
					}
				});
        		temp.setOnLongClickListener(new View.OnLongClickListener() {		
					@Override
					public boolean onLongClick(View v) {
						pathOfLastClicked = file.getAbsolutePath();
						String[] choices = { "Wyślij zdjęie na server" , "Usuń zdjęcie"};
						AlertDialog.Builder ad = new AlertDialog.Builder(FileManagerActivity.this);
						ad.setTitle("Wybór");
						ad.setItems(choices, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {							
								switch (which){
								case 0:
									new UploadPhoto().execute();							
									break;
								
								case 1:
									file.delete();
									thisLayout.removeAllViewsInLayout();
									addMiniatures();
									break;
								}
							}
						});
						ad.show();
						
						return false;
					}
				});
        		thisLayout.addView(temp);
        	}     	        	
         } 
		
		 refreshMiniatures();
	}
	
	private void refreshMiniatures(){
		thisLayout.getLayoutParams().height=(int)Math.ceil((double)thisLayout.getChildCount()/(double)numberOfMiniatures)*sizeOfMiniature;
		for(int i=0; i<(int)Math.ceil((double)thisLayout.getChildCount()/(double)numberOfMiniatures); i++){
			for(int j=0; j<numberOfMiniatures; j++){
				if(thisLayout.getChildAt(i*numberOfMiniatures+j)!= null){
					thisLayout.getChildAt(i*numberOfMiniatures+j).setX(j*sizeOfMiniature);
					thisLayout.getChildAt(i*numberOfMiniatures+j).setY(i*sizeOfMiniature);
				}				
			}
		}
		
		//Toast.makeText(this, (int)Math.ceil((double)thisLayout.getChildCount()/(double)numberOfMiniatures)*sizeOfMiniature+"", Toast.LENGTH_SHORT).show();
	}
	
	public class UploadPhoto extends AsyncTask<String, Void, String> {
		//private String response;
		
		public UploadPhoto() {
			super();
			
		}

		@Override
		protected String doInBackground(String... params) {				
			HttpPost httpPost = new HttpPost("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/Saving.aspx");

			httpPost.setEntity(new ByteArrayEntity(ImageUtils.convertBitmapToByteArray(ImageUtils.decodeImageFromPath(pathOfLastClicked))));

			DefaultHttpClient httpClient = new DefaultHttpClient(); 

			HttpResponse httpResponse = null; // obiekt odpowiedzi z serwera

			try {
				httpResponse = httpClient.execute(httpPost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // wykonanie wysłania

			String result = null;
			
			try {
				result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				if(result == null)
					result = "brak odpowiedzi";
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // odebranie odpowiedzi z serwera, ktｹ potem wyswietlimy w onPostExecute
		
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			AlertDialog.Builder alert = new AlertDialog.Builder(FileManagerActivity.this);
			alert.setTitle("Response");
			alert.setMessage(result);
		

			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
			    public void onClick(DialogInterface dialog, int which) {
			    	 dialog.cancel();				        			
			    }			     			                    
			});			        					        		
			//
			alert.show();		
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.show();			
		}
		
	}
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		thisLayout.removeAllViewsInLayout();
		addMiniatures();
			
	}
	
}


