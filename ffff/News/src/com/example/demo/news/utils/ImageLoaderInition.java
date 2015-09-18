package com.example.demo.news.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.xinhuamm.d0403.R;

/**
 * Created by 123456 on 2015/9/11.
 */
//imageloader的加载选项写入类 公用
public class ImageLoaderInition {
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.news_list_bg)
            .showImageForEmptyUri(R.drawable.news_list_bg)
            .showImageOnFail(R.drawable.news_list_bg).cacheInMemory(true)
            .cacheOnDisk(true).build();

}
