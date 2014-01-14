package com.androidtraining.graphics.animation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.androidtraining.R;

public class ScreenSlidePagerActivity extends FragmentActivity {

	private PagerAdapter mPagerAdapter;
	private ViewPager mPager;
	 /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;
	@Override
	public void onBackPressed() {
		if(mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.viewpager);
		mPager =  (ViewPager) findViewById(R.id.pager);
		mPager.setPageTransformer(true, new DepthPageTransformer());
		
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
	}
	 private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	        public ScreenSlidePagerAdapter(FragmentManager fm) {
	            super(fm);
	        }

	        @Override
	        public Fragment getItem(int position) {
	        	//mPager.setTranslationX(-1 * mPager.getWidth() * position);
	            return new ScreenSlidePageFragment();
	        }

	        @Override
	        public int getCount() {
	            return NUM_PAGES;
	        }
	    }

}
