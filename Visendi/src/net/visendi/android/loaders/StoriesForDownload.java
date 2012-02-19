package net.visendi.android.loaders;

import java.util.Set;

import com.stanfy.app.service.DownloadsService;

import net.visendi.android.model.Story;
import net.visendi.android.model.Storys;
import android.content.Context;
import android.util.Log;

public class StoriesForDownload extends StoriesLoader {

	private static final String TAG = StoriesForDownload.class.getSimpleName();
	private Set<String> downLoaded;
	private Set<String> inProgres;

	public StoriesForDownload(Context context) {
		super(context);
	}

	@Override
	public Storys loadInBackground() {
		Storys data = super.loadInBackground();
		Storys res = new Storys();

		downLoaded = DownloadsService.getDownloadedStories(getContext());
		inProgres = DownloadsService.getInProgressStories(getContext());

		Log.d(TAG, "downLoaded:"+downLoaded.toString());
		Log.d(TAG, "inProgress:"+inProgres.toString());
		
		
		for (Story item : data) {
			String id = item.getId();
			Log.d(TAG, "storyId:"+id);
			if (downLoaded.contains(id)) {
				item.setStatus(Story.DOWNLOADED);
				res.add(item);
			} else if (inProgres.contains(item.getId())) {
				item.setStatus(Story.INPROGRESS);
				res.add(item);
			}
		}

		return res;
	}

}
