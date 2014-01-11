package com.androidtraining.graphics.bitmaps;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.androidtraining.R;

public class ImageGridFragment extends Fragment implements OnItemClickListener {
	private ImageAdapter mAdapter;
	private ViewPager mPager;
	
	public static final String[] imageResIds = new String[] {
		"http://blog.room34.com/wp-content/uploads/underdog/logo.thumbnail.png",
		"http://joegp.com/wp-content/uploads/2013/08/week-thumbnails00041-200x200.png",
		"http://joegp.com/wp-content/uploads/2013/10/week-thumbnails-200x200.png",
		"http://blog.room34.com/wp-content/uploads/underdog/logo.thumbnail.png",
		"http://joegp.com/wp-content/uploads/2013/08/week-thumbnails00041-200x200.png",
		"http://joegp.com/wp-content/uploads/2013/10/week-thumbnails-200x200.png",
		"http://blog.room34.com/wp-content/uploads/underdog/logo.thumbnail.png",
		"http://joegp.com/wp-content/uploads/2013/08/week-thumbnails00041-200x200.png",
		"http://joegp.com/wp-content/uploads/2013/10/week-thumbnails-200x200.png",
		"http://blog.room34.com/wp-content/uploads/underdog/logo.thumbnail.png",
		"http://joegp.com/wp-content/uploads/2013/08/week-thumbnails00041-200x200.png",
		"http://joegp.com/wp-content/uploads/2013/10/week-thumbnails-200x200.png"
	};
	public ImageGridFragment() {}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAdapter = new ImageAdapter(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_grid_fragment, container, false);
        final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        return v;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		//final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
        //i.putExtra(ImageDetailActivity.EXTRA_IMAGE, position);
        //startActivity(i);

	}
	private class ImageAdapter extends BaseAdapter {
		private final Context mContext;
		public ImageAdapter(Context context) {
			super();
			mContext = context;
		}
		@Override
		public int getCount() {
			return imageResIds.length;
		}

		@Override
		public Object getItem(int position) {
			return imageResIds[position];
		}

		@Override
		public long getItemId(int position) {
            return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			ImageView imageView;
            if (convertView == null) { // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            } else {
                imageView = (ImageView) convertView;
            }
            EffectiveBitmapLoader loader = new EffectiveBitmapLoader(getActivity());
            Bitmap mExample = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            loader.loadBitmap(Uri.parse(imageResIds[position]), imageView,mExample); 
            //imageView.setImageResource(imageResIds[position]); // Load image into ImageView
            return imageView;
		}
		
	}

}
