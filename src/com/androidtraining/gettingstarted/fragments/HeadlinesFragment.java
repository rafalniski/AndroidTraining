package com.androidtraining.gettingstarted.fragments;

import com.androidtraining.R;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/** 
 * Fragment class for showing articles headlines.
 * @author rafalniski
 *
 */
public class HeadlinesFragment extends ListFragment {
	/** Instance of the interface **/
	OnHeadlineSelectedListener mCallBack;
	/** Activity that will contain this fragment, must implement this interface **/
	public interface OnHeadlineSelectedListener {
		public void OnArticleSelected(int position);
	}
	@Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
	/** This is just to make sure that activity implemented interface **/
	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack = (OnHeadlineSelectedListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement listener");
		}
	};
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallBack.OnArticleSelected(position);
		getListView().setItemChecked(position, true);
	}
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        // Create an array adapter for the list view, using the Ipsum headlines array
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Ipsum.Headlines));
    }
}
