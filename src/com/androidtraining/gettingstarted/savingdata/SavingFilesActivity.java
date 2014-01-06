package com.androidtraining.gettingstarted.savingdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;

import com.androidtraining.R;

public class SavingFilesActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saving_files);
		//internal storage
		operateInternalStorage();
		//external storage
		operateExternalStorage();
		
	}
	private boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	/* 
	 * We can save files on ExtSto as public files or private files.
	 * For public files we use:
	 * 	getExternalStoragePublicDirectory()
	 * For private files we use:
	 * 	getExternalFilesDir();
	 * 
	 * 
	 * */
	private void operateExternalStorage() {
		// Firstly always check if external storage is available
		if(isExternalStorageWritable()) { //for writing and reading
			// writing public file, these files won;t be deleted after uninstalling an app
			String albumName = "album";
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),albumName);
			//checking is there is enough space
			file.getFreeSpace();
			//writing private file, these files WILL be deleted after uninstalling your app
			File prvFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),albumName);
			// if non of the pre-defined directories suits us, we can pass null as argument, then root directory will be our path
			File rootFile = new File(getExternalFilesDir(null),albumName);
			if(!file.mkdirs()) {
				Log.e("tag","Directory not created");
			}
			
			//deleting files
			file.delete();
			
		} else if (isExternalStorageReadable()) { // only for reading
			
		} else { // unavailable
			
		}
	}
	/* Saving files on internal storage 
	 * 
	 * We can choose between:
	 * - getFilesDir() - returns File representing an internal directory for an app
	 * - getCacheDir() - returns File representing internal directory for app temporary cache files.
	 * When system is on low memory it can delete cache files without warning.
	 * */
	private File operateInternalStorage() {
		// creating file
		String fileName = "SampleFileName";
		File file = new File(getFilesDir(),fileName);
		
		//writing some text to file
		String string = "HelloWorld!";
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream.write(string.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// creating file in cache directory
		try {
			File cacheFile = File.createTempFile(fileName, null, getCacheDir());
			return cacheFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//deleting file - system will find it on a internal storage
		deleteFile(fileName);
		return file;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saving_files, menu);
		return true;
	}

}
