package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {
    Gson gson;
    public TextView nickNameShowText;
    public Button hisDesignBtn, hisShareBtn,watchBtn_home,unWatchBtn_home;
    public ImageView shareImg_1,shareImg_2,shareImg_3,moreShrBtn;
    public int trUserId;
    public String trUserNickName;
    private List<img> imgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Intent trIntent = getIntent();
        trUserId = trIntent.getIntExtra("trUserId",-1);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Log.d("yonghuid", String.valueOf(trUserId));
        trUserNickName = trIntent.getStringExtra("trUserNickName");
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imgList = getimgList();
        Log.d("imgsize", String.valueOf(imgList.size()));
        initImgs();
        if(commonInfo.loginStatus){
            if(watchStatus(trUserId)){
                watchBtn_home.setVisibility(View.GONE);
            }else{
                unWatchBtn_home.setVisibility(View.GONE);
            }
        }else{
            unWatchBtn_home.setVisibility(View.GONE);
        }
    }

    private void initImgs(){
        switch(imgList.size()){
            case 3:
                shareImg_1.setVisibility(View.VISIBLE);
                shareImg_2.setVisibility(View.VISIBLE);
                shareImg_3.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(imgList.get(0).imgstr)
                        .resize(100,100)
                        .config(Bitmap.Config.RGB_565)
                        .into(shareImg_1);
                Picasso.get()
                        .load(imgList.get(1).imgstr)
                        .resize(100,100)
                        .config(Bitmap.Config.RGB_565)
                        .into(shareImg_2);
                Picasso.get()
                        .load(imgList.get(2).imgstr)
                        .resize(100,100)
                        .config(Bitmap.Config.RGB_565)
                        .into(shareImg_3);
                break;
            case 2:
                shareImg_1.setVisibility(View.VISIBLE);
                shareImg_2.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(imgList.get(0).imgstr)
                        .resize(100,100)
                        .config(Bitmap.Config.RGB_565)
                        .into(shareImg_1);
                Picasso.get()
                        .load(imgList.get(1).imgstr)
                        .resize(100,100)
                        .config(Bitmap.Config.RGB_565)
                        .into(shareImg_2);
                break;
            case 1:
                shareImg_1.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(imgList.get(0).imgstr)
                        .resize(100,100)
                        .config(Bitmap.Config.RGB_565)
                        .into(shareImg_1);
                break;
            case 0:
                break;
        }
    }
    private void initViews(){
        nickNameShowText = findViewById(R.id.nickNameShow);
        nickNameShowText.setText(trUserNickName.trim());
        hisDesignBtn = findViewById(R.id.hisDesignsBtn);
        hisDesignBtn.setOnClickListener(this);
        hisShareBtn = findViewById(R.id.hisShareBtn);
        hisShareBtn.setOnClickListener(this);
        watchBtn_home = findViewById(R.id.watchBtn_home);
        watchBtn_home.setOnClickListener(this);
        unWatchBtn_home = findViewById(R.id.unWatchBtn_home);
        unWatchBtn_home.setOnClickListener(this);
        shareImg_1 = findViewById(R.id.shareImg_1);
        shareImg_2 = findViewById(R.id.shareImg_2);
        shareImg_3 = findViewById(R.id.shareImg_3);
        moreShrBtn = findViewById(R.id.moreShrBtn);
        moreShrBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hisDesignsBtn:
                commonInfo.viewChangeStatus = 3;
                Intent designIntent = new Intent(UserHomeActivity.this,SearchFoodActivity.class);
                designIntent.putExtra("userId",trUserId);
                startActivity(designIntent);
                break;
            case R.id.hisShareBtn:
                Intent shareIntent = new Intent(UserHomeActivity.this,ShareListActivity.class);
                shareIntent.putExtra("userId",trUserId);
                startActivity(shareIntent);
                break;
            case R.id.moreShrBtn:
                Intent moreShrIntent = new Intent(UserHomeActivity.this,ShareListActivity.class);
                moreShrIntent.putExtra("userId",trUserId);
                startActivity(moreShrIntent);
                break;
            case R.id.watchBtn_home:
                if(commonInfo.loginStatus){
                    if(commonInfo.currentUserId == trUserId){
                        toastShow("你为什么要关注自己？");
                    }else{
                        watchOrCancel();
                        watchBtn_home.setVisibility(View.GONE);
                        unWatchBtn_home.setVisibility(View.VISIBLE);
                    }
                }else{
                    toastShow("登录后才能关注哦！");
                }
                break;
            case R.id.unWatchBtn_home:
                watchOrCancel();
                unWatchBtn_home.setVisibility(View.GONE);
                watchBtn_home.setVisibility(View.VISIBLE);
                break;
        }
    }
    private boolean watchStatus(final int usrid){
        final boolean[] status = new boolean[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(commonInfo.httpUrl("watchStatus"+usrid+"/"+commonInfo.currentUserId))
                            .build();
                    Response response = client.newCall(request).execute();
                    int responseData = Integer.parseInt(response.body().string());
                    if(responseData == 1) status[0] = true;
                    else status[0] = false;
                }catch(Exception e){
                    e.printStackTrace();
                    status[0] = false;
                }
            }
        }).run();
        return status[0];
    }
    private void watchOrCancel(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(commonInfo.httpUrl("watchOrCancel"+trUserId+"/"+commonInfo.currentUserId))
                            .build();
                    client.newCall(request).execute();

                }catch (Exception e){

                }
            }
        }).run();
    }


    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
    private List<img> getimgList(){
         List<img> imglist = new ArrayList<>();
        try{
            gson = new Gson();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getimgs/"+trUserId))
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            Log.d("responseData",responseData);
            imglist = gson.fromJson(responseData,new TypeToken<List<img>>(){}.getType());
            return imglist;
        }catch(Exception e){
            e.printStackTrace();
            return imglist;
        }
    }
    class img{
        int imgid;
        String imgstr;
    }
}
