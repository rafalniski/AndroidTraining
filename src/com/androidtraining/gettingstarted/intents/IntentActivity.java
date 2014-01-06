package com.androidtraining.gettingstarted.intents;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.Menu;

import com.androidtraining.R;
/**
 * Activity showing many methods of running intent to gain specific data.
 * @author rafalniski
 *
 */
public class IntentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent);
		
		//example of implicit intents
		
		//phone call intent
		Uri number = Uri.parse("tel:5551234");
		Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
		
		// viewing a map 
		
		// Map point based on address
		Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
		// Or map point based on latitude/longitude
		// Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
		
		// viewing webpage in webview
		
		Uri webpage = Uri.parse("http://www.android.com");
		Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
		
		// sending an email with attachement
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		// The intent does not have a URI, so declare the "text/plain" MIME type
		emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
		emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
		// You can also attach multiple items by passing an ArrayList of Uris
		
		// creating a calendar event - works only above ICS (4.0)
		Intent calendarIntent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI);
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(2012, 0, 19, 7, 30);
		Calendar endTime = Calendar.getInstance();
		endTime.set(2012, 0, 19, 10, 30);
		calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
		calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
		calendarIntent.putExtra(Events.TITLE, "Ninja class");
		calendarIntent.putExtra(Events.EVENT_LOCATION, "Secret dojo");
		
		// checking if there are any apps for running event action
		
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(calendarIntent, 0);
		boolean isIntentSafe = activities.size() > 0;
		if(isIntentSafe) {
			startActivity(calendarIntent);
		}
		
		// showing app chooser 
		String title = "Choose app for sharing via";
		Intent chooser = Intent.createChooser(calendarIntent, title);
		if(calendarIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(chooser);
		}
		
		// Sending multiple images
		ArrayList<Uri> imageUris = new ArrayList<Uri>();
		imageUris.add(Uri.parse("pathtoimage")); // Add your image URIs here
		imageUris.add(Uri.parse("pathtoimage2"));

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
		shareIntent.setType("image/*");
		startActivity(Intent.createChooser(shareIntent, "Share images to.."));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intent, menu);
		return true;
	}

}
