package com.androidtraining.gettingstarted.intents;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import com.androidtraining.R;
/**
 * Activity for handling specific intent types 
 * @author rafalniski
 *
 */
public class IntentFilterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_filter);
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		Uri data = intent.getData();
		
		if(type.startsWith("image/")) {
			// handle intents with image data
			Intent result = new Intent("com.androidtraining.RESULT_ACTION", Uri.parse("content://result_uri"));
			setResult(Activity.RESULT_OK, result);
			finish();
		} else if (type.startsWith("text/plain")) {
			// handle intents with text
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intent_filter, menu);
		return true;
	}

}
