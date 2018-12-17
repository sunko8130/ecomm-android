package com.creative_webstudio.iba.utils;


import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.creative_webstudio.iba.R;

public class LoadImage {

    public static GlideUrl getGlideUrl(String access) {
        String bToken = "Bearer " + access;
        final GlideUrl glideUrl = new GlideUrl(AppConstants.IMAGE_URL,
                new LazyHeaders.Builder()
                        .addHeader("Authorization", bToken)
                        .build());



        return glideUrl;
    }

    public static RequestOptions getOption(){
       return new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.coco_coffee)
                .error(R.drawable.coco_coffee);
    }

}
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
////                Looper.prepare();
//                try {
//                    bitmap = Glide
//                            .with(SplashActivity.this)
//                            .asBitmap()
//                            .apply(options)
//                            .load(glideUrl)
//                            .submit()
//                            .get();
//                } catch (final ExecutionException e) {
//                    Log.e("ImageError", e.getMessage());
//                } catch (final InterruptedException e) {
//                    Log.e("ImageError", e.getMessage());
//                }
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void dummy) {
//                if (null != bitmap) {
//                    // The full bitmap should be available here
//                    ivSplash.setImageBitmap(bitmap);
//                    Log.d("ImageError", "Image loaded");
//                };
//            }
//        }.execute();
//    }

