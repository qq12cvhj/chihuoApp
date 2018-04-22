package com.example.qq12cvhj.chihuo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by qq12cvhj on 2018/4/22.
 */

public class homeImgLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object object, ImageView imageView) {
        Log.d("trImgs",((UserAction)(object)).titleImg);
        Picasso.get()
                .load(((UserAction)(object)).titleImg)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }
}
