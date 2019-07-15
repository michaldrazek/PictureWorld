package com.example.pictureworld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.example.pictureworld.FileManagerActivity.UploadPhoto;
import com.example.pictureworld.R.id;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class EditorActivity extends Activity {
	private ImageView thisPicture;
	private ImageView editButton;
	private Bitmap thisBitmap;
	private File picPath ;
	private ProgressDialog pDialog;
	private String pathOfLastClicked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_editor);
		
		pDialog = new ProgressDialog(EditorActivity.this);
		pDialog.setMessage("Wysyłanie zdjęcia na serwer");
		pDialog.setCancelable(false); 
		
		picPath = new File(getIntent().getStringExtra("picturePath"));
		thisBitmap = ImageUtils.decodeImageFromPath(getIntent().getStringExtra("picturePath"));		
		//-----------------------
		
		RelativeLayout.LayoutParams params = null; 
		
		if(thisBitmap.getHeight()>thisBitmap.getWidth()){
			thisPicture = new ScalingWidthImageView(EditorActivity.this);
			params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		} else {
			thisPicture = new ScalingHeightImageView(EditorActivity.this);
			params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		
		//thisPicture.requestLayout();
		//params.addRule(RelativeLayout.BELOW, R.id.saturationBar);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		//thisPicture.setVisibility(ImageView.VISIBLE);
		thisPicture.setLayoutParams(params);
		
		thisPicture.setImageBitmap(thisBitmap);
		RelativeLayout lay = (RelativeLayout) findViewById(R.id.thisPictureLayout);
		lay.addView(thisPicture);

		//thisPicture.invalidate();
		//thisPicture.refreshDrawableState();
		
	
		//-----------------------
		
		ImageView sendButton = (ImageView) findViewById(R.id.sentToServerIV);
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(EditorActivity.this);
				alert.setTitle("Czy na pewno?");
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
				    public void onClick(DialogInterface dialog, int which) {
				    	new UploadPhoto().execute();			    				        			
				    }			     			                    
				});			        					        		
				alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();	
					}
				});
				alert.show();	
					
			}
		});
		
		editButton = (ImageView) findViewById(R.id.editButton);
		editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 
				Intent intent = new Intent(
						EditorActivity.this,
						TextPickerActivity.class
						);				
				
				EditorActivity.this.startActivityForResult(intent, 111);
			}
		});
		
		ImageView saveButton = (ImageView) findViewById(R.id.editorSaveIV);
		saveButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {

	        	AlertDialog.Builder alert = new AlertDialog.Builder(EditorActivity.this);
            	alert.setTitle("Wpisz nazwę pliku");
            	
            	final EditText input = new EditText(EditorActivity.this);
            	alert.setView(input);

        	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
            	    public void onClick(DialogInterface dialog, int which) {   
            	    	
            	    	//thisPicture.buildDrawingCache(true);
            	    	//thisPicture.setDrawingCacheQuality(ImageView.DRAWING_CACHE_QUALITY_HIGH);
        	    	    //Bitmap bitmap = thisPicture.getDrawingCache();
        	    	    //Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, false);       	    	   
        	    	   	//thisPicture.destroyDrawingCache();
        	    	   	//thisPicture.buildDrawingCache(false);
            	    	/*Bitmap b = Bitmap.createBitmap(thisPicture.getMeasuredWidth(),
         				       thisPicture.getMeasuredHeight(),
         				       Bitmap.Config.ARGB_8888);
			         	Canvas c = new Canvas(b);
			         	thisPicture.draw(c);
			         	BitmapDrawable d = new BitmapDrawable(getResources(), b);
			         	Bitmap bmp = d.getBitmap();*/
            	    	//Bitmap bmp = ((BitmapDrawable) thisPicture.getDrawable()).getBitmap();
            	    	//Drawable d = thisPicture.getDrawable();
            	        //Bitmap bmp = Bitmap.createBitmap(thisPicture.getWidth(), thisPicture.getHeight(), Bitmap.Config.ARGB_8888);
            	        //Canvas canvas = new Canvas(bmp);
            	        //Paint paint = new Paint();
            	        //canvas.drawBitmap(bmp, 0, 0, paint);
            	        
            	    	BitmapDrawable drawable = (BitmapDrawable) thisPicture.getDrawable();
            	    	Bitmap bmp = drawable.getBitmap();
        	    	    try {
        	    	        FileOutputStream out = new FileOutputStream(picPath.getParent()+"/"+input.getText().toString()+".jpg");
        	    	        
        	    	        bmp.compress(Bitmap.CompressFormat.JPEG, 95, out);
        	    	        out.flush();
        	    	        out.close();
        					Intent intent = new Intent();
        					setResult(111, intent);        
        					finish();
        	    	   } catch (Exception e) {
        	    	        e.printStackTrace();
        	    	   }
            	    }
            	                    
            	});
			    		    			   

			    alert.setNegativeButton("Anuluj",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();

		                }
		            });
			    
			    alert.show();
			}
		});
				
		ImageView shareBtn = (ImageView) findViewById(R.id.editorShareIV);
		shareBtn.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent share = new Intent(Intent.ACTION_SEND);
				//typ który chcemy współdzielić
				share.setType("image/jpeg");
				SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String d = dFormat.format(new Date());
				//tworzę tymczasowy plik, który potem będzie współdzielony
				String tempFileName = "temp_" + d + ".jpg"; // dodaj bieżąca date do nazwy pliku

				//teraz utwórz plik File
				
				BitmapDrawable drawable = (BitmapDrawable) thisPicture.getDrawable();
    	    	Bitmap bmp = drawable.getBitmap();
    	    	
    	        File mediaFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);  	        
    	        File tempDir = new File(mediaFolder.getAbsolutePath().concat("/temp/"));
    	        if(!tempDir.exists()){
    	        	tempDir.mkdir();
    	        }    
    	    	File file = new File(tempDir.getAbsolutePath().concat("/").concat(tempFileName));
    	    	
    	    	try {
	    	    	FileOutputStream out = new FileOutputStream(file);	    	        
	    	        bmp.compress(Bitmap.CompressFormat.JPEG, 95, out);
	    	        //out.write(ImageUtils.convertBitmapToByteArray(bmp));
	    	        out.flush();
	    	        out.close();   					
		    	   } catch (Exception e) {
		    	        e.printStackTrace();
		    	   }

	    	    Intent shareIntent = new Intent();
	    	    shareIntent.setAction(Intent.ACTION_SEND);
	    	    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));
	    	    shareIntent.setType("image/jpeg");
	    	    startActivity(Intent.createChooser(shareIntent, "Udostępnianie pliku:"));
			}
		});
		
		ImageView effectsButton = (ImageView) findViewById(R.id.editorColorEffects);
		effectsButton.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				List<String> namePlates = new ArrayList <String>();							
				namePlates.add("normalny");
				namePlates.add("negatyw");
				namePlates.add("czerwony");
				namePlates.add("zielony");
				namePlates.add("niebieski");
				namePlates.add("cyan");
				namePlates.add("magenta");
				namePlates.add("żółty");
				
				AlertDialog.Builder alert = new AlertDialog.Builder(EditorActivity.this);
				alert.setTitle("Wybierz efekt");
				
				final String[] tab = new String[namePlates.size()];	
				alert.setItems(namePlates.toArray(tab), new DialogInterface.OnClickListener() { 
					
				    public void onClick(DialogInterface dialog, int which) {
				    	switch(which){
				    	 case 0:
				    		 thisPicture.setImageBitmap(ImageTransformations.resetToNormal(thisBitmap));
				    		 break;
				    	 case 1:
				    		 thisPicture.setImageBitmap(ImageTransformations.negative(thisBitmap));
				    		 break;
				    	 case 2:
				    		 thisPicture.setImageBitmap(ImageTransformations.redFilter(thisBitmap));
				    		 break;
				    	 case 3:
				    		 thisPicture.setImageBitmap(ImageTransformations.greenFilter(thisBitmap));
				    		 break;
				    	 case 4:
				    		 thisPicture.setImageBitmap(ImageTransformations.blueFilter(thisBitmap));
				    		 break;
				    	 case 5:
				    		 thisPicture.setImageBitmap(ImageTransformations.cyanFilter(thisBitmap));
				    		 break;
				    	 case 6:
				    		 thisPicture.setImageBitmap(ImageTransformations.magentaFilter(thisBitmap));
				    		 break;
				    	 case 7:
				    		 thisPicture.setImageBitmap(ImageTransformations.yellowFilter(thisBitmap));
				    		 break;
				    		 
				    	}
				    }
				    
				});

				alert.show();
			}
		});
		
		final SeekBar brightnessBar = (SeekBar) findViewById(R.id.brightnessBar);
		final SeekBar contrastBar = (SeekBar) findViewById(R.id.contrastBar);
		final SeekBar saturationBar = (SeekBar) findViewById(R.id.saturationBar);
		brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {		
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
					thisPicture.setImageBitmap(ImageTransformations.setAll(thisBitmap, progress-255, contrastBar.getProgress(), saturationBar.getProgress()));
					//thisPicture.invalidate();
					//thisPicture.refreshDrawableState();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		
		contrastBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
					thisPicture.setImageBitmap(ImageTransformations.setAll(thisBitmap, brightnessBar.getProgress()-255, progress, saturationBar.getProgress()));		
					
			}
		});
		

		saturationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
					thisPicture.setImageBitmap(ImageTransformations.setAll(thisBitmap, brightnessBar.getProgress()-255, contrastBar.getProgress(), progress));	
			}
		});
	}
	
	
	public class ScalingHeightImageView extends ImageView {

		  public ScalingHeightImageView(Context context) {
		    super(context);
		  }

		  public ScalingHeightImageView(Context context, AttributeSet attrs) {
		    super(context, attrs);
		  }

		  public ScalingHeightImageView(Context context, AttributeSet attrs, int defStyle) {
		    super(context, attrs, defStyle);
		  }
		  
		  @Override
	      protected void onDraw(Canvas canvas) {
		        super.onDraw(canvas);

		        // specific draw logic here
		        //setMinimumHeight(thisBitmap.getHeight());
		        //setMinimumWidth(thisBitmap.getWidth());
		        setDrawingCacheQuality(ImageView.DRAWING_CACHE_QUALITY_HIGH);
		        setDrawingCacheEnabled(true); // cache
		    }
		  
		  @Override	
		  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		    Drawable mDrawable = getDrawable();
		    if (mDrawable != null) {
		      int mDrawableWidth = mDrawable.getIntrinsicWidth();
		      int mDrawableHeight = mDrawable.getIntrinsicHeight();
		      float actualAspect = (float) mDrawableWidth / (float) mDrawableHeight;

		      // Assuming the width is ok, so we calculate the height.
		      final int actualWidth = MeasureSpec.getSize(widthMeasureSpec);
		      final int height = (int) (actualWidth / actualAspect);
		      heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		    }
		    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		  }
	}
	
	public class ScalingWidthImageView extends ImageView {

		  public ScalingWidthImageView(Context context) {
		    super(context);
		  }

		  public ScalingWidthImageView(Context context, AttributeSet attrs) {
		    super(context, attrs);
		  }

		  public ScalingWidthImageView(Context context, AttributeSet attrs, int defStyle) {
		    super(context, attrs, defStyle);
		  }

		  @Override
	      protected void onDraw(Canvas canvas) {
		        super.onDraw(canvas);

		        // specific draw logic here
		        //setMinimumHeight(thisBitmap.getHeight());
		        //setMinimumWidth(thisBitmap.getWidth());
		        setDrawingCacheQuality(ImageView.DRAWING_CACHE_QUALITY_HIGH);
		        setDrawingCacheEnabled(true); // cache
		    }
		  
		  @Override
		  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		    Drawable mDrawable = getDrawable();
		    if (mDrawable != null) {
		      int mDrawableWidth = mDrawable.getIntrinsicWidth();
		      int mDrawableHeight = mDrawable.getIntrinsicHeight();
		      float actualAspect = (float) mDrawableHeight / (float) mDrawableWidth;

		      // Assuming the height is ok, so we calculate the width.
		      final int actualHeight = MeasureSpec.getSize(heightMeasureSpec);
		      final int width = (int) (actualHeight / actualAspect);
		      widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		    }
		    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		  }
		}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//Log.d("YY", "width: "+Integer.toString(thisPicture.getWidth()));
		//Log.d("YY", "height: "+Integer.toString(thisPicture.getHeight()));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 111){
			AssetManager assetManager = getAssets();
			String[] lista = null;
			try {
				lista = assetManager.list("fonts");
			} catch (IOException e) {
				e.printStackTrace();
			} // fonts to nazwa podfolderu w assets
			if(lista != null){
				final RelativeLayout lay = (RelativeLayout) findViewById(R.id.thisPictureLayout);
				Bundle heh = data.getExtras();
				int fontSize = heh.getInt("fontSize");
				Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/"+heh.getString("typeface"));

				final TextPreview meh = new TextPreview(EditorActivity.this, thisPicture.getLeft(), thisPicture.getTop()+fontSize/2+10, fontSize, heh.getString("text"), tf , heh.getInt("strokeColor"), heh.getInt("fillColor"));
				lay.addView(meh);
				/*lay.setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						 //wykrycie odpowiedniego eventa (down, up, move)
						v.performClick();
				        switch(event.getAction()){                
				                    
				            case MotionEvent.ACTION_MOVE:			           
				            	if(event.getX()>meh.getLeft()&&event.getX()<meh.getRight()&&event.getY()>meh.getTop()&&event.getY()<meh.getBottom()){
				            		float offsetX = meh.getLeft() + event.getX();
					                float offsetY = meh.getTop() + event.getY();
					                meh.setX(event.getX()-offsetX);
					                meh.setY(event.getY()-offsetY);
					                Log.d("YY", "spam");
				            	} else{
				            		Log.d("YY", "niespam");
				            	}
				                break;

				            case MotionEvent.ACTION_DOWN:
				                break;
				            case MotionEvent.ACTION_UP:                    
				                break;
				        }                
						return true;
					}
				});*/			
			}
		}	
	}
	public class UploadPhoto extends AsyncTask<String, Void, String> {
		//private String response;
		
		public UploadPhoto() {
			super();
			
		}

		@Override
		protected String doInBackground(String... params) {				
			HttpPost httpPost = new HttpPost("http://4ia1.spec.pl.hostingasp.pl/Dr%C4%85%C5%BCek_Micha%C5%82/Saving.aspx");
			BitmapDrawable drawable = (BitmapDrawable) thisPicture.getDrawable();
	    	Bitmap bmp = drawable.getBitmap();
	    	
			httpPost.setEntity(new ByteArrayEntity(ImageUtils.convertBitmapToByteArray(bmp)));

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
			} // odebranie odpowiedzi z serwera, którą potem wyswietlimy w onPostExecute
		
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			AlertDialog.Builder alert = new AlertDialog.Builder(EditorActivity.this);
			alert.setTitle("Odpowiedź");
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
}

