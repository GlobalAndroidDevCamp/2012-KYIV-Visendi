package net.visendi.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.ActionBar.Tab;
import android.support.v4.app.ActionBar.TabListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class VisendiActivity extends FragmentActivity implements TabListener {
	private static final String TAG = VisendiActivity.class.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_stories);
		tab.setTabListener(this);

		getSupportActionBar().addTab(tab, 0);

		tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_favourite);
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab, 1);

		getSupportActionBar().setSelectedNavigationItem(0);
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		try {

			Fragment fragment = StoriesFragment.class.newInstance();

			Log.d(TAG, "position:" + tab.getPosition());
			if (tab.getPosition() == 0) {
				fragment = StoriesFragment.class.newInstance();
			} else if (tab.getPosition() == 1) {
				fragment = DownloadedFragment.class.newInstance();
			}
			if (fragment != null) {
				getSupportFragmentManager().beginTransaction()
						.replace(android.R.id.content, fragment).commit();
			}
		} catch (InstantiationException e) {
			Log.d(TAG, "" + e);
		} catch (IllegalAccessException e) {
			Log.d(TAG, "" + e);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}