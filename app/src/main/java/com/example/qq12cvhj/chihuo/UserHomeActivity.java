package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView nickNameShowText;
    public Button hisDesignBtn, hisShareBtn,watchBtn_home,unWatchBtn_home;
    public ImageView shareImg_1,shareImg_2,shareImg_3;
    public int trUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Intent trIntent = getIntent();
        trUserId = trIntent.getIntExtra("trUserId",-1);
        Log.d("yonghuid", String.valueOf(trUserId));
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews(){
        nickNameShowText = findViewById(R.id.nickNameShow);
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
            case R.id.watchBtn_home:
                if(commonInfo.loginStatus){

                }else{
                    toastShow("登录后才能关注哦！");
                }
                break;
            case R.id.unWatchBtn_home:
                break;
        }
    }
    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
