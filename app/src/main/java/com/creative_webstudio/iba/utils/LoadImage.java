package com.creative_webstudio.iba.utils;


import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.creative_webstudio.iba.R;

public class LoadImage {

    public static GlideUrl getGlideUrl(String access, Long imageId) {
        String bToken = "Bearer " + access;
        final GlideUrl glideUrl = new GlideUrl(AppConstants.IMAGE_URL + imageId,
                new LazyHeaders.Builder()
                        .addHeader("Authorization", bToken)
                        .build());


        return glideUrl;
    }

    public static RequestOptions getOption() {
        return new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.coco_coffee)
                .error(R.drawable.coco_coffee);
    }


}


