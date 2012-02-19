package net.visendi.android;

import java.text.MessageFormat;

import com.google.android.imageloader.ImageLoader.Callback;

import net.visendi.android.model.Story;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedFragment extends Fragment implements OnClickListener {

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
		headerView = (ViewGroup) inflater.inflate(R.layout.detailed_header,
				null, false);
		return headerView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Story item = storyInterface.getStory();
		title = (TextView) headerView.findViewById(R.id.title);
		coverImage = (ImageView) headerView.findViewById(R.id.coverImage);
		description = (TextView) headerView.findViewById(R.id.description);
		buyButton = (Button) headerView.findViewById(R.id.buyButton);

		title.setText(item.getTitle());
		VisendiApplication app = VisendiApplication.getInstance(getActivity());
		String url = MessageFormat.format(
				getString(R.string.cover_url_template), item.getId());
		app.getImageLoader().bind(coverImage, url, new Callback() {

			public void onImageLoaded(ImageView view, String url) {
				// TODO Auto-generated method stub

			}

			public void onImageError(ImageView view, String url, Throwable error) {
				// TODO Auto-generated method stub

			}
		});
		description.setText(item.getDesc());
		initButton();
		buyButton.setOnClickListener(this);
	}

	public void initButton() {
		Story story = storyInterface.getStory();
		if (story.getCost() == 0) {
			buyButton.setText(R.string.download);
		} else {
			buyButton.setText("");
			buyButton.setBackgroundResource(R.drawable.paypal);
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.buyButton) {
			Story story = storyInterface.getStory();
			if (story.getCost() == 0) {
				StoriesFragment.process(getActivity(), story);
				buyButton.setEnabled(false);
				buyButton.setText(R.string.button_in_progress);
			} else {
				buyButton.setEnabled(false);
				String url = "http://www.paypal.com";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}

		}
	}

}
