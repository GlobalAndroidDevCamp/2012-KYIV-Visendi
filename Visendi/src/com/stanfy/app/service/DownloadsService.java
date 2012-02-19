package com.stanfy.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.visendi.android.model.Storys;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.stanfy.images.BuffersPool;
import com.stanfy.images.PoolableBufferedInputStream;

/**
 * Service that can be used instead of {@link android.app.DownloadManager} on
 * older devices.
 * 
 * @author Roman Mazur (Stanfy - http://www.stanfy.com)
 */
public class DownloadsService extends Service {

	/** Logging tag. */
	protected static final String TAG = "DownloadsService";
	/** Debug flag. */
	private static final boolean DEBUG = true;

	/** Enqueue action. */
	public static final String ACTION_ENQUEUE = "com.stanfy.download.action.ENQUEUE";
	/** Broadcast complete action. */
	public static final String ACTION_DOWNLOAD_COMPLETE = "com.stanfy.download.action.COMPLETE";
	/** Broadcast click on download action. */
	public static final String ACTION_DOWNLOAD_CLICK = "com.stanfy.download.action.CLICK";

	/** Extra name. */
	public static final String EXTRA_REQUEST = "request",
			EXTRA_ID = "download_id", EXTRA_SUCCESS = "download_success";

	/** Preference name. */
	private static final String PREF_NAME = "downloads_service_counter";
	/** Key to store the counter. */
	private static final String KEY_ID = "id";

	/** Base ID for notifications. */
	private static final int NOTIFICATION_BASE_ID = Integer.MAX_VALUE / 2;

	/** Default buffer size. */
	private static final int DEFAULT_BUFFER_SIZE = 8192;

	/** Buffers pool. */
	private final BuffersPool buffersPool = new BuffersPool(new int[][] { { 4,
			DEFAULT_BUFFER_SIZE } });

	/** tasks. */
	private final LinkedList<DownloadTask> tasks = new LinkedList<DownloadsService.DownloadTask>();

	/** Notification manager. */
	private NotificationManager notificationManager;
	
	private Set<String> storiesInProgress = new HashSet<String>();
	private Set<String> storiesDownloaded = new HashSet<String>();
	private static final String STORIES_IN_PROGRESS = "storiesInProgress";
	private static final String STORIES_DOWNLOADED = "storiesDownloaded";
	
	

	/** @return next unique download ID */
	public static final synchronized long nextId(final Context context) {
		final SharedPreferences counterStore = context.getSharedPreferences(
				PREF_NAME, 0);
		final long value = counterStore.getLong(KEY_ID, 0) + 1;
		counterStore.edit().putLong(KEY_ID, value).commit();
		return value;
	}

	/** @return new download task */
	protected DownloadTask createDownloadTask() {
		return new DownloadTask();
	}

	/** @return the buffersPool */
	protected BuffersPool getBuffersPool() {
		return buffersPool;
	}

	/** @return the notificationManager */
	protected NotificationManager getNotificationManager() {
		return notificationManager;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags,
			final int startId) {
		if (intent == null) {
			return START_STICKY;
		}
		final String action = intent.getAction();
		if (action == null) {
			return START_STICKY;
		}
		if (DEBUG) {
			Log.v(TAG, "Start " + action);
		}

		if (ACTION_ENQUEUE.equals(action)) {
			final Request request = (Request) intent
					.getSerializableExtra(EXTRA_REQUEST);
			enqueue(request);
		}

		return START_STICKY;
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		if (!tasks.isEmpty()) {
			if (DEBUG) {
				Log.i(TAG, "Canceling current tasks");
			}
			for (final DownloadTask task : tasks) {
				task.cancel(true);
			}
		}
		super.onDestroy();
	}

	protected void enqueue(final Request request) {
		final DownloadTask task = createDownloadTask();
		tasks.add(task);
		request.notificationId = NOTIFICATION_BASE_ID + tasks.size();
		task.execute(request);
	}

	protected void onTaskFinish(final DownloadTask task, final Request request) {
		getNotificationManager().cancel(request.notificationId);
		tasks.remove(task);
		sendBroadcast(new Intent(ACTION_DOWNLOAD_COMPLETE).putExtra(EXTRA_ID,
				request.id).putExtra(EXTRA_SUCCESS, request.success));
		if (tasks.isEmpty()) {
			stopSelf();
		}
		removeStoryFromProgress(request.id);
	}
	
	public Set<String> getDownloadedStories() {
		return getSharedPreferences(STORIES_DOWNLOADED, MODE_PRIVATE).getStringSet(STORIES_DOWNLOADED, null);
	}
	
	public void putStoryInProgress(long id) {
		storiesInProgress.add(Long.toString(id));
		SharedPreferences sharedPreferences = getSharedPreferences(STORIES_IN_PROGRESS, MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putStringSet(STORIES_IN_PROGRESS, storiesInProgress);
		edit.commit();
	}
	
	public void removeStoryFromProgress(long id) {
		String stringId = Long.toString(id);
		if(storiesInProgress.contains(stringId)) {
			storiesInProgress.remove(stringId);
			SharedPreferences sharedPreferences = getSharedPreferences(STORIES_IN_PROGRESS, MODE_PRIVATE);
			Editor edit = sharedPreferences.edit();
			edit.putStringSet(STORIES_IN_PROGRESS, storiesInProgress);
			edit.commit();
			
			storiesDownloaded.add(stringId);
			SharedPreferences sharedPreferencesDown = getSharedPreferences(STORIES_DOWNLOADED, MODE_PRIVATE);
			sharedPreferencesDown.edit().putStringSet(STORIES_DOWNLOADED, storiesDownloaded).commit();
		}
	}

	/**
	 * Download request.
	 * 
	 * @author Roman Mazur (Stanfy - http://www.stanfy.com)
	 */
	public static class Request implements Serializable {

		/** Identifier. */
		private long id;
		/** Title. */
		private String title;
		/** Description. */
		private String description;
		/** URI. */
		private String uri;
		/** Destination URI. */
		private String destinationUri;
		/** Success flag. */
		private boolean success;

		/** Notification ID. */
		private int notificationId;

		public Request() {
		}

		/** @return the id */
		public long getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(final long id) {
			this.id = id;
		}

		/** @return the title */
		public String getTitle() {
			return title;
		}

		/**
		 * @param title
		 *            the title to set
		 */
		public void setTitle(final String title) {
			this.title = title;
		}

		/** @return the description */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description
		 *            the description to set
		 */
		public void setDescription(final String description) {
			this.description = description;
		}

		/**
		 * @param destinationUri
		 *            the destinationUri to set
		 */

		/** @return the success */
		public boolean isSuccess() {
			return success;
		}

		/**
		 * @param success
		 *            the success to set
		 */
		public void setSuccess(final boolean success) {
			this.success = success;
		}

		public String getDestinationUri() {
			return destinationUri;
		}

		public void setDestinationUri(String destinationUri) {
			this.destinationUri = destinationUri;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

	}

	/**
	 * Download task.
	 * 
	 * @author Roman Mazur (Stanfy - http://www.stanfy.com)
	 */
	public class DownloadTask extends AsyncTask<Request, Float, Request> {
		/** Notification ID. */
		private Request request;
		/** Click intent. */
		private Intent clickIntent = new Intent(ACTION_DOWNLOAD_CLICK);
		/** Notification time. */
		private long notificationTime = System.currentTimeMillis();

		protected void updateDownloadProgress(final Float progress) {
			// request;
		}

		@Override
		protected Request doInBackground(final Request... params) {
			final Request request = params[0];
			request.success = false;

			this.request = request;
			clickIntent.putExtra(EXTRA_ID, request.id);
			updateDownloadProgress(null);
			
			putStoryInProgress(request.id);
			
			File destination;
			URLConnection urlConnection;
			try {

				Log.e(TAG, "before");

				destination = new File(new URI(request.getDestinationUri()
						.toString()));

				Log.e(TAG, "Good Uri destination URI");

				URL url = new URL(request.getUri().toString());
				urlConnection = url.openConnection();
				Log.e(TAG, "Good uri get");

			} catch (final URISyntaxException e) {
				Log.e(TAG, "Bad URI", e);
				return request;
			} catch (final MalformedURLException e) {
				Log.e(TAG, "Bad URI", e);
				return request;
			} catch (IOException e) {
				Log.e(TAG, "Bad URI", e);
				return request;
			}

			InputStream input = null;
			OutputStream output = null;
			try {
				final long length = urlConnection.getContentLength();
				if (length > 0) {
					updateDownloadProgress(0f);
				}

				destination.createNewFile();
				output = new FileOutputStream(destination);
				input = new PoolableBufferedInputStream(
						urlConnection.getInputStream(), DEFAULT_BUFFER_SIZE,
						buffersPool);
				int count;
				final byte[] buffer = buffersPool.get(DEFAULT_BUFFER_SIZE);
				int total = 0;
				float prevProgress = 0;
				final float minDelta = 0.05f;
				do {
					count = input.read(buffer);
					if (count > 0) {
						output.write(buffer, 0, count);
						if (length > 0) {
							total += count;
							final float progress = (float) total / length;
							if (progress - prevProgress >= minDelta) {
								updateDownloadProgress(progress);
								prevProgress = progress;
							}
						}
					}
				} while (count != -1);
				request.success = true;
				return request;
			} catch (final Exception e) {
				Log.e(TAG, "Cannot download " + request.getUri(), e);
				return request;
			} finally {
				try {
					if (input != null) {
						input.close();
					}
					if (output != null) {
						output.close();
					}
				} catch (final IOException e) {
					Log.e(TAG, "Cannot close the "
							+ (input == null ? "output" : "input")
							+ " stream for " + request.getUri(), e);
				}
			}
		}

		@Override
		protected void onCancelled(final Request result) {
			onTaskFinish(this, result);
		}

		@Override
		protected void onPostExecute(final Request result) {
			onTaskFinish(this, result);
		}
	}

}
