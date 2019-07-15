package com.example.pictureworld;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;

public class PickerCameraActivity extends Activity {
	FrameLayout cameraLayout;
	Camera camera;
	CameraPreview camPrev;
	int cameraId = -1;
	byte[] fdata;
	ImageView camIV;
	ImageView conIV;
	ImageView abIV;
	int code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_picker_camera);
		code = (int) getIntent().getIntExtra("code", 0);
		cameraLayout = (FrameLayout) findViewById(R.id.pickerCameraLayout);
		camIV = (ImageView) findViewById(R.id.camIV);
		conIV = (ImageView) findViewById(R.id.conIV);
		abIV = (ImageView) findViewById(R.id.abIV);
		camIV.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				camera.takePicture(null, null, camPictureCallback);
			}
		});
		conIV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 
				//dodac obsluge przycisku potwierdzajacego
				Intent intent = new Intent();
				intent.putExtra("xdata", fdata);
				setResult(code, intent);        
				finish();
				//Toast.makeText(PickerCameraActivity.this, "code:"+ code + "; ", Toast.LENGTH_SHORT).show();
			}
		});
		if(initCamera()){
			initPreview();
		}
		
		abIV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fdata = null;
				conIV.setVisibility(android.view.View.GONE);
				abIV.setVisibility(android.view.View.GONE);
				camera.startPreview();
			}
		});
	}

	private void initPreview(){
		camPrev = new CameraPreview(PickerCameraActivity.this, camera);
		cameraLayout = (FrameLayout) findViewById(R.id.pickerCameraLayout);
		cameraLayout.addView(camPrev);
		final Camera.Parameters camParams = camera.getParameters();
		
		if(camParams==null){
			Toast.makeText(PickerCameraActivity.this, "Nie uda³o siê pobraæ parametrów kamery.", Toast.LENGTH_LONG).show();
		} else{					
			if(camParams.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
				camParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				camera.setParameters(camParams);
			}
			
			String[] zimiok = new String[camParams.getSupportedPictureSizes().size()];
			
			for(int i=0; i<camParams.getSupportedPictureSizes().size(); i++)
			{
				zimiok[i]=camParams.getSupportedPictureSizes().get(i).width +"x"+ camParams.getSupportedPictureSizes().get(i).height;
			}
	
			camParams.setPictureSize(camParams.getSupportedPictureSizes().get(zimiok.length-1).width, camParams.getSupportedPictureSizes().get(zimiok.length-1).height);
	    	camera.setParameters(camParams); 		
		}

	}
	
	private PictureCallback camPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            fdata = data;    
            conIV.setVisibility(android.view.View.VISIBLE);
			abIV.setVisibility(android.view.View.VISIBLE);
            // odswiez kamerê (zapobiega przyciêciu siê kamery po zrobieniu zdjêcia)		
            //camera.startPreview();
        }
	};
	
	private boolean initCamera(){
		boolean cam = getPackageManager().hasSystemFeature(
	                PackageManager.FEATURE_CAMERA);
        if (!cam) {
            // uwaga - brak kamery, przetestuj przy wy³aczonej
            // kamerze w emulatorze
        	return false;
        } else {
            // wykorzystanie danych zwróconych przez kolejna funkcjê getCameraId

            cameraId = getCameraId();
                // jest jakaœ kamera!  
            if (cameraId < 0) {
                // brak kamery z przodu!
            } else if (cameraId >= 0) {
            	
                camera = Camera.open(cameraId);
            } else {
                camera = Camera.open();
            }
            return true;
        }
	}
	
	private int getCameraId(){
		int camerasCount = Camera.getNumberOfCameras(); // pobranie referencji do kamer
        
		for (int i = 0; i < camerasCount; i++) {
		    CameraInfo cameraInfo = new CameraInfo();
		    Camera.getCameraInfo(i, cameraInfo);
		            // 0 - back camera
		            // 1 - front camera
		    
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                // zwróæ (return) z funkcji i -> czyli aktywn¹ kamerê
            	return i;
            } else if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                // zwróæ (return) z funkcji i -> czyli aktywn¹ kamerê
            	return i;
            } 	            
		}
		return -1;
	}
	
	
}
