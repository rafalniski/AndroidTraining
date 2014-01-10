package com.androidtraining.multimedia.printing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidtraining.R;

public class PrintingHtmlDocumentActivity extends Activity {

	private WebView mWebView;
	private void doWebViewPrint() {
		WebView webView = new WebView(this);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// always should be called in this method
				// otherwise printed document may be blank or incomplete
				createWebPrintJob(view);
				mWebView = null;
			}
		});
		// creating html document on the fly
		String htmlDoc = "<html><body><h1>Test Content</h1><p>Testing, " +
				"testing, testing...</p></body></html>";
		webView.loadDataWithBaseURL(null, htmlDoc, "text/HTML", "UTF-8", null);
		/**
		 * Placing graphics example:
		 * webView.loadDataWithBaseURL("file:///android_asset/images/", htmlBody,
         * "text/HTML", "UTF-8", null);
         * 
         * Printing existing webpage example:
         * webView.loadUrl("http://developer.android.com/about/index.html");
		 */
		// keeping reference to WebView until passing
		// the PrintDocumentAdapter to the PrintManager
		// now it is not garbage collected
		mWebView = webView;
	}
	
	@SuppressLint("NewApi")
	private void createWebPrintJob(WebView webView) {
		PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
		// get the print adapter instance
		PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
		// create a print job name and adapter instance
		String jobName = getString(R.string.app_name) + " Document";
		PrintJob printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printing_html_document);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.printing_html_document, menu);
		return true;
	}
	@SuppressLint("NewApi")
	private void doPrint() {
		PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
		String jobName = getString(R.string.app_name) + " Document";
		printManager.print(jobName, new CustomDocumentPrinting(this), null);
		
	}

}
