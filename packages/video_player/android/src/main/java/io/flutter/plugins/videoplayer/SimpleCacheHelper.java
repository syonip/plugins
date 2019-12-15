package io.flutter.plugins.videoplayer;

import android.content.Context;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import java.io.File;

public class SimpleCacheHelper {

  private static volatile SimpleCache INSTANCE;
  private static ExoDatabaseProvider DB;
  private static File CacheDirectory;
  private static Context Context;

  public static SimpleCache getInstance(File cacheDirectory, Context context) {
    if (INSTANCE == null) { // Check 1
      synchronized (SimpleCacheHelper.class) {
        if (INSTANCE == null) { // Check 2
          LeastRecentlyUsedCacheEvictor evictor =
              new LeastRecentlyUsedCacheEvictor(CacheDataSourceFactory.MAX_CACHE_SIZE);
          DB = new ExoDatabaseProvider(context);
          INSTANCE = new SimpleCache(cacheDirectory, evictor, DB);
          SimpleCacheHelper.CacheDirectory = cacheDirectory;
          SimpleCacheHelper.Context = context;
        }
      }
    }
    return INSTANCE;
  }

  public static void clearCache() {
    SimpleCache.delete(SimpleCacheHelper.CacheDirectory, DB);
    INSTANCE.release();
    INSTANCE = null;
    getInstance(SimpleCacheHelper.CacheDirectory, SimpleCacheHelper.Context);
  }
}
