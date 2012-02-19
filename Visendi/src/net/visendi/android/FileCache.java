/*
 * Project   de.bild.android.supermanager 
 * Author    khal
 * Created   Jul 21, 2011
 */

package net.visendi.android;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.ResponseCache;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.google.android.filecache.FileResponseCache;


public class FileCache extends FileResponseCache {

	private static final String TAG = FileCache.class.getSimpleName();


	public static void install(Context context) {
		ResponseCache responseCache = ResponseCache.getDefault();
		if ( responseCache instanceof FileCache ) {
		} else if ( responseCache == null ) {
			FileCache dropCache = new FileCache(context);
			ResponseCache.setDefault(dropCache);
		} else {
			Class<? extends ResponseCache> type = responseCache.getClass();
		}
	}


	private static File getCacheDir(Context context) {
		File dir = context.getCacheDir();
		dir = new File(dir, "filecache");
		return dir;
	}

	private final Context mContext;


	public FileCache(Context context) {
		mContext = context;
	}


	
	@Override
	protected boolean isStale(File file, URI uri, String requestMethod,
			Map<String, List<String>> requestHeaders, Object cookie) {
		if ( cookie instanceof Long ) {
			Long maxAge = (Long)cookie;
			long age = System.currentTimeMillis() - file.lastModified();
			if ( age > maxAge.longValue() ) {
				return true;
			}
		}
		return super.isStale(file, uri, requestMethod, requestHeaders, cookie);
	}
	


	@Override
	protected File getFile(URI uri, String requestMethod, Map<String, List<String>> requestHeaders,
			Object cookie) {
		try {
			File parent = getCacheDir(mContext);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(String.valueOf(uri).getBytes("UTF-8"));
			byte[] output = digest.digest();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < output.length; i++) {
				builder.append(Integer.toHexString(0xFF & output[i]));
			}
			String filename = builder.toString();
			return new File(parent, filename);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
