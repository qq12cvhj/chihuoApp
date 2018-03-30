package com.example.qq12cvhj.chihuo;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameEdit,
            nickNameEdit,
            emailAddressEdit,
            phoneNumberEdit,
            selfIntroEdit;
    private Button myInfoModifyBtn,
            myInfoSaveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        //保证可以在主线程中使用okhttp
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        userNameEdit = findViewById(R.id.userNameEdit);
        nickNameEdit = findViewById(R.id.nickNameEdit);
        emailAddressEdit = findViewById(R.id.emailAddressEdit);
        phoneNumberEdit = findViewById(R.id.phoneNumberEdit);
        selfIntroEdit = findViewById(R.id.selfIntroEdit);
        myInfoModifyBtn = findViewById(R.id.myInfoModifyBtn);
        myInfoSaveBtn = findViewById(R.id.myInfoSaveBtn);
        myInfoModifyBtn.setOnClickListener(this);
        myInfoSaveBtn.setOnClickListener(this);
        initViews();
        super.onStart();
    }
    private void initViews(){

        userNameEdit.setText(commonInfo.currentUser.getUsername());
        userNameEdit.setEnabled(false);
        nickNameEdit.setText(commonInfo.currentUser.getNickname());
        nickNameEdit.setEnabled(false);
        emailAddressEdit.setText(commonInfo.currentUser.getEmailAddress());
        emailAddressEdit.setEnabled(false);
        phoneNumberEdit.setText(commonInfo.currentUser.getPhoneNumber());
        phoneNumberEdit.setEnabled(false);
        selfIntroEdit.setText(commonInfo.currentUser.getSelfIntroduction());
        selfIntroEdit.setEnabled(false);
        myInfoSaveBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.myInfoModifyBtn:
                if(myInfoModifyBtn.getText().toString().trim().equals("修改信息")){
                    myInfoModifyBtn.setText("取消");
                    myInfoSaveBtn.setVisibility(View.VISIBLE);
                    nickNameEdit.setEnabled(true);
                    emailAddressEdit.setEnabled(true);
                    phoneNumberEdit.setEnabled(true);
                    selfIntroEdit.setEnabled(true);
                }else{
                    initViews();
                    myInfoModifyBtn.setText("修改信息");
                }
                break;
            case R.id.myInfoSaveBtn:
                switch (modifyMyInfo()){
                    case -1:
                        toastShow("网络错误，请重试");
                        break;
                    case -2:
                        toastShow("修改失败，昵称已经存在");
                        break;
                    case 1:
                        toastShow("恭喜，修改成功");
                        commonInfo.currentUser.nickname = nickNameEdit.getText().toString();
                        commonInfo.currentUser.emailAddress = emailAddressEdit.getText().toString();
                        commonInfo.currentUser.phoneNumber = phoneNumberEdit.getText().toString();
                        commonInfo.currentUser.selfIntroduction = selfIntroEdit.getText().toString();
                        myInfoModifyBtn.setText("修改信息");
                        initViews();
                        break;
                }
                break;
        }
    }
    public int modifyMyInfo(){
        final int[] modifyStatus = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(3, TimeUnit.SECONDS)
                            .build();
                    RequestBody modifyBody = new FormBody.Builder()
                            .add("currentUserId", String.valueOf(commonInfo.currentUserId))
                            .add("nickname",nickNameEdit.getText().toString())
                            .add("emailAddress",emailAddressEdit.getText().toString())
                            .add("phoneNumber",phoneNumberEdit.getText().toString())
                            .add("selfIntro",selfIntroEdit.getText().toString())
                            .build();
                    Request modifyRequest = new Request.Builder()
                            .url(commonInfo.httpUrl("modifyMyInfo"))
                            .post(modifyBody)
                            .build();
                    Response modifyResponse = client.newCall(modifyRequest).execute();
                    int modifyResponseData = Integer.parseInt(modifyResponse.body().string());
                    Log.d("returndata",String.valueOf(modifyResponseData));
                    modifyStatus[0] = modifyResponseData;
                }catch (Exception e){
                    e.printStackTrace();
                    modifyStatus[0] = -1;
                }
            }
        }).run();
        return modifyStatus[0];
    }
    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
