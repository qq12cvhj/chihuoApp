package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FoodDetailActivity extends AppCompatActivity {

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
                actionBar.setTitle(trFoodName);
            }


        }
    }
}
