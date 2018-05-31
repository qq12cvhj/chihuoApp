package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;
import com.liulishuo.share.ShareBlock;
import com.liulishuo.share.model.IShareManager;
import com.liulishuo.share.model.ShareContentWebpage;
import com.liulishuo.share.wechat.WechatShareManager;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FoodDetailActivity extends AppCompatActivity implements  CircleMenu.OnItemClickListener {
    WebView getFoodInfoWebView;
    CircleMenu shareMenu;
    CircleMenuButton starfood,shareWechat;
    int trFoodid;
    String trFoodName;
    String trImgSrc;

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
                //actionBar.setTitle(trFoodName);
            }
            starfood = findViewById(R.id.starfood);
            shareMenu = findViewById(R.id.shareMenu);
            shareWechat = findViewById(R.id.shareWechat);
            shareMenu.setOnItemClickListener(this);
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
                            String jsNickName = uri.getQueryParameter("nickName");
                            Intent usrHomeIntent = new Intent(FoodDetailActivity.this,UserHomeActivity.class);
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
            getFoodInfoWebView.loadUrl(commonInfo.httpUrl("getFoodInfo"+trFoodid));
            if(!commonInfo.loginStatus){
                starfood.setImageResource(R.drawable.like);
                starfood.setTag("");
            }else{
                if(starStatus(commonInfo.currentUserId,trFoodid)){
                    starfood.setImageResource(R.drawable.unlike);
                    starfood.setTag(true);
                }else{
                    starfood.setImageResource(R.drawable.like);
                    starfood.setTag(false);
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
    //设置菜单的点击事件
    @Override
    public void onItemClick(CircleMenuButton menuButton) {
        switch(menuButton.getId()){
                case R.id.starfood:
                    if(menuButton.getTag().equals(false)){
                        starOrCancel(trFoodid,commonInfo.currentUserId);
                        menuButton.setTag(true);
                        menuButton.setImageResource(R.drawable.unlike);
                        toastShow("你收藏了此菜品");
                    }else if(menuButton.getTag().equals(true)){
                        starOrCancel(trFoodid,commonInfo.currentUserId);
                        menuButton.setTag(false);
                        menuButton.setImageResource(R.drawable.like);
                        toastShow("你取消收藏了此菜品");
                    }else{
                        toastShow("登录后才能收藏哦！");
                    }
                    break;
                case R.id.shareWechat:
                    IShareManager iShareManager = new WechatShareManager(FoodDetailActivity.this);
                    iShareManager.share(new ShareContentWebpage(
                            trFoodName,
                            "为你分享最新菜肴，快来试试哦！",
                            commonInfo.httpUrl("getFoodInfo"+trFoodid),
                            trImgSrc
                            ),WechatShareManager.WEIXIN_SHARE_TYPE_TALK);



        }

    }
}

