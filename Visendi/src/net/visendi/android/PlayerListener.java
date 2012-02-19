package net.visendi.android;

import com.pocketjourney.media.StreamingMediaPlayer;

public interface PlayerListener {
	void setMusicStringUrl(String url);

	String getMusicStringUrl();

	StreamingMediaPlayer getStreamPlayer();

	void setStreamPlayer(StreamingMediaPlayer player);

	void setPlayerFragment(PlayerFragment playerFragment);

	PlayerFragment getPlayerFragment();

	String getMusicTitle();

	void setMusicTitle(String url);

}
