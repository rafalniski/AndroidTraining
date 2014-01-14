package com.androidtraining.graphics.animation;

import com.androidtraining.R;
import com.androidtraining.R.id;
import com.androidtraining.R.layout;
import com.androidtraining.R.menu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CardFlipActivity extends Activity implements FragmentManager.OnBackStackChangedListener  {

	private boolean mShowingBack = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_flip);
		  if (savedInstanceState == null) {
			  getFragmentManager().beginTransaction().add(R.id.container, new CardFrontFragment()).commit();
		  }
	           
	            
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		flipCard();
		return super.onTouchEvent(event);
	}
		  private void flipCard() {
			    if (mShowingBack) {
			        getFragmentManager().popBackStack();
			        return;
			    }

			    // Flip to the back.

			    mShowingBack = true;

			    // Create and commit a new fragment transaction that adds the fragment for the back of
			    // the card, uses custom animations, and is part of the fragment manager's back stack.

			    getFragmentManager()
			            .beginTransaction()

			            // Replace the default fragment animations with animator resources representing
			            // rotations when switching to the back of the card, as well as animator
			            // resources representing rotations when flipping back to the front (e.g. when
			            // the system Back button is pressed).
			            .setCustomAnimations(
			                    R.layout.card_flip_right_in, R.layout.card_flip_right_out,
			                    R.layout.card_flip_left_in, R.layout.card_flip_left_out)

			            // Replace any fragments currently in the container view with a fragment
			            // representing the next page (indicated by the just-incremented currentPage
			            // variable).
			            .replace(R.id.container, new CardBackFragment())

			            // Add this transaction to the back stack, allowing users to press Back
			            // to get to the front of the card.
			            .addToBackStack(null)

			            // Commit the transaction.
			            .commit();
			}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.card_flip, menu);
		return true;
	}
	public class CardFrontFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.card_front, container, false);
        }
    }

    /**
     * A fragment representing the back of the card.
     */
	public class CardBackFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.card_back, container, false);
        }
    }

	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub
		
	}
}
