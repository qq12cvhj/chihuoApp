package com.example.qq12cvhj.chihuo;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Timeout;

public class RegActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameRegInput;
    EditText passwordRegInput;
    EditText nicknameRegInput;
    Button userRegBtn;
    boolean sent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //保证可以在主线程中使用okhttp
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //下面是页面加载操作
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        usernameRegInput = findViewById(R.id.usernameRegInput);
        passwordRegInput = findViewById(R.id.passwordRegInput);
        nicknameRegInput = findViewById(R.id.nicknameRegInput);
        userRegBtn = findViewById(R.id.userRegBtn);
        userRegBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userRegBtn:
                sent = true;
                clickToReg();
                if(!sent){
                    toastShow("网络错误，请重试");
                }
                break;
        }
    }

    private void clickToReg() {
           if( usernameRegInput.getText().toString().trim().equals("")
            || passwordRegInput.getText().toString().trim().equals("")
            || nicknameRegInput.getText().toString().trim().equals("")){
                toastShow("用户名、密码、昵称均不能为空");
           }else{
               new Thread(new Runnable() {
                   //判断信息是否发送出去
                   @Override
                   public void run() {
                        try{
                            OkHttpClient client = new OkHttpClient();
                            RequestBody regRequestBody = new FormBody.Builder()
                                    .add("username",usernameRegInput.getText().toString())
                                    .add("password",passwordRegInput.getText().toString())
                                    .add("nickname",nicknameRegInput.getText().toString())
                                    .build();
                            Request regrequest = new Request.Builder().url(commonInfo.httpUrl("reg"))
                                    .post(regRequestBody).build();
                            Response regResponse = client.newCall(regrequest).execute();
                            String regResponseData = regResponse.body().string();
                            int regReturn = Integer.parseInt(regResponseData);
                            switch (regReturn){
                                case -3:
                                    toastShow("系统错误，注册失败");
                                    break;
                                case -2:
                                    toastShow("用户名已存在");
                                    break;
                                case -1:
                                    toastShow("昵称已存在");
                                    break;
                                case 0:
                                    toastShow("注册成功，前往登录页面中……");
                                    Thread.sleep(1000);
                                    finish();
                                    break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            //没发出去，网络请求失败
                            sent = false;
                        }
                   }
               }).run();
           }
    }
    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
