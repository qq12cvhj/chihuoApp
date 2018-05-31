package com.example.qq12cvhj.chihuo;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import devlight.io.library.ntb.NavigationTabBar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qq12cvhj on 2018/4/12.
 */

public class FriendContent extends Fragment implements View.OnClickListener {
    private List<UserAction> actionList = new ArrayList<>();
    private Gson gson = new Gson();
    private ListView actionListView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
    }

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
    public void onResume() {
        if(commonInfo.loginStatus){
            actionList = getActionList();
            actionListView = (ListView) getActivity().findViewById(R.id.action_list);
            ActionAdapter actionAdapter = new ActionAdapter(
                    getContext(),R.layout.list_action_item,actionList);
            actionListView.setAdapter(actionAdapter);
        }else{
            View friend_out = getActivity().findViewById(R.id.friend_out);
            friend_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取得底部菜单栏，setModelIndex()为底部索引，即触发点击事件。
                    NavigationTabBar navigationTabBar = getActivity().findViewById(R.id.ntb_horizontal);
                    navigationTabBar.setModelIndex(3);
                }
            });
        }
        super.onResume();
    }

    private List<UserAction> getActionList(){
        List<UserAction> list = new ArrayList<>();
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getActionList"+commonInfo.currentUserId))
                    .build();
            Response response = client.newCall(request).execute();
            String resonseData = response.body().string();
            Log.d("getActionList",resonseData);
            list = gson.fromJson(resonseData,new TypeToken<List<UserAction>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return list;
    }
    @Override
    public void onClick(View v) {

    }
}
