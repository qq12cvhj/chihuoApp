package com.example.qq12cvhj.chihuo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FoodDetailActivity extends AppCompatActivity {
    WebView getFoodInfoWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_datail);
        Intent intent = getIntent();
        int trFoodid = intent.getIntExtra("trFoodid",-1);
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
            getFoodInfoWebView = findViewById(R.id.getFoodInfoWebView);
            WebSettings settings = getFoodInfoWebView.getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            /** 下面这些是页面缩放的，现在先取消掉*/
            /*settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
            getFoodInfoWebView.setInitialScale(200);*/
            getFoodInfoWebView.setWebChromeClient(new WebChromeClient(){});
            getFoodInfoWebView.loadUrl(commonInfo.httpUrl("getFoodInfo"+trFoodid));
        }
    }




}
