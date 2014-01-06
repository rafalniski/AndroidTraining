package com.androidtraining.gettingstarted.savingdata;

import com.androidtraining.R;
import com.androidtraining.R.layout;
import com.androidtraining.R.menu;
import com.androidtraining.gettingstarted.savingdata.FeedReaderContract.FeedEntry;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;

public class DatabaseActivity extends Activity {
		private FeedReaderDbHelper mDbHelper;
		private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		// initializing our helper class
		mDbHelper = new FeedReaderDbHelper(this);
		// getting data repository in the write mode, should be called in separate thread
		db = mDbHelper.getWritableDatabase();
		insertData();
		readData();
	}
	private void readData() {
		
		// projection of columns that you want to use after query
		String[] projection = {
				FeedEntry.COLUMN_NAME_ENTRY_ID,
				FeedEntry.COLUMN_NAME_TITLE,
				FeedEntry.COLUMN_NAME_SUBTITLE
		};
		
		String sortOrder = FeedEntry.COLUMN_NAME_TITLE + " DESC";
		Cursor c = db.query(
				FeedEntry.TABLE_NAME, // Table name
				projection, 		  // The columns for return
				null, 				  // The columns for WHERE clause
				null, 				  // The values for WHERE clause
				null, 				  // Don't group the rows
				null, 				  // Don't filter by row group
				sortOrder			  // The sort order
				);
		c.moveToFirst();
		int itemId = c.getInt(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ID));
		updateData(itemId);
		deleteData(itemId);
	}
	private void deleteData(int itemId) {
		String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		String[] selectArgs = { String.valueOf(itemId)};
		db.delete(FeedEntry.TABLE_NAME, selection, selectArgs);
	}
	private void insertData() {
		ContentValues values = new ContentValues();
		int id = 1;
		String title = "title";
		String subtitle = "subtitle";
		values.put(FeedEntry.COLUMN_NAME_ENTRY_ID,id);
		values.put(FeedEntry.COLUMN_NAME_SUBTITLE,title);
		values.put(FeedEntry.COLUMN_NAME_TITLE,subtitle);
		long newRowID;
		// second argument is a column name, that will be null when values are empty. In case arg
		// is null, than no data will be inserted.
		newRowID = db.insert(FeedEntry.TABLE_NAME,null, values);
		
	}
	private void updateData(int rowId) {
		// New value for one column
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_TITLE, "title");

		// Which row to update, based on the ID
		String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(rowId) };

		int count = db.update(
		    FeedEntry.TABLE_NAME,
		    values,
		    selection,
		    selectionArgs);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
		return true;
	}

}
