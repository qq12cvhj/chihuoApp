package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.liulishuo.share.ShareBlock;
import com.liulishuo.share.model.IShareManager;
import com.liulishuo.share.model.ShareContentWebpage;
import com.liulishuo.share.wechat.WechatShareManager;
import com.michaldrabik.tapbarmenulib.TapBarMenu;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FoodDetailActivity extends AppCompatActivity implements   View.OnClickListener {
    WebView getFoodInfoWebView;
    int trFoodid;
    String trFoodName;
    String trImgSrc;
    TapBarMenu shrmenu;
    ImageView starbtn,shrwx,shrpyq;
    TextView startext;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_datail);
        ShareBlock.getInstance().initShare(commonInfo.wechatAppId, "", "",
                commonInfo.wechatSecret);
        Intent intent = getIntent();
        trFoodid = intent.getIntExtra("trFoodid",-1);
        trFoodName = intent.getStringExtra("trFoodName");
        trImgSrc = intent.getStringExtra("trImgSrc");
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
            }
            shrmenu = findViewById(R.id.shrMenu);
            shrmenu.setOnClickListener(this);
            starbtn = findViewById(R.id.starbtn);
            starbtn.setOnClickListener(this);
            shrwx = findViewById(R.id.shrwx);
            shrwx.setOnClickListener(this);
            shrpyq = findViewById(R.id.shrpyq);
            shrpyq.setOnClickListener(this);
            startext = findViewById(R.id.startext);
            getFoodInfoWebView = findViewById(R.id.getFoodInfoWebView);
            WebSettings settings = getFoodInfoWebView.getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setJavaScriptEnabled(true);
            getFoodInfoWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Uri uri = Uri.parse(url);
                    if(uri.getScheme().equals("js")){
                        if(uri.getAuthority().equals("webview")){
                            int jsUsrId = Integer.parseInt(uri.getQueryParameter("userId"));
                            String jsNickName = uri.getQueryParameter("nickName");
                            Intent usrHomeIntent = new Intent(FoodDetailActivity.this,UserHomeActivity.class);
                            usrHomeIntent.putExtra("trUserId",jsUsrId);
                            usrHomeIntent.putExtra("trUserNickName",jsNickName);
                            Log.d("yonghuid", String.valueOf(jsUsrId));
                            startActivity(usrHomeIntent);
                        }
                    }
                    return true;
                }
            });
            getFoodInfoWebView.loadUrl(commonInfo.httpUrl("getFoodInfo"+trFoodid));
            if(!commonInfo.loginStatus){
                starbtn.setImageResource(R.drawable.like);
                startext.setText("添加到收藏");
            }else{
                if(starStatus(commonInfo.currentUserId,trFoodid)){
                    starbtn.setImageResource(R.drawable.unlike);
                    startext.setText("已收藏");
                }else{
                    starbtn.setImageResource(R.drawable.like);
                    startext.setText("添加到收藏");
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

    private void toastShow(String str){
        Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.shrMenu:
                shrmenu.toggle();
                break;
            case R.id.starbtn:
                if(!commonInfo.loginStatus){
                    toastShow("登录后才可以收藏！");
                }else{
                    if(startext.getText().toString().trim().equals("已收藏")){
                        starOrCancel(trFoodid,commonInfo.currentUserId);
                        starbtn.setImageResource(R.drawable.like);
                        startext.setText("添加到收藏");
                    }else{
                        starOrCancel(trFoodid,commonInfo.currentUserId);
                        starbtn.setImageResource(R.drawable.unlike);
                        startext.setText("已收藏");
                    }
                }
                break;
            case R.id.shrwx:
                IShareManager iShareManager = new WechatShareManager(FoodDetailActivity.this);
                iShareManager.share(new ShareContentWebpage(
                        trFoodName,
                        "为你分享最新菜肴，快来试试哦！",
                        commonInfo.httpUrl("getFoodInfo"+trFoodid),
                        trImgSrc
                ),WechatShareManager.WEIXIN_SHARE_TYPE_TALK);
                break;
            case R.id.shrpyq:
                IShareManager shareManager = new WechatShareManager(FoodDetailActivity.this);
                shareManager.share(new ShareContentWebpage(
                        trFoodName,
                        "为你分享最新菜肴，快来试试哦！",
                        commonInfo.httpUrl("getFoodInfo"+trFoodid),
                        trImgSrc
                ),WechatShareManager.WEIXIN_SHARE_TYPE_FRENDS);
                break;
        }
    }
}

