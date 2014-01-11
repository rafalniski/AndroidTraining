package com.androidtraining.graphics.bitmaps;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.Menu;
import android.widget.ImageView;

import com.androidtraining.R;

public class LoadingBitmapsActivity extends Activity {

	private ImageView mImageView;
	private Bitmap mLoadingBitmap;
	private LruCache<String, Bitmap> mMemoryCache;
	private DiskLruCache mDiskLruCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_bitmaps);
		// get max available VM memory, exceeding this amout will throw
		// an outofmemory exception.Stored in kbytes as LruCahce takes an int in its constructor
		final int maxMemory = (int) Runtime.getRuntime().maxMemory()/1024;
		// using 1/8th available memory for this cache
		final int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// the cache size will be measured in kb rather tan number of items
				return bitmap.getByteCount() / 1024;
			}
		};
		
		mImageView = (ImageView) findViewById(R.id.mImageView);
		// sample usage
		//getDrawable(resId)
		
	}
	public void loadBitmap(int resId, ImageView imageView) {
	    final String imageKey = String.valueOf(resId);

	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	        mImageView.setImageBitmap(bitmap);
	    } else {
	        mImageView.setImageResource(R.drawable.ic_launcher);
	        BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
	        task.execute(resId);
	    }
	}
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if(getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}
	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	public void getDrawable(int resId) {
		if(cancelPotentialWork(mImageView)) {
			BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), mLoadingBitmap, task);
			mImageView.setImageDrawable(asyncDrawable);
			task.execute(mImageView.getId());
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loading_bitmaps, menu);
		return true;
	}
	
	/**
	 * Checks if another running task is already assosiated with the ImageView
	 * @param data
	 * @param imageView
	 * @return
	 */
	private static boolean cancelPotentialWork(ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if(bitmapWorkerTask != null) {
			final int bitmapData = bitmapWorkerTask.data;
			if(bitmapData != imageView.getId()) {
				// cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// no task assosiated with the ImageView
		return true;
	}
	
	
	private static BitmapWorkerTask getBitmapwoBitmapWorkerTask(ImageView imageView) {
		if(imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if(drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}
	/**
	 * This method uses inJustDecodeBounds = true. So that, returned bitmap is null
	 * and no memory is allocated. Altought bitmap properties such as dimenssion, height, width or type
	 * are properly set.
	 * 
	 */
	private void getBitmapDimensionsAndType() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;
		
	}
	/**
	 * Method for calculating proper scaling factor.
	 * @param bitmapOptions - bitmap options
	 * @param reqHeight - height after scaling
	 * @param reqWidth - width after scaling
	 * @return inSampleSize - BitmapFactory.Options - value wich decoder will use to scale the bitmap. Always power of 2.
	 */
	private static int calculateInSampleSize(BitmapFactory.Options bitmapOptions, int reqHeight, int reqWidth) {
		// raw height and width of the image
		final int height = bitmapOptions.outHeight;
		final int width = bitmapOptions.outWidth;
		int inSampleSize = 1;
		if( height > reqHeight || width > reqWidth) {
			
			final int halfWidth = width / 2;
			final int halfHeight = height / 2;
			
			// Calculate the largest inSampleSize value that is 
			// power of 2 and keeps both height and width larger 
			// than the requested height and width
			while((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
	/**
	 * This method shows proper way to decode bitmap from resources.
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static Bitmap decodeSampleBitmapfromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		
		// first decode with injustDecodeBounds = true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		
		// calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
		
		
		// decode bitmap with inSampleSize set, remember to
		// set inJustDecodeBounds to false
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncDrawable) {
		           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		           return asyncDrawable.getBitmapWorkerTask();
		       }
		    }
		    return null;
	}
	
	private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;
		private int data = 0;
		
		public BitmapWorkerTask(ImageView imageView) {
			// there is no guarantee that ImageView is still around when the task finishes
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(Integer... params) {
			data = params[0];
			final Bitmap bitmap = decodeSampleBitmapfromResource(getResources(), data, 200, 200);
			addBitmapToMemoryCache(String.valueOf(data), bitmap);
			return bitmap;
		}
		// Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	    	if(isCancelled()) {
	    		bitmap = null;
	    	}
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
	
	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
		
		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}
		
		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}
}
