package net.visendi.android;

import net.visendi.android.adapter.StoryAdapter;
import net.visendi.android.model.Story;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DetailedActivity extends FragmentActivity {
	private Story item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_view);
		onNewIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		item = (Story)intent.getSerializableExtra(Story.class.getSimpleName());
	}
}
