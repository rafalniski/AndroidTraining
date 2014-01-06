package com.androidtraining.gettingstarted.nfc;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import com.androidtraining.R;

public class SendingFileByNFCActivity extends Activity {
	private NfcAdapter mNfcAdapter;
	private Uri[] mFileUris = new Uri[10];
	boolean mAndroidBeamAvailable = false;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sending_file_by_nfc);
		if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
			//disable NFC functionality here 
		} else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			mAndroidBeamAvailable = false;
			//disable android beam here
		} else {
			mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
			FileUriCallBack mFileUriCallback = new FileUriCallBack();
			mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback, this);
		}
	}
	@SuppressLint("NewApi")
	private class FileUriCallBack implements NfcAdapter.CreateBeamUrisCallback {

		public FileUriCallBack() {
			
		}
		
		@Override
		public Uri[] createBeamUris(NfcEvent event) {
			String transferFile = "transferimage.jpg";
			File extDir = getExternalFilesDir(null);
			File requestFile = new File(extDir,transferFile);
			requestFile.setReadable(true, false);
			Uri fileUri = Uri.fromFile(requestFile);
			if(fileUri != null) {
				mFileUris[0] = fileUri;
			}
			// TODO Auto-generated method stub
			return mFileUris;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sending_file_by_nfc, menu);
		return true;
	}

}
