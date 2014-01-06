package com.androidtraining.contentsharing.filesharing;

import java.io.File;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.androidtraining.MainActivity;
import com.androidtraining.R;
/**
 * Activity for sharing file with another application.
 * To achieve this, we need to set up project a bit:
 * 1. Adding provider to manifest
 * 2. Creating xml file paths, that is used in manifest and is describing shared folders
 * @author rafalniski
 *
 */
public class FileSelectActivity extends Activity {
	private File mPrivateRootDir;
	private File mImagesDir;
	private ListView list;
	private Button done;
	File[] mImagesFiles;
	String[] mImageFileNames;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_select);
		list = (ListView) findViewById(R.id.list);
		done = (Button)  findViewById(R.id.done);
		/** Proper intent for sharing a file **/
		final Intent mResultIntent = new Intent("com.androidtraining.ACTION_RETURN_FILE");
		/** Getting path to internal storage folder **/
		mPrivateRootDir = getFilesDir();
		/** Getting files subdirectory, as declared in paths.xml **/
		mImagesDir = new File(mPrivateRootDir, "images");
		/** Getting all the files in that subdirectory **/
		mImagesFiles = mImagesDir.listFiles();
		/** We begin with canceled activity result **/
		setResult(Activity.RESULT_CANCELED, null);
		/** Getting only file names **/
		int i = 0;
		mImageFileNames = new String[mImagesFiles.length];
		for(File file : mImagesFiles) {
			mImageFileNames[i] = file.getAbsolutePath();
			i++;
		}
		/** Setting adapter showing names of the files from the directory in the list view **/
		list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mImageFileNames));
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position,
			long rowId) {
			/** File that was clicked in the list view **/
			File requestFile = new File(mImageFileNames[position]);
			Uri fileUri = null;
			try {
				/** Getting file URI using FileProvider **/
				fileUri = FileProvider.getUriForFile(FileSelectActivity.this, "com.androidtraining.fileprovider", requestFile);
			} catch (IllegalArgumentException e) {
	            Log.e("File Selector",
	                    "The selected file can't be shared: " +
	                    		mImageFileNames[position]);
			}
	         if(fileUri != null) {
	        	 /** Granting temporary read permission to the file. It is obligatory 
	        	  * for other apps to be able to read a file.
	        	  */
	        	 mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	        	 /** Setting type of a file and activity result **/
	        	 mResultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
	        	 FileSelectActivity.this.setResult(Activity.RESULT_OK, mResultIntent);
	         } else {
	        	 mResultIntent.setDataAndType(null, "");
	        	 FileSelectActivity.this.setResult(RESULT_CANCELED, mResultIntent);
	         }
	         }
		});
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();	
			}
		});	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_select, menu);
		return true;
	}

}
