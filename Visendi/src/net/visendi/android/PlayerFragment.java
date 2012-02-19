package net.visendi.android;

import java.io.IOException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pocketjourney.media.StreamingMediaPlayer;

public class PlayerFragment extends Fragment {

	private ImageButton playButton;

	private TextView textStreamed;

	private boolean isPlaying;

	private StreamingMediaPlayer audioStreamer;

	private PlayerListener playerListener;

	@Override
	public void onAttach(SupportActivity activity) {
		super.onAttach(activity);
		if (activity instanceof PlayerListener) {
			playerListener = (PlayerListener) activity;
			playerListener.setStreamPlayer(audioStreamer);
			playerListener.setPlayerFragment(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tutorial3, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initControls();
	}

	private void initControls() {
		textStreamed = (TextView) getView().findViewById(R.id.text_kb_streamed);

		playButton = (ImageButton) getView().findViewById(R.id.button_play);
		playButton.setEnabled(false);
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (audioStreamer.getMediaPlayer().isPlaying()) {
					audioStreamer.getMediaPlayer().pause();
					playButton
							.setImageResource(R.drawable.media_playback_start);
				} else {
					audioStreamer.getMediaPlayer().start();
					audioStreamer.startPlayProgressUpdater();
					playButton.setImageResource(R.drawable.button_pause);
				}
				isPlaying = !isPlaying;

			}
		});
		startStreamingAudio();
	}

	public void startStreamingAudio() {
		try {
			if (audioStreamer != null) {
				audioStreamer.interrupt();
			}
			String title = playerListener.getMusicTitle();
			if (title == null) {
				title = "";
			}
			audioStreamer = new StreamingMediaPlayer(getActivity(),
					textStreamed, playButton, title);
			String mediaUrl = playerListener.getMusicStringUrl();// "http://www.pocketjourney.com/downloads/pj/tutorials/audio.mp3";
			if (mediaUrl != null) {
				getView().setVisibility(View.VISIBLE);
				audioStreamer.startStreaming(mediaUrl);
			} else {
				getView().setVisibility(View.GONE);
			}
		} catch (IOException e) {
			Log.e(getClass().getName(), "Error starting to stream audio.", e);
		}

	}

}
