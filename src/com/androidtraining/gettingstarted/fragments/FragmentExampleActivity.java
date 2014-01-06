package com.androidtraining.gettingstarted.fragments;

import com.androidtraining.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

public class FragmentExampleActivity extends FragmentActivity implements HeadlinesFragment.OnHeadlineSelectedListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_articles);
		
		if(findViewById(R.id.fragment_container) != null) {
			if(savedInstanceState != null) {
				return;
			}
			/** Adding headlines fragment to layout **/
			HeadlinesFragment firstFragment = new HeadlinesFragment();
			firstFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(
					R.id.fragment_container, firstFragment).commit();
		}
	}
	@Override
	public void OnArticleSelected(int position) {
		 ArticleFragment articleFrag = (ArticleFragment)
	                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

	        if (articleFrag != null) {
	            // If article frag is available, we're in two-pane layout...

	            // Call a method in the ArticleFragment to update its content
	            articleFrag.updateArticleView(position);
	        } else {
	            // Otherwise, we're in the one-pane layout and must swap frags...

	            // Create fragment and give it an argument for the selected article
	            ArticleFragment newFragment = new ArticleFragment();
	            Bundle args = new Bundle();
	            args.putInt(ArticleFragment.ARG_POSITION, position);
	            newFragment.setArguments(args);
	        
	            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, newFragment);
	            transaction.addToBackStack(null);

	            // Commit the transaction
	            transaction.commit();
		
	}
}
}
