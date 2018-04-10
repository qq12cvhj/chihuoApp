package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by qq12cvhj on 2018/4/10.
 */

public class ShareInfoAdapter extends ArrayAdapter {
    private int resourceId;
    public ShareInfoAdapter(@NonNull Context context, int textViewResourceId, @NonNull List objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ShareInfo shareInfo = (ShareInfo) getItem(position);
        ImageView shareImg = (ImageView)view.findViewById(R.id.list_share_img);
        TextView shareTime = (TextView) view.findViewById(R.id.list_share_time);
        TextView shareTitle = (TextView)view.findViewById(R.id.list_share_title);
        shareImg.setImageResource(R.drawable.chihuo);
        shareTime.setText(shareInfo.pubTimeStr);
        shareTitle.setText(shareInfo.shareTitle);
        return view;
}
}
