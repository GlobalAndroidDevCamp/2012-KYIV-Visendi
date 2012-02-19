package com.stanfy.images;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import android.util.Log;

/**
 * A pool of temporary storages required for images decoding or other IO operations.
 * @author Roman Mazur - Stanfy (http://www.stanfy.com)
 */
public class BuffersPool {

  /** Default buffer size. */
  public static final int DEFAULT_SIZE_FOR_IMAGES = 16 * 1024;

  /** Used buffers count. */
  private int usedBuffersCount = 0, buffersCount = 0;

  /** Logging tag. */
  private static final String TAG = "BuffersPool";

  /** Debug flag. */
  private static final boolean DEBUG = true;

  /** Buffers store. */
  private TreeMap<Integer, List<Object>> buffers = new TreeMap<Integer, List<Object>>();

  /** A lock. */
  private Object lock = new Object();

  private static byte[] allocate(final int size) { return new byte[size]; }

  public BuffersPool(final int[][] initDescription) {
    if (DEBUG) { Log.i(TAG, "Creating buffers. Types count: " + initDescription.length); }
    for (int i = initDescription.length - 1; i >= 0; i--) {
      final int count = initDescription[i][0];
      final int amount = initDescription[i][1];
      for (int k = count - 1; k >= 0; k--) {
        ++buffersCount;
        release(allocate(amount));
      }
    }
    usedBuffersCount = 0;
  }

  /**
   * @param minCapacity minimal capacity of the buffer
   * @return buffer with length greater on equal than <code>minCapacity</code>
   */
  public byte[] get(final int minCapacity) {
    if (buffers == null) { return null; }
    ++usedBuffersCount;
    final SortedMap<Integer, List<Object>> map = buffers.tailMap(minCapacity);
    if (map == null || map.isEmpty()) {
      ++buffersCount;
      return allocate(minCapacity);
    }

    final List<Object> bList = map.get(map.firstKey());
    if (bList == null || bList.isEmpty()) {
      ++buffersCount;
      return allocate(minCapacity);
    }

    synchronized (lock) {
      final byte[] array = (byte[])bList.remove(0);
      return array;
    }
  }

  /**
   * Recycle the buffer.
   * @param buffer unused buffer
   */
  public void release(final byte[] buffer) {
    if (buffer == null || buffers == null) { return; }
    final int capacity = buffer.length;
    if (capacity == 0) { return; }

    List<Object> bList = buffers.get(capacity);
    if (bList == null) {
      bList = new LinkedList<Object>();
      synchronized (lock) {
        buffers.put(capacity, bList);
      }
    }

    synchronized (lock) {
      bList.add(buffer);
    }
    --usedBuffersCount;
    if (DEBUG) { Log.d(TAG, "Buffers in use: " + usedBuffersCount + "/" + buffersCount); }
  }

  /**
   * Flush resources.
   */
  public void flush() {
    synchronized (lock) {
      buffers.clear();
      if (DEBUG) { Log.d(TAG, "Buffers flushed"); }
    }
  }

}
