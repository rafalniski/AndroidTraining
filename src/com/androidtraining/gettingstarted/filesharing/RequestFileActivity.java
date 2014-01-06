package com.androidtraining.gettingstarted.filesharing;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

import com.androidtraining.R;
import com.androidtraining.R.id;
import com.androidtraining.R.layout;
import com.androidtraining.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RequestFileActivity extends Activity {

	private Intent mRequestFileIntent;
	private ParcelFileDescriptor mInputPFD;
	private Button click;
	private TextView nameText, sizeText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_file);
		click = (Button) findViewById(R.id.button1);
		click.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			requestFile();
		}
	});
	}
	// Call when you what to request a file
	protected void requestFile() {
		mRequestFileIntent = new Intent(Intent.ACTION_PICK);
		mRequestFileIntent.setType("image/jpg");
		startActivityForResult(mRequestFileIntent, 0);
		
	}
	/** After the activity that hosted files sets result and call finish, this method is called.
	 *  The returned Intent contains URI of sent file.
	 *  The result code indicates if selection worked or not
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK) {
			return;
		} else {
			/** Getting URI of the sent file from Intent **/
			Uri returnUri = data.getData();
			/** getting MIME type of the file **/
			String mimeType = getContentResolver().getType(returnUri);
			/** To get file contents and info we have to query file using its URI **/
			Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
			int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
			int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
			returnCursor.moveToFirst();
			
			nameText = (TextView) findViewById(R.id.name);
			sizeText = (TextView) findViewById(R.id.size);
			String name = returnCursor.getString(nameIndex);
			int size = returnCursor.getInt(sizeIndex);
			nameText.setText(name);
			sizeText.setText(Integer.toString(size));
			try {
				mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
			} catch (FileNotFoundException e) {
				// TODO: handle exception
			}
			/** W created FileDescriptor of file which can be used
			 * as constructor argument of FileOutputStream or FileInputStream 
			 */
			FileDescriptor fd = mInputPFD.getFileDescriptor();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.request_file, menu);
		return true;
	}

}
