package com.example.pictureworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.pictureworld.EditorActivity.UploadPhoto;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class PickerActivity extends Activity {
 private ArrayList<HashMap<String, Integer>> lista;
 private int listSize;
 private DisplayMetrics displaymetrics;
 private int type;
 private int lastClickedID;
 private Bitmap[] zdjecia;
 
 private FrameLayout mainLayout;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_picker);
		displaymetrics = new DisplayMetrics();
		(PickerActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		mainLayout = (FrameLayout) findViewById(R.id.pickerLayout);
		lista = (ArrayList<HashMap<String, Integer>>) getIntent().getSerializableExtra("lista");
		listSize = (int) getIntent().getIntExtra("size", 0);
		type = (int) getIntent().getIntExtra("type", 0);
		addLayouts();
		zdjecia = new Bitmap[listSize];
	}
	

	private void addLayouts(){
		if(listSize==lista.size()){
			for(int i=0; i<lista.size(); i++){
				HashMap<String, Integer> element;
				element = lista.get(i);
				mainLayout.addView(addSingleImage(element.get("x"), element.get("y"), element.get("width"), element.get("height"), i));
			}
		}
	}
	
	private ImageView addSingleImage(int posX, int posY, int szer, int wys, final int id){
		final ImageView iv = new ImageView(PickerActivity.this);
		
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT);  
        int size = displaymetrics.widthPixels<displaymetrics.heightPixels?displaymetrics.widthPixels/5:displaymetrics.heightPixels/5;
        layoutParams1.height = size;
        layoutParams1.width = size;
        iv.setLayoutParams(layoutParams1);
        iv.setImageResource(R.drawable.camera);
        iv.setX(posX+((szer-size)/2));
        iv.setY(posY+((wys-size)/2)); 
        iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 
				AlertDialog.Builder builder = new AlertDialog.Builder(PickerActivity.this);
			    builder.setTitle("Wybór");
			   
		        List <String> temptab = new ArrayList<String>();
        	      temptab.add("Zrób zdjêcie");
        	      temptab.add("Pobierz z galerii systemowej");
        	      temptab.add("Obróæ");
        	      temptab.add("Przerzuæ w pionie");
        	      temptab.add("Przerzuæ w poziomie");
		        
		        final String[] aryj = new  String[temptab.size()];
		        
			    builder.setItems(temptab.toArray(aryj), new DialogInterface.OnClickListener() {                
			        public void onClick(DialogInterface dialog, int which) {                    
			        	switch (which){
			        	case 0:
				        		HashMap<String, Integer> element;
								//Toast.makeText(PickerActivity.this, "type: "+type, Toast.LENGTH_SHORT).show();
								element = lista.get(id);
								int position = element.get("position");
								//Toast.makeText(PickerActivity.this, "position: "+position, Toast.LENGTH_SHORT).show();
								String code = Integer.toString(type) + Integer.toString(position);
								Intent cameraIntent = new Intent(
										PickerActivity.this,	
										PickerCameraActivity.class						
										);	
								cameraIntent.putExtra("lista", lista);
								cameraIntent.putExtra("size", lista.size());
								cameraIntent.putExtra("code", Integer.decode(code));			
								startActivityForResult(cameraIntent,  Integer.decode(code));
								iv.setId(Integer.decode(code));
			        		break;
			        	case 1:
			        			lastClickedID = id;
			        			iv.setId(10000 + id);
				        		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				        		photoPickerIntent.setType("image/*");
				        		startActivityForResult(photoPickerIntent, 6404);  
			        		break;
			        	case 2:
			        			zdjecia[id] = ImageUtils.rotateBitmap(zdjecia[id]);
			        			iv.setImageBitmap(zdjecia[id]);
			        		break;
			        	case 3:
			        			zdjecia[id] = ImageUtils.flipBitmapVertically(zdjecia[id]);
		        				iv.setImageBitmap(zdjecia[id]);
		        			break;
			        	case 4:
				        		zdjecia[id] = ImageUtils.flipBitmapHorizontally(zdjecia[id]);
		        				iv.setImageBitmap(zdjecia[id]);
			        		break;
			        	}
					        
			        }
			    });
			    
			   
			    builder.setNegativeButton("Anuluj",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                }
		            });
			    
			    builder.show();
				
				
			}
		});
		return iv;
	}
	
	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// 
			super.onActivityResult(requestCode, resultCode, data);
			
			if(data!=null){
				if(requestCode==6404 && resultCode == RESULT_OK){

					Uri selectedImage = data.getData();
		            InputStream imageStream = null;
					try {
						imageStream = getContentResolver().openInputStream(selectedImage);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ImageView focused = (ImageView) findViewById(10000+lastClickedID);
					HashMap<String, Integer> element;
					element = lista.get(lastClickedID);
					Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
					//Toast.makeText(this, ""+(Character.getNumericValue(String.valueOf(requestCode).charAt(1)) - 2), Toast.LENGTH_SHORT).show();
					focused.setX(element.get("x"));
					focused.setY(element.get("y"));
					focused.getLayoutParams().width = element.get("width");
					focused.getLayoutParams().height = element.get("height");
					focused.setScaleType(ImageView.ScaleType.CENTER_CROP);
					focused.setImageBitmap(yourSelectedImage);
					zdjecia[lastClickedID] = yourSelectedImage;
				}else {
					Bundle extras = data.getExtras();
					byte[] xdata = (byte[]) extras.get("xdata");
					// teraz konwersja byte na bitmap
					//Bitmap bmp = BitmapFactory.decodeByteArray(xdata,0, xdata.length);
					ImageView focused = (ImageView) findViewById(requestCode);
					HashMap<String, Integer> element;
					int index = Character.getNumericValue(String.valueOf(requestCode).charAt(1)) - 1 ;
					element = lista.get(index);
					
					//Toast.makeText(this, ""+(Character.getNumericValue(String.valueOf(requestCode).charAt(1)) - 2), Toast.LENGTH_SHORT).show();
					focused.setX(element.get("x"));
					focused.setY(element.get("y"));
					focused.getLayoutParams().width = element.get("width");
					focused.getLayoutParams().height = element.get("height");
					focused.setImageBitmap(ImageUtils.decodeRotateAndCreateBitmap(xdata));
					focused.setScaleType(ImageView.ScaleType.CENTER_CROP);
					zdjecia[index] = ImageUtils.decodeRotateAndCreateBitmap(xdata);
				}
				
			}
		}
		

}
