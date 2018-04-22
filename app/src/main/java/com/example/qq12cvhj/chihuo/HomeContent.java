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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qq12cvhj on 2018/4/22.
 */

public class HomeContent extends Fragment {
    private Banner homebanner;
    private List<UserAction> actionList = new ArrayList<>();
    private Gson gson;
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
        view = inflater.inflate(R.layout.home_content,container,false);
        return view;
    }
    private void initviews(){
        gson = new Gson();
        actionList = getActionList();
        homebanner = (Banner) getActivity().findViewById(R.id.home_banner);
        homebanner.setImageLoader(new homeImgLoader());
        homebanner.setImages(actionList);
        homebanner.start();
    }

    @Override
    public void onResume() {
        initviews();
        super.onResume();
    }

    private List<UserAction> getActionList(){
        List<UserAction> list ;
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getActionList"))
                    .build();
            Response response = client.newCall(request).execute();
            String resonseData = response.body().string();
            Log.d("responseData1",resonseData);
            list = gson.fromJson(resonseData,new TypeToken<List<UserAction>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return list;
    }
}
