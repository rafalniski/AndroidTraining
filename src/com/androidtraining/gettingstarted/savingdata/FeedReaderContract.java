package com.androidtraining.gettingstarted.savingdata;

import android.provider.BaseColumns;

public class FeedReaderContract {
	public FeedReaderContract() {}
	
	/* This is an inner class representing single table.
	 * By implemening BaseColumns we gain consistency by having _ID and _COUNT in every row
	 * */
	public static abstract class FeedEntry implements BaseColumns {
		public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
	}
	public static final String TEXT_TYPE = " TEXT";
	public static final String COMMA_SEP = ",";
	public static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
	    FeedEntry._ID + 					"INTEGER PRIMARY KEY," 			  +
	    FeedEntry.COLUMN_NAME_ENTRY_ID + 	TEXT_TYPE + 			COMMA_SEP +
	    FeedEntry.COLUMN_NAME_TITLE + 		TEXT_TYPE + 			COMMA_SEP +
	    FeedEntry.COLUMN_NAME_SUBTITLE + 	TEXT_TYPE + 			
	    ")";

	public static final String SQL_DELETE_ENTRIES =
	    "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
