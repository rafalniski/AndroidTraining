package com.androidtraining.multimedia.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

public class RemoteControlReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		if(Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
			KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
				//time to handle a button click
			
			}
		}
		if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
			//pause the playback
		}

	}

}
