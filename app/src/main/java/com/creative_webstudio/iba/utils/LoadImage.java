package com.creative_webstudio.iba.utils;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.creative_webstudio.iba.R;

public class LoadImage {

    public static GlideUrl getGlideUrl(String access, Long imageId) {
        String accessToken = "Bearer " + access;

        return new GlideUrl(AppConstants.IMAGE_URL + imageId,
                new LazyHeaders.Builder()
//                        .addHeader("Authorization", accessToken)
                        .build());
    }


    public static RequestOptions getOption() {

        return new RequestOptions()
                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .placeholder(R.drawable.blank_photo)
                .error(R.drawable.blank_photo);
    }


}


