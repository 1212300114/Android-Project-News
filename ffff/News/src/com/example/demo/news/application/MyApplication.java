package com.example.demo.news.application;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private boolean get = false;

    public boolean isGet() {
        return get;
    }

    public void setGet(boolean get) {
        this.get = get;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        initImageLoader(getApplicationContext());

    }

    public static void initImageLoader(Context context) {
        // �����ļ���Ŀ¼
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "universalimageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheExtraOptions(480, 800)
                        // max width, max height���������ÿ�������ļ�����󳤿�
                .threadPoolSize(3)
                        // �̳߳����̵߳�����
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        // �������ʱ���URI������MD5 ����
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024) // �ڴ滺������ֵ
                .diskCacheSize(50 * 1024 * 1024) // SD����������ֵ
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // ��ԭ�ȵ�discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))// �Զ��建��·��
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                        // (5
                        // s),
                        // readTimeout
                        // (30
                        // s)��ʱʱ��
                .writeDebugLogs() // Remove for release app
                .build();
        // ȫ�ֳ�ʼ��������
        ImageLoader.getInstance().init(config);
    }
}
