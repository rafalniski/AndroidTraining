package com.androidtraining.gettingstarted.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.androidtraining.R;
/**
 * Activity implements actionbar with share button 
 * @author rafalniski
 *
 */
public class ActionBarActivity extends Activity {

	private ShareActionProvider mShareActionProvider;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_bar, menu);
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		if(mShareActionProvider != null) {
			Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_TEXT, "kkaka");
	        shareIntent.setType("text/plain");
	        mShareActionProvider.setShareIntent(shareIntent);
		}
		return true;
	}
	private void openSearch() {}
	private void openSettings() {}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	private void setShareIntent(Intent shareIntent) {
		if(mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

}
