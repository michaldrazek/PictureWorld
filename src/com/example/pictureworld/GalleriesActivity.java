package com.example.pictureworld;

import java.io.File;
import java.io.FileFilter;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GalleriesActivity extends Activity {

	private LinearLayout lin1;
	private String dirName = "MichalDrazek";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_galleries);
		lin1 = (LinearLayout) findViewById(R.id.lin1);
		//nakierowanie i tworzenie folderu
			
        final File parentFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File galleryFolder = null;

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
        
        File[] fileList = galleryFolder.listFiles();
        for(File folder: fileList){
        	if(folder.isDirectory()){
        		GalleryManager g = new GalleryManager(GalleriesActivity.this, folder);
        		FileFilter notDirectoryFilter = new FileFilter() {				
					@Override
					public boolean accept(File pathname) {
						return pathname.isFile();
					}
				};
				
        		if(folder.listFiles(notDirectoryFilter).length>0)
        			g.setPictureFromPath(folder.listFiles(notDirectoryFilter)[0].getAbsolutePath());
        		lin1.addView(g);
        	}     	        	
        }     
        
        //
        ImageView newFol = (ImageView) findViewById(R.id.GalleriesNewFolder);
		newFol.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(GalleriesActivity.this);
            	alert.setTitle("WprowadŸ nazwê katalogu");
            	alert.setIcon(R.drawable.new_folder);
            	
            	final EditText input = new EditText(GalleriesActivity.this);
            	input.setText("Nowy Folder");
            	alert.setView(input);

            	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
            	    public void onClick(DialogInterface dialog, int which) {
            	    	File newDir = new File(parentFolder.getAbsolutePath()+"/"+ dirName + "/" + input.getText());
            	    	newDir.mkdir();
            	    	GalleryManager g = new GalleryManager(GalleriesActivity.this, newDir);
            	    	lin1.addView(g);
            	    }
            	                    
            	});

            	//no
            	alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {		                	              
            	    public void onClick(DialogInterface dialog, int which) {
            	    	dialog.cancel();
            	    }
        	    });
            	alert.show();
            
			}
		});
	}

	public class GalleryManager extends LinearLayout {
		private Context thisContext;
		private ImageView iv;
		private TextView tv;
		
		public GalleryManager(Context context, final File folder) {
				super(context);
				// 
				//pobranie szerokosci
				DisplayMetrics displaymetrics = new DisplayMetrics();
		        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		        int width = displaymetrics.widthPixels;	        
		        thisContext = context;
				setLayoutParams(new LinearLayout.LayoutParams(width,LayoutParams.WRAP_CONTENT));
				setOrientation(HORIZONTAL);
				//obrazek
				iv = new ImageView(context);
				iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        iv.setImageResource(R.drawable.photo);
		        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(width/4,ViewGroup.LayoutParams.WRAP_CONTENT);
		        layoutParams1.weight = 1;
		        //layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL);
		        //layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		        iv.setLayoutParams(layoutParams1);	        
				iv.getLayoutParams().height = iv.getLayoutParams().width;
		        //podpis
		        tv = new TextView(context);
		        tv.setText(folder.getName());
		        tv.setGravity(0x11);
		        tv.setTextSize(30);	 	    
		        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(3*width/4,ViewGroup.LayoutParams.MATCH_PARENT);
		        layoutParams2.weight = 1;
		        //layoutParams2.addRule(LinearLayout.TEXT_ALIGNMENT_CENTER);
		        //layoutParams2.addRule(LinearLayout.ALIGN_PARENT_BOTTOM);
		        tv.setLayoutParams(layoutParams2);
				
				this.addView(iv);
				this.addView(tv);
				//listener
				this.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// 
						Intent thisIntent = new Intent(
								((Activity)thisContext) ,
								FileManagerActivity.class
								);
						thisIntent.putExtra("thisFolderPath", folder.getAbsolutePath());
						thisContext.startActivity(thisIntent);
					}
				});
				
				this.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						String[] choices = {"Usuñ katalog"};
						AlertDialog.Builder ad = new AlertDialog.Builder(thisContext);
						ad.setTitle("Wybór");
						ad.setItems(choices, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {							
								switch (which){
								case 0:
									AlertDialog.Builder alert = new AlertDialog.Builder(thisContext);
				                	alert.setTitle("Usuwanie");
				                	alert.setMessage("Czy napewno chcesz usun¹æ ten katalog?");
				                	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
				                	    public void onClick(DialogInterface dialog, int which) {
											for(File plik : folder.listFiles()){
												plik.delete();
											}
											folder.delete();
											for(int i=0; i<lin1.getChildCount(); i++){
												if(lin1.getChildAt(i) == GalleryManager.this)
													lin1.removeViewAt(i);
											}
				                	    }	                	                    
				                	});

				                	//no
				                	alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {		                	              
				                	    public void onClick(DialogInterface dialog, int which) {
				                	    	dialog.cancel();
				                	    }
			                	    });		 
				                	alert.show();
									break;
								}
							}
						});
						ad.show();
						return false;
					}
				});
		}
		
		public void setPictureFromPath(String path){
			iv.setImageBitmap(ImageUtils.decodeImageFromPath(path));
		}
		
	}


}
