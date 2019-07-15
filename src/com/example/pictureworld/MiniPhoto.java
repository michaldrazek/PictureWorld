package com.example.pictureworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MiniPhoto extends RelativeLayout {

	public MiniPhoto(Context context, int width, int height, Bitmap picture) {
		super(context);
	
		ImageView iv = new ImageView(this.getContext());
		//iv.getLayoutParams().width = size;
		//iv.getLayoutParams().height = size;
		//iv.setImageBitmap(ImageUtils.scaleBitmap(picture, size, size));
		iv.setImageBitmap(picture);
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		iv.setLayoutParams(params);
		this.addView(iv);
		//
		//this.getLayoutParams().width = size;
		//this.getLayoutParams().height = size;
	}
}
