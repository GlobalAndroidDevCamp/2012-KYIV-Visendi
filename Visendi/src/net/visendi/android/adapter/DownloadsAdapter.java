package net.visendi.android.adapter;

import java.util.List;

import net.visendi.android.R;
import net.visendi.android.VisendiActivity;
import net.visendi.android.model.Story;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class DownloadsAdapter extends StoryAdapter {

	public DownloadsAdapter(VisendiActivity context, List<Story> objects,
			OnItemClickListener onItemClickListener) {
		super(context, objects, onItemClickListener);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View res = super.getView(position, convertView, parent);
		TextView tx = (TextView)res.findViewById(R.id.desc);
		Story item = getItem(position);
		String testRes = "";
		switch (item.getStatus()) {
		case Story.DOWNLOADED:
			testRes = getContext().getString(R.string.button_downloaded);
			break;
		case Story.INPROGRESS:
			testRes = getContext().getString(R.string.button_in_progress);
			break;
		default:
			break;
		}
		tx.setText(testRes);
		return res;
	}

}
