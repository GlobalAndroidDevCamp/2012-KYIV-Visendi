package net.visendi.android;

import net.visendi.android.adapter.StoryAdapter;
import net.visendi.android.loaders.StoriesLoader;
import net.visendi.android.model.Story;
import net.visendi.android.model.Storys;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class StoriesFragment extends ListFragment implements
		LoaderCallbacks<Storys> {

	private StoryAdapter adapter;

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
			adapter = new StoryAdapter((VisendiActivity) getActivity(), data);
			setListAdapter(adapter);
			getListView().setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Story item = adapter.getItem(arg2);

					Intent intent = new Intent(getActivity(),
							DetailedActivity.class);
					intent.putExtra(Story.class.getSimpleName(), item);
					startActivity(intent);
				}
			});
		}
	}

	public void onLoaderReset(Loader<Storys> loader) {

	}

}
