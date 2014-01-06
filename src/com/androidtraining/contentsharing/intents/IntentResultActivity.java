package com.androidtraining.contentsharing.intents;

import com.androidtraining.R;
import com.androidtraining.R.layout;
import com.androidtraining.R.menu;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
/**
 * Activity for getting some intent and handling returned data
 * @author rafalniski
 *
 */
public class IntentResultActivity extends Activity {

	private static final int PICK_CONTACT_REQUEST = 1; // request code
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_result);
		pickContact();
	}
	private void pickContact() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
		pickContactIntent.setType(Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PICK_CONTACT_REQUEST) {
			if(resultCode == RESULT_OK) {
				// Getting number from people App
				Uri contactUri = data.getData();
				String[] projection = {Phone.NUMBER};
				Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
				cursor.moveToFirst();
				
				int column = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(column);
				// Do sth with the number.
				
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intent_result, menu);
		return true;
	}

}
