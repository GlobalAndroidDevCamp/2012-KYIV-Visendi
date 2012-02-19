package net.visendi.android.adapter;

import java.text.MessageFormat;
import java.util.List;

import net.visendi.android.PlayerListener;
import net.visendi.android.R;
import net.visendi.android.VisendiActivity;
import net.visendi.android.model.Story;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class StoryAdapter extends ArrayAdapter<Story> {

	private LayoutInflater infalter;

	public StoryAdapter(VisendiActivity context, List<Story> objects) {
		super(context, 0, objects);
		infalter = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.story_item, parent, false);
		}

		Story item = getItem(position);

		ImageButton image = (ImageButton) convertView
				.findViewById(R.id.playButton);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView author = (TextView) convertView.findViewById(R.id.auth);
		TextView distance = (TextView) convertView.findViewById(R.id.distance);
		TextView desc = (TextView) convertView.findViewById(R.id.desc);

		title.setText(item.getTitle());
		author.setText(item.getAuthor());
		distance.setText(item.getDist() + "");
		desc.setText(item.getDesc());

		image.setOnClickListener(new OnItemClick(position));
		return convertView;
	}

	private class OnItemClick implements OnClickListener {
		int position;

		public OnItemClick(int position) {
			super();
			this.position = position;
		}

		public void onClick(View v) {
			Story item = getItem(position);
			Context context = getContext();
			if (context instanceof PlayerListener) {
				PlayerListener playerListener = (PlayerListener) context;
				playerListener.setMusicTitle(item.getTitle());
				playerListener
						.setMusicStringUrl(MessageFormat
								.format("http://188.40.14.158:8080/visendi/resources/{0}/preview.ogg",
										item.getId()));
				playerListener.getPlayerFragment().startStreamingAudio();
		//		startStreamingAudio()
			}
			
		}
	}
}
