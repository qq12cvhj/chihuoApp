package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.CipherSuite;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FoodDetailActivity extends AppCompatActivity implements View.OnClickListener {
    WebView getFoodInfoWebView;
    Button starBtn;
    int trFoodid;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_datail);
        Intent intent = getIntent();
        trFoodid = intent.getIntExtra("trFoodid",-1);
        String trFoodName = intent.getStringExtra("trFoodName");
        /**没得到传来的值，出错，关闭activity*/
        if(trFoodid==-1){
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("错误...")
                        .setContentText("操作错误，无法获取此菜品信息，请返回上一层")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                            }
                        }).show();
        }else{
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null){
                actionBar.hide();
                //actionBar.setTitle(trFoodName);
            }
            starBtn = findViewById(R.id.starBtn);
            starBtn.setOnClickListener(this);
            getFoodInfoWebView = findViewById(R.id.getFoodInfoWebView);
            WebSettings settings = getFoodInfoWebView.getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setJavaScriptEnabled(true);

            /** 下面这些是页面缩放的，现在先取消掉*/
            /*settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
            getFoodInfoWebView.setInitialScale(200);*/
            getFoodInfoWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Uri uri = Uri.parse(url);
                    if(uri.getScheme().equals("js")){
                        if(uri.getAuthority().equals("webview")){
                            int jsUsrId = Integer.parseInt(uri.getQueryParameter("userId"));
                            Intent usrHomeIntent = new Intent(getApplicationContext(),UserHomeActivity.class);
                            usrHomeIntent.putExtra("trUserId",jsUsrId);
                            Log.d("yonghuid", String.valueOf(jsUsrId));
                            getApplicationContext().startActivity(usrHomeIntent);
                        }
                    }
                    //return super.shouldOverrideUrlLoading(view, url);
                    return true;
                }
            });
            getFoodInfoWebView.loadUrl(commonInfo.httpUrl("getFoodInfo"+trFoodid));
            if(!commonInfo.loginStatus){
                starBtn.setText("登录收藏");
                starBtn.setBackgroundResource(R.drawable.shape_white_button);
            }else{
                if(starStatus(commonInfo.currentUserId,trFoodid)){
                    starBtn.setBackgroundResource(R.drawable.red_button_background);
                    starBtn.setText("取消收藏");
                }else{
                    starBtn.setBackgroundResource(R.drawable.shape_white_button);
                    starBtn.setText("点击收藏");
                }

            }
        }
    }

    private void starOrCancel(final int foodId, final int userId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(commonInfo.httpUrl("starOrCancel"+foodId+"/"+userId))
                            .build();
                    client.newCall(request).execute();

                }catch (Exception e){

                }
            }
        }).run();

    }

    private int getStarCount(int foodId){
        final int[] responseData = new int[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(commonInfo.httpUrl("getStarCount"+ trFoodid))
                            .build();
                    Response response = client.newCall(request).execute();
                    responseData[0] = Integer.parseInt(response.body().string());
                    Log.d("responseData", String.valueOf(responseData[0]));
                }catch(Exception e){
                    e.printStackTrace();
                    responseData[0] = 0;
                }
            }
        }).run();
        return responseData[0];
    }
    private boolean starStatus(final int userId, final int foodId){
        final boolean[] status = new boolean[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(commonInfo.httpUrl("starStatus"+foodId+"/"+userId))
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


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.starBtn:
                if(starBtn.getText().equals("点击收藏")){
                    starOrCancel(trFoodid,commonInfo.currentUserId);
                    starBtn.setBackgroundResource(R.drawable.red_button_background);
                    starBtn.setText("取消收藏");
                }else if(starBtn.getText().equals("取消收藏")){
                    starOrCancel(trFoodid,commonInfo.currentUserId);
                    starBtn.setText("点击收藏");
                    starBtn.setBackgroundResource(R.drawable.shape_white_button);
                }else{
                    toastShow("登录后才能收藏哈！！");
                }
                break;
        }

    }
    private void toastShow(String str){
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }

}
