package org.gaochun.myapplication;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class AppAplication extends Application {

    private static Context mContext;
    public static DisplayImageOptions mOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageLoader(this);
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * ��ʼ��ImageLoader
     */
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(200 * 1024 * 1024) // 200 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);


        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_loading_large)   //���ع�����
                .showImageForEmptyUri(R.mipmap.ic_loading_large) //uriΪ��ʱ
                .showImageOnFail(R.mipmap.ic_loading_large)      //����ʧ��ʱ
                .cacheOnDisk(true)
                .cacheInMemory(true)                             //����cache���ڴ�ʹ�����
                .bitmapConfig(Bitmap.Config.RGB_565)             //ͼƬѹ����������
                .build();
    }
}