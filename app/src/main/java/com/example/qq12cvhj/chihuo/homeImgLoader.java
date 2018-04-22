package com.example.qq12cvhj.chihuo;

import android.content.Context;
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
        Log.d("responseData1",((UserAction)(object)).titleImg);
        Picasso.get()
                .load(((UserAction)(object)).titleImg)
                .into(imageView);
    }
}
