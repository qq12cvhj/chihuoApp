package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by qq12cvhj on 2018/3/23.
 */

public class FoodInfoAdapter extends ArrayAdapter<FoodInfo> {
    private int resourceId;
    public FoodInfoAdapter(@NonNull Context context,  int textViewResourceId, @NonNull List<FoodInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FoodInfo  foodInfo = getItem(position);

        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView imgView = view.findViewById(R.id.search_food_img);
        Picasso.get()
                .load(foodInfo.foodImgSrc)
                .config(Bitmap.Config.RGB_565)
                .into(imgView);
        //TextView idText = (TextView) view.findViewById(R.id.search_food_id);
        TextView nameText = (TextView) view.findViewById(R.id.search_food_name);
        TextView authorText = (TextView)view.findViewById(R.id.search_food_author);
        assert foodInfo != null;
        //idText.setText(String.valueOf(foodInfo.foodId));
        nameText.setText(foodInfo.foodName);
        authorText.setText(foodInfo.foodAuthor);
        return view;
    }
}
