package com.androidtraining.graphics.bitmaps;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.androidtraining.R;
/**
 * Class is loading bitmap from Uri, resizing it and putting in LRU memory cache.
 * This is reference usage for putting images into ListView or GridVIew
 * @author rafalniski
 *
 */
public class EffectiveBitmapLoader {

	private Bitmap mLoadingBitmap;
	private Context context;
	private LruCache<String, Bitmap> mMemoryCache;
	private DiskLruCache mDiskLruCache;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";
	public EffectiveBitmapLoader(Context ctx) {
		this.context = ctx;
		mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		/*
		 * Initiating memory cache
		 */
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
	}
	private void addBitmapToMemCache(String key, Bitmap bitmap) {
		if(getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}
	private Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	public void loadBitmap(Uri resId, ImageView imageView, Bitmap loadingBitmap) {
		if(cancelPotentialWork(resId, imageView)) {
			final String imageKey = String.valueOf(resId);
			final Bitmap bitmap = getBitmapFromMemCache(imageKey);
			if(bitmap != null) {
		        imageView.setImageBitmap(bitmap);
			} else {
				BitmapWorkerTask task = new BitmapWorkerTask(imageView);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), loadingBitmap, task);
				imageView.setImageDrawable(asyncDrawable);
				task.execute(resId);
			}
		}
	}
	
	/**
	 * Checks if another running task is already assosiated with the ImageView
	 * @param data
	 * @param imageView
	 * @return
	 */
	private static boolean cancelPotentialWork(Uri data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if(bitmapWorkerTask != null) {
			final Uri bitmapData = bitmapWorkerTask.data;
			if(bitmapData != data) {
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
		BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher, options);
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
	 * @throws IOException 
	 */
	private static Bitmap decodeSampleBitmapfromResource(Resources res, Uri resId, int reqWidth, int reqHeight) throws IOException {
		
		// first decode with injustDecodeBounds = true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		URL url = new URL(resId.toString());
		BitmapFactory.decodeStream(url.openConnection().getInputStream(),null, options);
		//BitmapFactory.decode(res, resId, options);
		
		// calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth);
		
		
		// decode bitmap with inSampleSize set, remember to
		// set inJustDecodeBounds to false
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(url.openConnection().getInputStream(),null, options);
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
	
	private class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;
		private Uri data = null;
		
		public BitmapWorkerTask(ImageView imageView) {
			// there is no guarantee that ImageView is still around when the task finishes
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(Uri... params) {
			data = params[0];
			Bitmap bitmap = null;
			try {
				bitmap = decodeSampleBitmapfromResource(context.getResources(), data, 200, 200);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addBitmapToMemCache(String.valueOf(data), bitmap);
			Log.i("Memory cache", "Memory cache: " + mMemoryCache.toString());
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
	            if (this == bitmapWorkerTask && imageView != null) {
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
