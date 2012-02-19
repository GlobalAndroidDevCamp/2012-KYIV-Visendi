package net.visendi.android;

import net.visendi.android.adapter.StoryAdapter;
import net.visendi.android.loaders.StoriesLoader;
import net.visendi.android.model.Storys;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class StoriesFragment extends ListFragment implements
		LoaderCallbacks<Storys> {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getActivity().getString(R.string.empty_list));

		// We have a menu item to show in action bar.
		setHasOptionsMenu(false);

		setListShown(false);

		getLoaderManager().initLoader(0, null, this);
	}

	public Loader<Storys> onCreateLoader(int id, Bundle args) {
		return new StoriesLoader(getActivity());
	}

	public void onLoadFinished(Loader<Storys> loader, Storys data) {
		setListShown(true);
		if (data != null) {
			setListAdapter(new StoryAdapter((VisendiActivity)getActivity(), data));
		}
	}

	public void onLoaderReset(Loader<Storys> loader) {

	}

}
