package net.visendi.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;

public class VisendiActivity extends FragmentActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		ActionBar supportActionBar = getSupportActionBar();
		supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }
}