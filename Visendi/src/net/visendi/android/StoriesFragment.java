package net.visendi.android;

import net.visendi.android.adapter.StoryAdapter;
import net.visendi.android.loaders.StoriesLoader;
import net.visendi.android.model.Story;
import net.visendi.android.model.Storys;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class StoriesFragment extends ListFragment implements
		LoaderCallbacks<Storys> {

	private StoryAdapter adapter;
	private String TAG = StoriesFragment.class.getSimpleName();

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
			OnItemClickListener onItemClickListener = new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Log.d(TAG,
							"onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)");
					onListItemClick(getListView(), arg1, arg2, arg3);
				}
			};

			adapter = new StoryAdapter((VisendiActivity) getActivity(), data,
					onItemClickListener);
			setListAdapter(adapter);
			getListView().setOnItemClickListener(onItemClickListener);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(TAG, "itemClicked");
		Story item = adapter.getItem(position);
		startItemDescriptionActivity(getActivity(), item);

	}

	public static void startItemDescriptionActivity(Context context, Story item) {
		Intent intent = new Intent(context, DetailedActivity.class);
		intent.putExtra(Story.class.getSimpleName(), item);
		context.startActivity(intent);
	}

	public void onLoaderReset(Loader<Storys> loader) {

	}

}
