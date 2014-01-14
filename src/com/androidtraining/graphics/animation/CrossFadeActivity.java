package com.androidtraining.graphics.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.androidtraining.R;

public class CrossFadeActivity extends Activity {

	private View mContentView;
	private View mLoadingView;
	private int mShortAnimationDuration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cross_fade);
		mContentView = findViewById(R.id.content);
		mLoadingView = findViewById(R.id.loading_spinner);
		mContentView.setVisibility(View.GONE);
		// retrieve and cache system's default short animation time for performance 
		mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
		Button mButton = (Button)findViewById(R.id.button);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				crossfade();
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cross_fade, menu);
		return true;
	}
	private void crossfade() {
		mContentView.setAlpha(0f);
		mContentView.setVisibility(View.VISIBLE);
		
		mContentView.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);
		mLoadingView.animate().alpha(0f).setDuration(mShortAnimationDuration).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoadingView.setVisibility(View.GONE);
				}
		});
	}

}
