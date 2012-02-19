package net.visendi.android;

import com.stanfy.app.service.DownloadsService;

import net.visendi.android.DetailedActivity.DownloadStatusReceiver;
import net.visendi.android.adapter.StoryAdapter;
import net.visendi.android.model.Story;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class DetailedActivity extends FragmentActivity implements
		StoryInterface {
	private static final String TAG = DetailedActivity.class.getSimpleName();
	private Story item;
	private DownloadStatusReceiver receiver;

	public class DownloadStatusReceiver extends BroadcastReceiver {

		private final String TAG = getClass().getSimpleName();

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Recieved");
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		receiver = new DownloadStatusReceiver();
		setContentView(R.layout.detailed_view);
		registerReciever();
		onNewIntent(getIntent());
	}

	private void unregisterReceiver() {
		unregisterReceiver(receiver);
	}

	private void registerReciever() {
		IntentFilter filter = new IntentFilter(
				DownloadsService.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(receiver, filter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReciever();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		item = (Story) intent.getSerializableExtra(Story.class.getSimpleName());

		Fragment fragment = null;
		try {
			fragment = DetailedFragment.class.newInstance();
		} catch (InstantiationException e) {
			Log.d(TAG, "", e);
		} catch (IllegalAccessException e) {
			Log.d(TAG, "", e);
		}

		if (fragment != null) {
			getSupportFragmentManager().beginTransaction()
					.replace(android.R.id.content, fragment).commit();
		}

	}

	public Story getStory() {
		return item;
	}
}
