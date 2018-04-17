package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShareDetailActivity extends AppCompatActivity implements View.OnClickListener {
    WebView getShareInfoWebView;
    int trShareId;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_datail);
        Intent intent = getIntent();
        trShareId = intent.getIntExtra("trShareId",-1);
        //String trShareName = intent.getStringExtra("trShareName");
        /**没得到传来的值，出错，关闭activity*/
        if(trShareId==-1){
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("错误...")
                        .setContentText("操作错误，请重试")
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
            }
            getShareInfoWebView = findViewById(R.id.getShareInfoWebView);
            WebSettings settings = getShareInfoWebView.getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setJavaScriptEnabled(true);
            /** 下面这些是页面缩放的，现在先取消掉*/
            /*settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);
            getShareInfoWebView.setInitialScale(200);*/
            getShareInfoWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Uri uri = Uri.parse(url);
                    if(uri.getScheme().equals("js")){
                        if(uri.getAuthority().equals("webview")){
                            int jsUsrId = Integer.parseInt(uri.getQueryParameter("userId"));
                            String jsNickName = uri.getQueryParameter("nickName");
                            Intent usrHomeIntent = new Intent(ShareDetailActivity.this,UserHomeActivity.class);
                            usrHomeIntent.putExtra("trUserId",jsUsrId);
                            usrHomeIntent.putExtra("trUserNickName",jsNickName);
                            Log.d("yonghuid", String.valueOf(jsUsrId));
                            startActivity(usrHomeIntent);
                        }
                    }
                    //return super.shouldOverrideUrlLoading(view, url);
                    return true;
                }
            });
            getShareInfoWebView.loadUrl(commonInfo.httpUrl("getShareInfo"+trShareId));

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
    private void toastShow(String str){
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }
}
