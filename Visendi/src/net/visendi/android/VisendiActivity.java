package net.visendi.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.ActionBar.Tab;
import android.support.v4.app.ActionBar.TabListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class VisendiActivity extends FragmentActivity implements TabListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_stories);
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText(R.string.tab_favourite);
		tab.setTabListener(this);
		getSupportActionBar().addTab(tab);

	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		try {
			if (tab.getPosition() == 0) {
				ft.show(StoriesFragment.class.newInstance());
			} else if (tab.getPosition() == 1) {
				ft.show(FavouritesFragment.class.newInstance());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}