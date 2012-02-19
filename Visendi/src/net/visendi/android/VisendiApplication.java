package net.visendi.android;

import java.net.ContentHandler;
import java.net.URLStreamHandlerFactory;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.google.android.imageloader.BitmapContentHandler;
import com.google.android.imageloader.ImageLoader;

public class VisendiApplication extends Application {

	private static final int IMAGE_TASK_LIMIT = 1;
	private String analyticKey;
	private ImageLoader mImageLoader;
	// 50% of available memory, up to a maximum of 32MB
	private static final long IMAGE_CACHE_SIZE = Math.min(Runtime.getRuntime()
			.maxMemory() / 2, 32 * 1024 * 1024);

	public static VisendiApplication getInstance(Context context) {
		return (VisendiApplication) context.getApplicationContext();
	}

	public ImageLoader getImageLoader() {
		if (mImageLoader == null) {
			this.mImageLoader = VisendiApplication.createImageLoader(this);
		}
		return mImageLoader;
	}

	@Override
	public Object getSystemService(String name) {
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			return getImageLoader();
		}

		return super.getSystemService(name);
	}

	public static ImageLoader createImageLoader(Context context) {
		// Install the file cache (if it is not already installed)
		FileCache.install(context);

		// Just use the default URLStreamHandlerFactory because
		// it supports all of the required URI schemes (http).
		URLStreamHandlerFactory streamFactory = null;

		// Load images using a BitmapContentHandler
		// and cache the image data in the file cache.
		BitmapContentHandler bitmapContentHandler = new BitmapContentHandler();
		ContentHandler bitmapHandler = FileCache.capture(bitmapContentHandler,
				null);

		// For pre-fetching, use a "sink" content handler so that the
		// the binary image data is captured by the cache without actually
		// parsing and loading the image data into memory. After pre-fetching,
		// the image data can be loaded quickly on-demand from the local cache.
		ContentHandler prefetchHandler = FileCache.capture(FileCache.sink(),
				null);

		// Perform callbacks on the main thread
		Handler handler = null;

		// new ImageLoader();
		// return new ImageLoader(IMAGE_TASK_LIMIT, streamFactory,
		// bitmapHandler, prefetchHandler, IMAGE_CACHE_SIZE, handler);

		return new ImageLoader(IMAGE_CACHE_SIZE);
	}
}
