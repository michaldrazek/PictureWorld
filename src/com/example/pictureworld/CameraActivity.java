package com.example.pictureworld;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;


public class CameraActivity extends Activity {
	
	private Camera.Parameters camParams;
	String path;
	Camera camera;
	int cameraId = -1;
	CameraPreview camPrev;
	FrameLayout frLay;
	//
	private RelativeLayout relPic;
	private RelativeLayout relConfirm;
	private RelativeLayout relAbort;
	//
	byte[] fdata;
	//
	private RelativeLayout exposureRel;
	private RelativeLayout colorRel;
	private RelativeLayout resRel;
	private RelativeLayout whiteRel;
	private RelativeLayout flashRel;
	//
	private List <byte[]> zdjecia;
	private final int miniatureSize = 200;
	private int statusBarHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		statusBarHeight = getStatusBarHeight();
		Bundle bundle = getIntent().getExtras();
		path = bundle.getString("path").toString();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);
		if(initCamera()){
			initPreview();
			checkCamParams();
		} else {
			Toast.makeText(this, "Nie uda³o siê zainicjalizowaæ kamery", Toast.LENGTH_SHORT).show();
		}
	}
	
	public int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	} 
	
	private void initPreview(){
		zdjecia = new ArrayList<byte[]>();
		camPrev = new CameraPreview(CameraActivity.this, camera);
		frLay = (FrameLayout) findViewById(R.id.frameLayout1);
		frLay.addView(camPrev);
		//temp kolo
		Kolo koleczko = new Kolo(CameraActivity.this);
		frLay.addView(koleczko);
		//temp miniturka
		
		
		relPic = (RelativeLayout) findViewById(R.id.relPic);
		relPic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				camera.takePicture(null, null, camPictureCallback);
	    		
			}
		});
		
		relConfirm = (RelativeLayout) findViewById(R.id.relConfirm);
		relConfirm.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				if(fdata==null){
					Toast.makeText(CameraActivity.this, "Najpierw zrób zdjêcie.", Toast.LENGTH_SHORT).show();					
					
				} else {
					
					AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
				    builder.setTitle("Wybór katalogów");
				    builder.setIcon(R.drawable.folder);
			         File parentFolder = new File(path);
				    //builder.setIcon(R.drawable.icon);
			        List <String> temptab = new ArrayList<String>();
			        for(File file : parentFolder.listFiles()){
			        	if(file.isDirectory())
			        		temptab.add(file.getName());			        	
			        }			      
			        
			        final String[] aryj = new  String[temptab.size()];
			        
				    builder.setItems(temptab.toArray(aryj), new DialogInterface.OnClickListener() {                
				        public void onClick(DialogInterface dialog, int which) {                    
				            // wyswietl opcje[which]);
				        	//========== pobranie daty aby zdjêcie mia³o w nazwie datê

						    SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
							String d = dFormat.format(new Date());
							
							//zapisz d w nazwie zdjêcia
							File myFoto = new File(path+"/"+aryj[which]+"/"+d+".jpg");

							// zapis danych zrobionego foto - konieczne dodac try catch
							try {
								FileOutputStream fs = new FileOutputStream(myFoto);
								ImageUtils.decodeRotateAndCreateBitmap(fdata).compress(Bitmap.CompressFormat.JPEG, 95, fs);	
								fs.close();
								Toast.makeText(CameraActivity.this, "Zapisano pomyœlnie.", Toast.LENGTH_LONG).show();
							} catch (Exception ex){					
								Toast.makeText(CameraActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
							}        
				        }
				    });
				    
				    builder.setNeutralButton("Nowy folder",
				            new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int id) {
				                	AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
				                	alert.setTitle("Nowy katalog");
				                	alert.setIcon(R.drawable.new_folder);
				                	
				                	final EditText input = new EditText(CameraActivity.this);
				                	input.setText("Nowy Folder");
				                	alert.setView(input);

				                	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
				                	    public void onClick(DialogInterface dialog, int which) {
				                	    	File newDir = new File(path + "/" + input.getText());
				                	    	newDir.mkdir();
				                	    }
				                	                    
				                	});

				                	//no
				                	alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {		                	              
				                	    public void onClick(DialogInterface dialog, int which) {
				                	    	dialog.cancel();
				                	    }
			                	    });
				                	alert.show();
				                    //context.startActivity(new Intent(context, Setup.class));
				                    //dialog.cancel();

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
			}
		});
		
		relAbort = (RelativeLayout) findViewById(R.id.relAbort);
		relAbort.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(fdata==null){
					Toast.makeText(CameraActivity.this, "Najpierw zrób zdjêcie.", Toast.LENGTH_SHORT).show();										
				} else {
					fdata = null;
					Toast.makeText(CameraActivity.this, "Usuniêto zdjêcie.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	public void checkCamParams(){
		camParams = camera.getParameters();
	
		if(camParams==null){
			Toast.makeText(CameraActivity.this, "Nie uda³o siê pobraæ parametrów kamery.", Toast.LENGTH_LONG).show();
		} else{
					
			exposureRel = (RelativeLayout) findViewById(R.id.exposureRel);
			colorRel = (RelativeLayout) findViewById(R.id.colorRel);
			resRel = (RelativeLayout) findViewById(R.id.resRel);
			whiteRel = (RelativeLayout) findViewById(R.id.whiteRel);
			flashRel = (RelativeLayout) findViewById(R.id.flashRel);
			
			if(camParams.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
				camParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				camera.setParameters(camParams);
			}
			
			exposureRel.setOnClickListener(new View.OnClickListener() {			
				
				@Override
				public void onClick(View v) {
					final Integer[] zimiok = new Integer[Math.abs(camParams.getMaxExposureCompensation()-camParams.getMinExposureCompensation())+1];
					List<String> namePlates = new ArrayList <String>();
					
					for(int i=0; i<zimiok.length; i++){
						zimiok[i]=camParams.getMaxExposureCompensation()-i;
						namePlates.add(zimiok[i].toString());
					}
					
					AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
					alert.setTitle("Kompensacja naœwietlania");
					//nie mo¿e mieæ setMessage!!!	
					final String[] tab = new String[namePlates.size()];	
					alert.setItems(namePlates.toArray(tab), new DialogInterface.OnClickListener() {                
					    public void onClick(DialogInterface dialog, int which) {
					    	camParams.setExposureCompensation(zimiok[which]);
					    	camera.setParameters(camParams);     
					    }
					});
					//
					alert.show();
				
				}				
			});	
			
			colorRel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(camParams.getSupportedColorEffects()!=null){
						List<String> zimiok = new ArrayList <String>();
						zimiok = camParams.getSupportedColorEffects();
						AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
						alert.setTitle("Efekty Koloru");
						//nie mo¿e mieæ setMessage!!!
						final String[] tab = new String[zimiok.size()];		
						alert.setItems(zimiok.toArray(tab), new DialogInterface.OnClickListener() {                
						    public void onClick(DialogInterface dialog, int which) {
						    	camParams.setColorEffect(tab[which]);
						    	camera.setParameters(camParams);     
						    }
						});
						//
						alert.show();
					}else{
						Toast.makeText(CameraActivity.this, "Nie obs³ugiwane.", Toast.LENGTH_SHORT).show();
					}
				}
			});		
			
			resRel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String[] zimiok = new String[camParams.getSupportedPictureSizes().size()];

					for(int i=0; i<camParams.getSupportedPictureSizes().size(); i++)
					{
						zimiok[i]=camParams.getSupportedPictureSizes().get(i).width +"x"+ camParams.getSupportedPictureSizes().get(i).height;
					}

					AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
					alert.setTitle("Rodzielczoœæ");
					alert.setItems(zimiok, new DialogInterface.OnClickListener() {                
					    public void onClick(DialogInterface dialog, int which) {
					    	camParams.setPictureSize(camParams.getSupportedPictureSizes().get(which).width, camParams.getSupportedPictureSizes().get(which).height);
					    	camera.setParameters(camParams); 					   	
					    }
					});
					//
					alert.show();
				}
			});
			
			whiteRel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(camParams.getSupportedWhiteBalance()!=null){
						List<String> zimiok = new ArrayList <String>();
						zimiok = camParams.getSupportedWhiteBalance();
						AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
						alert.setTitle("Balans bieli");
						//nie mo¿e mieæ setMessage!!!
						final String[] tab = new String[zimiok.size()];		
						alert.setItems(zimiok.toArray(tab), new DialogInterface.OnClickListener() {                
						    public void onClick(DialogInterface dialog, int which) {
						    	camParams.setWhiteBalance(tab[which]);
						    	camera.setParameters(camParams);     
						    }
						});
						//
						alert.show();
					}else{
						Toast.makeText(CameraActivity.this, "Nie obs³ugiwane.", Toast.LENGTH_SHORT).show();
					}
				}
			});
		
			flashRel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(camParams.getSupportedFlashModes()!=null){
						List<String> zimiok = new ArrayList <String>();
						zimiok = camParams.getSupportedFlashModes();			
						AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
						alert.setTitle("Tryb lampy");
						//nie mo¿e mieæ setMessage!!!
						final String[] tab = new String[zimiok.size()];		
						alert.setItems(zimiok.toArray(tab), new DialogInterface.OnClickListener() {                
						    public void onClick(DialogInterface dialog, int which) {
						    	camParams.setFlashMode(tab[which]);
						    	camera.setParameters(camParams);     
						    }
						});
						alert.show();
					}else{
						Toast.makeText(CameraActivity.this, "Nie obs³ugiwane.", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		  
	}
	
	private void addMiniature (Miniaturka min){
		final DisplayMetrics displaymetrics = new DisplayMetrics();
		(CameraActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		frLay = (FrameLayout) findViewById(R.id.frameLayout1);	
		final Miniaturka smallMin = new Miniaturka(CameraActivity.this, fdata, miniatureSize);
 		frLay.addView(smallMin);
 		smallMin.setOnLongClickListener(new View.OnLongClickListener() {		
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
			    builder.setTitle("Opcje zdjêcia");
		        List <String> temptab = new ArrayList<String>();		    
        		temptab.add("Obejrzyj zdjêcie");			        	
        		temptab.add("Usuñ zdjêcie");	
        		temptab.add("Zapisz zdjêcie");	
        		temptab.add("Usuñ wszystkie");		
        		temptab.add("Zapisz wszystkie");
		        final String[] aryj = new  String[temptab.size()];
		        
			    builder.setItems(temptab.toArray(aryj), new DialogInterface.OnClickListener() {                
			        public void onClick(DialogInterface dialog, int which) {                    
			        	switch (which){
			        		case 0:
			        			Dialog builder = new Dialog(CameraActivity.this);
			        		    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
			        		    builder.getWindow().setBackgroundDrawable(
			        		        new ColorDrawable(android.graphics.Color.TRANSPARENT));
			        		    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			        		        @Override
			        		        public void onDismiss(DialogInterface dialogInterface) {
			        		            //nothing;
			        		        }
			        		    });

			        		    ImageView imageView = new ImageView(CameraActivity.this);
			        		    imageView.setImageBitmap(ImageUtils.scaleBitmapToFit(ImageUtils.decodeRotateAndCreateBitmap(smallMin.fullPicture), displaymetrics.widthPixels, displaymetrics.heightPixels - statusBarHeight));
			        		    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
			        		            ViewGroup.LayoutParams.MATCH_PARENT, 
			        		            ViewGroup.LayoutParams.MATCH_PARENT));
			        		    builder.show();
			        			break;
			        		case 1:		  
			        			AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
			        			alert.setTitle("Usuwanie");
			        			alert.setMessage("Czy na pewno chcesz usun¹æ zdjêcie?");
			        		
			        			alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {			        			                
			        			    public void onClick(DialogInterface dialog, int which) {
			        			    	 dialog.cancel();
			        			    }
		        			    });
			        			alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {                
			        			    public void onClick(DialogInterface dialog, int which) {
			        			    	for(int i = 0; i < zdjecia.size(); i++){	        			
				        					if(zdjecia.get(i).equals(smallMin.fullPicture))
				        						zdjecia.remove(i);	        						
				        				}
				        				for(int k = 0; k < frLay.getChildCount(); k++){
				        					if(frLay.getChildAt(k).equals(smallMin)){
				        						frLay.removeViewAt(k);
				        					}		        						
				        				}
					        			refreshMiniatures();					        			
			        			    }			     			                    
			        			});			        					        		
			        			//
			        			alert.show();	
			        			break;
			        		case 2:
			        			AlertDialog.Builder folderDialog = new AlertDialog.Builder(CameraActivity.this);
							    folderDialog.setTitle("Wybór katalogów");
							    
						        File parentFolder = new File(path);
						        folderDialog.setIcon(R.drawable.folder);
						        List <String> temptab = new ArrayList<String>();
						        for(File file : parentFolder.listFiles()){
						        	if(file.isDirectory())
						        		temptab.add(file.getName());			        	
						        }			      
						        
						        final String[] aryj = new  String[temptab.size()];
						        
							    folderDialog.setItems(temptab.toArray(aryj), new DialogInterface.OnClickListener() {                
							        public void onClick(DialogInterface dialog, int which) {                    
							            // wyswietl opcje[which]);
							        	//========== pobranie daty aby zdjêcie mia³o w nazwie datê

									    SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
										String d = dFormat.format(new Date());
										
										//zapisz d w nazwie zdjêcia
										File myFoto = new File(path+"/"+aryj[which]+"/"+d+".jpg");

										// zapis danych zrobionego foto - konieczne dodac try catch
										try {
											FileOutputStream fs = new FileOutputStream(myFoto);
											ImageUtils.decodeRotateAndCreateBitmap(fdata).compress(Bitmap.CompressFormat.JPEG, 95, fs);									
											fs.flush();
											fs.close();
											
											Toast.makeText(CameraActivity.this, "Zapisano pomyœlnie.", Toast.LENGTH_LONG).show();
										} catch (Exception ex){					
											Toast.makeText(CameraActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
										}        
							        }
							    });
							    
							    folderDialog.setNeutralButton("Nowy folder",
							            new DialogInterface.OnClickListener() {
							                public void onClick(DialogInterface dialog, int id) {
							                	AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
							                	alert.setTitle("Nowy katalog");
							                	alert.setIcon(R.drawable.new_folder);
							                	//tutaj input
							                	final EditText input = new EditText(CameraActivity.this);
							                	input.setText("Nowy Folder");
							                	alert.setView(input);

							                	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
							                	    public void onClick(DialogInterface dialog, int which) {
							                	    	File newDir = new File(path + "/" + input.getText());
							                	    	newDir.mkdir();
							                	    }
							                	                    
							                	});

							                	alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {		                	              
							                	    public void onClick(DialogInterface dialog, int which) {
							                	    	dialog.cancel();
							                	    }
						                	    });
							                	alert.show();
							                }
							            });

							    folderDialog.setNegativeButton("Anuluj",
						            new DialogInterface.OnClickListener() {
						                public void onClick(DialogInterface dialog, int id) {
						                    dialog.cancel();

						                }
						            });
							    folderDialog.show();
			        			break;
		        			case 3:
		        				AlertDialog.Builder alert1 = new AlertDialog.Builder(CameraActivity.this);
			        			alert1.setTitle("Usuwanie");
			        			alert1.setMessage("Czy na pewno chcesz usun¹æ zdjêcia?");			        				        		
			        			alert1.setNegativeButton("Nie", new DialogInterface.OnClickListener() {			        			                
			        			    public void onClick(DialogInterface dialog, int which) {
			        			    	 dialog.cancel();
			        			    }
		        			    });
			        			alert1.setPositiveButton("Tak", new DialogInterface.OnClickListener() {                
			        			    public void onClick(DialogInterface dialog, int which) {
			        			    	
				        				zdjecia.clear();

				        				/*for(int k = 0; k < frLay.getChildCount(); k++){
				        					if(frLay.getChildAt(k).getClass().equals(Miniaturka.class)){
				        						frLay.removeViewAt(k);
				        					}		        						
				        				}*/
				        			    while (frLay.getChildCount()>2) 
				        			    {
						        			int z = 0;
						        			if(z==0 || z==1)
						        			{
						        				frLay.removeView(frLay.getChildAt(z+2));
						        			}else{
						        				frLay.removeView(frLay.getChildAt(z));
						        			}
						        			z++;
				        			    }

					        			refreshMiniatures();				        			
			        			    }			     			                    
			        			});
			        			alert1.show();	
			        			break;
		        			case 4:AlertDialog.Builder folderDialog1 = new AlertDialog.Builder(CameraActivity.this);
							    folderDialog1.setTitle("Wybór katalogów");
							    
						        File parentFolder1 = new File(path);
						        folderDialog1.setIcon(R.drawable.folder);
						        List <String> temptab1 = new ArrayList<String>();
						        for(File file : parentFolder1.listFiles()){
						        	if(file.isDirectory())
						        		temptab1.add(file.getName());			        	
						        }			      
						        
						        final String[] aryj1 = new  String[temptab1.size()];
						        
							    folderDialog1.setItems(temptab1.toArray(aryj1), new DialogInterface.OnClickListener() {                
							        public void onClick(DialogInterface dialog, int which) {                    
							            // wyswietl opcje[which]);
							        	//========== pobranie daty aby zdjêcie mia³o w nazwie datê
	
									    SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
										String d = dFormat.format(new Date());
										
										for(int i=0; i<zdjecia.size();i++){
										File myFoto = new File(path+"/"+aryj1[which]+"/"+d+"_"+i+".jpg");	
											try {
												FileOutputStream fs = new FileOutputStream(myFoto);
												ImageUtils.decodeRotateAndCreateBitmap(zdjecia.get(i)).compress(Bitmap.CompressFormat.JPEG, 95, fs);	
												fs.flush();
												fs.close();
	
											} catch (Exception ex){					
												Toast.makeText(CameraActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
											}	
								        }
										Toast.makeText(CameraActivity.this, "Zapisano pomyœlnie.", Toast.LENGTH_LONG).show();        
							        }
							    });
							    
							    folderDialog1.setNeutralButton("Nowy folder",
							            new DialogInterface.OnClickListener() {
							                public void onClick(DialogInterface dialog, int id) {
							                	AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
							                	alert.setTitle("Nowy katalog");
							                	alert.setIcon(R.drawable.new_folder);
							                	final EditText input = new EditText(CameraActivity.this);
							                	input.setText("Nowy Folder");
							                	alert.setView(input);
							                	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {                
							                	    public void onClick(DialogInterface dialog, int which) {
	
							                	    	File newDir = new File(path + "/" + input.getText());
							                	    	newDir.mkdir();
							                	    }
							                	                    
							                	});
	
							                	alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {		                	              
							                	    public void onClick(DialogInterface dialog, int which) {
							                	    	dialog.cancel();
							                	    }
						                	    });
							                	alert.show();
							                }
							            });
	
							    folderDialog1.setNegativeButton("Anuluj",
						            new DialogInterface.OnClickListener() {
						                public void onClick(DialogInterface dialog, int id) {
						                    dialog.cancel();
	
						                }
						            });
							    folderDialog1.show();	
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

				return false;
			}
		});
	}
	
	private void refreshMiniatures(){
		int count = 0;
		final DisplayMetrics displaymetrics = new DisplayMetrics();
		(CameraActivity.this).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		frLay = (FrameLayout) findViewById(R.id.frameLayout1);		
 		
		int x = (displaymetrics.widthPixels - miniatureSize) / 2;	     
        int y = (displaymetrics.heightPixels - miniatureSize) / 2; 
        double r = displaymetrics.widthPixels * 0.35;
        int kat = zdjecia.size()==0?0:360/zdjecia.size();
        for(int i = 0; i < frLay.getChildCount(); i++){
        	if(frLay.getChildAt(i).getClass() == Miniaturka.class){    		
        		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(miniatureSize, miniatureSize);
    		
    			params.topMargin = (int) (y - (r*Math.sin(Math.toRadians(kat)*count)));
        		params.leftMargin = (int) (x + (r*Math.cos(Math.toRadians(kat)*count)));        		
        		frLay.getChildAt(i).setLayoutParams(params);
        		count++;
        	}
        }
        
	}
	
	private PictureCallback camPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            fdata = data;    
            zdjecia.add(fdata);
            
            addMiniature(new Miniaturka(CameraActivity.this, fdata, miniatureSize));   		
    		refreshMiniatures();
    		
            // odswiez kamerê (zapobiega przyciêciu siê kamery po zrobieniu zdjêcia)		
            camera.startPreview();
        }
	};
	
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
	
	private boolean initCamera(){
		boolean cam = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

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
	
}
