package com.androidtraining.multimedia.photos;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.widget.ImageView;

import com.androidtraining.R;

public class CameraActivity extends Activity {

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_VIDEO_CAPTURE = 2;

	private ImageView imageView;
	private String mCurrentPhotoPath;
	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storagedDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpg",storagedDir);
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		/** SUPER-IMPORTANT
		 *  Write this condition every time you are requesting some intent.
		 *  In this case it checks if there are any app for handling taking pictures.
		 *  Without this condition program will cash when there is no app for camera.
		 */
		if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
			/** Handling thumbnail image **/
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			/** Handling full-size image **/
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
		
	}
	/** Scaling images in order to save allocated memory on a heap **/
	private void setPic() {
		// get the dimension of the view
		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();
		
		// get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		//determine how much to scale down the pic
		int scaleFactor = Math.min(photoW/targetW,photoH/targetH);
		
		// decode the image file into a Bitmap sized to fill the view
		
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
		
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
		imageView.setImageBitmap(bitmap);
	}
	private void dispatchTakeVideoIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if(takeVideoIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			/** Handling thumbnail image **/
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			//do something with the bitmap
		} else if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
			Uri resultVideo = data.getData();
			// videoView.setVideoURI(videoUri);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		imageView = (ImageView) findViewById(R.id.imageView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

}
