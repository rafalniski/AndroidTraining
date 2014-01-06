package com.androidtraining.gettingstarted.savingdata;

import com.androidtraining.R;
import com.androidtraining.R.layout;
import com.androidtraining.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;

public class SharedPreferencesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared_preferences);
		// opening preferences for reading or writing
		SharedPreferences preferences = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		//writing to SharedPreferences
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("defaultInt", 10);
		editor.putString("defaultString", "default");
		editor.commit();
		
		//reading for SharedPreferences
		
		int defaultInt = preferences.getInt("defaultInt", 0); //first value is saved one, the second is default one
		String defaultString = preferences.getString("defaultString", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shared_preferences, menu);
		return true;
	}

}
