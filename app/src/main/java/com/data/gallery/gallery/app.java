package com.data.gallery.gallery;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2015/8/12.
 */
public class app extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        initImageLoader();
    }
    private void initImageLoader() {
        String dirName = "";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dirName = Environment.getExternalStorageDirectory() + "/Gallery/";
        } else {
            dirName = Environment.getExternalStorageDirectory() + "/Gallery/";
//            dirName=activity.getCacheDir()+"/myShare/";
//            dirName = app.getContext().getFilesDir().getPath() + "/myShare/";
        }

        File cacheDir = new File(dirName);
        if(!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs()
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .build();
        ImageLoader.getInstance().init(config);
    }
    public static Context getContext() {
        return mContext;
    }
}

