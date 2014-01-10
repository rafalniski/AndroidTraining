package com.androidtraining.multimedia.printing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.view.Menu;
import android.webkit.WebView;

import com.androidtraining.R;

public class PrintingImagesActiivty extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printing_images_actiivty);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.printing_images_actiivty, menu);
		return true;
	}
	/*
	 * This method can be called as an menu item action.
	 * This is all code needed for printing image. After calling, proper
	 * print UI appears, allowing user to select printer and options.
	 */
	private void doPhotoPrint() {
		PrintHelper photoPrinter = new PrintHelper(this);
		photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		photoPrinter.printBitmap("ic_launcher - test print", bitmap);
		
	}


}
