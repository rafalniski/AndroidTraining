package com.androidtraining.gettingstarted.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidtraining.R;

/**
 * Activity class
 * On button click it starts new activity. It also
 * sends text via intent as extra.
 * 
 * @author rafalniski
 * 
 */
public class SimpleUserInterfaceActivity extends Activity {
	private Button sendButton;
	private Intent intent;
	public static final String EXTRA_MESSAGE = "com.androidtraining.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simpleuserinterface);
		/** Initialize button and set click listener **/
		sendButton = (Button) findViewById(R.id.send_message);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(SimpleUserInterfaceActivity.this, DisplayMessageActivity.class);
				EditText editMessage = (EditText) findViewById(R.id.edit_message);
				/** Adding values to the intent extras **/
				String message = editMessage.getText().toString();
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/** Adding values to be saved before killing activity, it's called before onStop **/
		int firstSampleInt = 5;
		int secondSampleInt = 10;
		outState.putInt("firstInt", firstSampleInt);
		outState.putInt("secondInt", secondSampleInt);
		super.onSaveInstanceState(outState);
	}
}
