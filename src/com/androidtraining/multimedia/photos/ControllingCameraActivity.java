package com.androidtraining.multimedia.photos;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.Menu;

import com.androidtraining.R;

public class ControllingCameraActivity extends Activity {
	private Camera mCamera;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controlling_camera);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.controlling_camera, menu);
		return true;
	}
	
	private void releaseCameraAndPreview() {
		//mPreview.setCamera(null);
		if(mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	/**
	 * Since API 9 you can have multiple cameras;
	 * Call open() without id will open first rear-facing camera
	 * @param id
	 * @return
	 */
	private boolean safeCameraOpen(int id) {
		boolean qOpened = false;
		try {
			releaseCameraAndPreview();
			mCamera = Camera.open(id);
			qOpened = (mCamera != null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return qOpened;
	}
	

}
