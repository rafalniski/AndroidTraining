package com.androidtraining.multimedia;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.view.Menu;

import com.androidtraining.R;
/**
 * 
 * Activity for controlling audio focus, listening for broadcast, gaining audiofocus 
 * @author rafalniski
 *
 */
public class SetVolumeControlActivity extends Activity {
	private AudioManager am;
	private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
	private RemoteControlReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_volume_control);
		/** Setting listener for specific audio types **/
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		receiver = new RemoteControlReceiver();
		am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		/** Start listening for button clicks **/
		am.registerMediaButtonEventReceiver(new ComponentName(
				getPackageName(), RemoteControlReceiver.class.getName()));
		/** Stop listening for button press **/
		am.unregisterMediaButtonEventReceiver(new ComponentName(
				getPackageName(), RemoteControlReceiver.class.getName()));
		
	}
	private void chceckHardware() {
		if (am.isBluetoothA2dpOn()) {
		    // Adjust output for Bluetooth.
		} else if (am.isSpeakerphoneOn()) {
		    // Adjust output for Speakerphone.
		} else if (am.isWiredHeadsetOn()) {
		    // Adjust output for headsets
		} else { 
		    // If audio plays and noone can hear it, is it still playing?
		}
	}
	OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		// this is how to check if we loose playback
		@Override
		public void onAudioFocusChange(int focusChange) {
			if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
				// pause playback
			} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
				// resume plaback
			} else if(focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				am.unregisterMediaButtonEventReceiver(new ComponentName(
						getPackageName(), RemoteControlReceiver.class.getName()));
				am.abandonAudioFocus(afChangeListener);
				// stop playback
			} else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
				//lower the volume
			} 
			
		}
	};
	private void startPlayback() {
		registerReceiver(receiver, intentFilter);
	}
	private void stopPlayback() {
		unregisterReceiver(receiver);
	}
	// this is how to to rrquest audio plackback
	private void requestAudioFocus() {
		int result = am.requestAudioFocus(
				afChangeListener, 
				AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);
		if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			am.unregisterMediaButtonEventReceiver(new ComponentName(
					getPackageName(), RemoteControlReceiver.class.getName()));
			//start playback;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_volume_control, menu);
		return true;
	}

	private class NoisyAudioStreamReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
				// pause playback
			}
			
		}
		
	}
}
