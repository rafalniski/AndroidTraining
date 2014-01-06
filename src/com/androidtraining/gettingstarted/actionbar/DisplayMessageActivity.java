package com.androidtraining.gettingstarted.actionbar;

import com.androidtraining.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;
/**
 * Activity display message, that was passed with intent by the previous activity
 * @author rafalniski
 *
 */
public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaymessage);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		String message = intent.getStringExtra(SimpleUserInterfaceActivity.EXTRA_MESSAGE);
		TextView messageTextView = (TextView) findViewById(R.id.message);
		messageTextView.setText(message);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		int firstInt = savedInstanceState.getInt("firstInt");
		int secondInt = savedInstanceState.getInt("secondInt");
		
	}
}
