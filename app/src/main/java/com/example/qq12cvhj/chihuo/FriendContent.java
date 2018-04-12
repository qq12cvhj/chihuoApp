package com.example.qq12cvhj.chihuo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

/**
 * Created by qq12cvhj on 2018/4/12.
 */

public class FriendContent extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if(commonInfo.loginStatus){
            view = inflater.inflate(R.layout.friend_content_in,null,false);
        }
        else{
            view = inflater.inflate(R.layout.friend_content_out,null,false);
        }
        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
