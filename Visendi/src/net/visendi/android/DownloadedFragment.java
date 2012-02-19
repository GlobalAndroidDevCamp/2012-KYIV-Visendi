package net.visendi.android;

import net.visendi.android.adapter.DownloadsAdapter;
import net.visendi.android.adapter.StoryAdapter;
import net.visendi.android.loaders.StoriesForDownload;
import net.visendi.android.loaders.StoriesLoader;
import net.visendi.android.model.Story;
import net.visendi.android.model.Storys;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DownloadedFragment extends ListFragment implements
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
		return new StoriesForDownload(getActivity());
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

			adapter = new DownloadsAdapter((VisendiActivity) getActivity(), data,
					onItemClickListener);
			setListAdapter(adapter);
			getListView().setOnItemClickListener(onItemClickListener);
			getListView().setOnItemLongClickListener(
					new OnItemLongClickListener() {

						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							StoriesFragment.process(getActivity(),
									adapter.getItem(arg2));
							return false;
						}
					});
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
		StoriesFragment.startItemDescriptionActivity(getActivity(), item);

	}

	public void onLoaderReset(Loader<Storys> loader) {
		// TODO Auto-generated method stub
		
	}

}
