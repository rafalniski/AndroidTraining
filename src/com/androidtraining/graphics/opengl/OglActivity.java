package com.androidtraining.graphics.opengl;

import com.androidtraining.R;
import com.androidtraining.R.layout;
import com.androidtraining.R.menu;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OglActivity extends Activity {

	private GLSurfaceView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//CReate a GLSurfaceView instance and set it
		// as the COntentView for this Activity
		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ogl, menu);
		return true;
	}

}
