package net.visendi.android.loaders;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import net.visendi.android.R;
import net.visendi.android.model.Story;
import net.visendi.android.model.Storys;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;

public class StoriesLoader extends AsyncTaskLoader<Storys> {

	private String TAG = getClass().getSimpleName();

	public StoriesLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
		Log.d(TAG, "onStartLoading");
	}

	@Override
	public Storys loadInBackground() {
		Storys res = null;
		try {
			URL url = new URL(getContext().getString(R.string.url_stories));
			URLConnection urlConnection = url.openConnection();
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(in);
			res = gson.fromJson(reader, Storys.class);
		} catch (Exception e) {
			Log.d(TAG, "" + e);
		}

		return res;
	}
}
