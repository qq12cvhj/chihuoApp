package com.example.qq12cvhj.chihuo;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNewFoodTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView foodTypeInput,descInput;
    private Button addFoodTypeBtn;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_type);
        foodTypeInput = findViewById(R.id.foodTypeInput);
        descInput = findViewById(R.id.descInput);
        addFoodTypeBtn = findViewById(R.id.addFoodTypeBtn);
        addFoodTypeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFoodTypeBtn:
                addNewFoodType();
                break;
        }
    }
    private void addNewFoodType(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                  .setTitleText("上传中，请稍后");
        if(foodTypeInput.getText().toString().trim().equals("")  || descInput.getText().toString().trim().equals("") ){
            toastShow("名称和简介不能为空哦！");
        }else{
            pDialog.show();
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    add_send();
                }
            },1000);
        }
    }

    private void add_send(){
        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody
                    .Builder()
                    .add("foodTypeInput",foodTypeInput.getText().toString())
                    .add("foodDescInput",descInput.getText().toString())
                    .build();
            Request request = new Request
                    .Builder()
                    .url(commonInfo.httpUrl("addNewFoodType"))
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            int responseData = Integer.parseInt(response.body().string());
            switch(responseData){
                case -1:
                    pDialog.setTitleText("创建失败，请重试!")
                            .setConfirmText("好的")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    break;

                case 0:
                    pDialog.setTitleText("创建成功!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
            pDialog.setTitleText("网络超时，请重试")
                    .setConfirmText("好的")
                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        }
    }

    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
