package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by qq12cvhj on 2018/4/12.
 */

public class ActionAdapter extends ArrayAdapter<UserAction>{
    private int resourceId;
    public ActionAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<UserAction> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        final UserAction userAction = getItem(position);
        ImageView actionImg = (ImageView)view.findViewById(R.id.list_action_img);
        TextView actionText = (TextView)view.findViewById(R.id.list_action_text);
        TextView actionTitle = (TextView)view.findViewById(R.id.list_action_title);
        TextView actionTime = (TextView)view.findViewById(R.id.list_action_time);
        Picasso.get()
                .load(userAction.titleImg)
                .resize(200,200)
                .config(Bitmap.Config.RGB_565)
                .into(actionImg);
        switch (userAction.actionType){
            case 1:
                actionText.setText(userAction.subjectName + " 创建了新菜品 ");
                actionTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastShow("前往菜品"+userAction.objectId);
                        Intent intent1 = new Intent(getContext(),FoodDetailActivity.class);
                        intent1.putExtra("trFoodid",userAction.objectId);
                        getContext().startActivity(intent1);
                    }
                });
                break;
            case 2:
                actionText.setText(userAction.subjectName + " 发布了新的分享 ");
                actionTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastShow("前往分享"+userAction.objectId);
                        Intent intent2 = new Intent(getContext(),ShareDetailActivity.class);
                        intent2.putExtra("trShareId",userAction.objectId);
                        getContext().startActivity(intent2);
                    }
                });
                break;
            case 3:
                actionText.setText(userAction.subjectName + " 收藏了菜品 ");
                actionTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastShow("前往菜品"+userAction.objectId);
                        Intent intent3 = new Intent(getContext(),FoodDetailActivity.class);
                        intent3.putExtra("trFoodid",userAction.objectId);
                        getContext().startActivity(intent3);
                    }
                });
                break;
        }
        actionTitle.setText(userAction.objectName);
        actionTime.setText(userAction.actionTime);
        return view;
    }
    private void toastShow(String str){
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}

