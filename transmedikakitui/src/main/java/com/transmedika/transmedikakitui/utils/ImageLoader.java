package com.transmedika.transmedikakitui.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


public class ImageLoader {
    public static void load(Context context, String url, ImageView iv, int imgIfEmpty) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(imgIfEmpty)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(iv);
    }

    public static void loadResize(Context context, String url, ImageView iv, int imgIfEmpty) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .override(300,300)
                        .placeholder(imgIfEmpty)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(iv);
    }


    public static void loadIcon(Context context, String url, ImageView iv, int imgIfEmpty) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .override(200,200)
                        .placeholder(imgIfEmpty)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(iv);
    }

    public static void loadIcon(Context context, String url, ImageView iv, int imgIfEmpty, int size) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .override(size,size)
                        .placeholder(imgIfEmpty)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(iv);
    }

    public static void load(Activity activity, String url, ImageView iv) {
        if(!activity.isDestroyed()) {
            Glide.with(activity)
                    .load(url)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(iv);
        }
    }

    public static void load(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(iv);
    }

    public static void loadAll(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(iv);
    }

    public static void loadAllSize(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .override(720,480)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(iv);
    }

    public static void loadAll(Activity activity, String url, ImageView iv) {
        if(!activity.isDestroyed()) {
            Glide.with(activity)
                    .load(url)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(iv);
        }
    }

    public static void loadAll(Activity activity, String url, ImageView iv, int placeHolder) {
        if(!activity.isDestroyed()) {
            Glide.with(activity)
                    .load(url)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(placeHolder))
                    .into(iv);
        }
    }

}
