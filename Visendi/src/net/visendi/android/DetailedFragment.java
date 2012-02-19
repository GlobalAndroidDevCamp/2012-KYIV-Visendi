package net.visendi.android;

import java.util.logging.LogRecord;

import net.visendi.android.model.Story;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedFragment extends ListFragment {

	
	private StoryInterface storyInterface;
	private ViewGroup headerView;
	private TextView title;
	private ImageView coverImage;
	private TextView description;
	private Button buyButton;
	
	@Override
	public void onAttach(SupportActivity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		if (activity instanceof StoryInterface) {
			storyInterface = (StoryInterface) activity;
		}

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headerView = (ViewGroup)LayoutInflater.from(getActivity()).inflate(R.layout.detailed_header, null, false);
		
		Story item = storyInterface.getStory();
		title = (TextView)headerView.findViewById(R.id.title);
		coverImage = (ImageView)headerView.findViewById(R.id.coverImage);
		description = (TextView)headerView.findViewById(R.id.description);
		buyButton = (Button)headerView.findViewById(R.id.buyButton);
		
		
		title.setText(item.getTitle());
		//coverImage
		description.setText(item.getDesc());
		buyButton.setText(R.string.download);
		setListShown(true);
		setEmptyText(getActivity().getString(R.string.stories_empty));
		getListView().addHeaderView(headerView);
	}
	
	

}
