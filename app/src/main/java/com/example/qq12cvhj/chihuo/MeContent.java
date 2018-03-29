package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by qq12cvhj on 2018/3/28.
 */

public class MeContent extends Fragment implements View.OnClickListener {
    EditText usernameLoginInput ;
    EditText passwordLoginInput ;
    FragmentManager fm;
    FragmentTransaction ft;
    public int loginReturn = -4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view ;
        if(commonInfo.loginStatus){
            view = inflater.inflate(R.layout.me_content_in, null, false);
        }
        else{
            view = inflater.inflate(R.layout.me_content_out, null, false);
        }
        return view;
    }

    @Override
    public void onStart() {
        if(commonInfo.loginStatus){

        }else{
            usernameLoginInput = (EditText) getActivity().findViewById(R.id.usernameLoginInput);
            passwordLoginInput = (EditText) getActivity().findViewById(R.id.passwordLoginInput);
            Button userLoginBtn = (Button)getActivity().findViewById(R.id.userLoginBtn);
            Button login2RegBtn = (Button)getActivity().findViewById(R.id.login2RegBtn);
            userLoginBtn.setOnClickListener(this);
            login2RegBtn.setOnClickListener(this);
        }
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userLoginBtn:
                userlogin();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (loginReturn){
                    case -3:
                        showErrDiag("网络连接出错了");
                        //状态回退。
                        loginReturn = -4;
                        break;
                    case -2:
                        showErrDiag("密码错误");
                        //状态回退。
                        loginReturn = -4;
                        break;
                    case -1:
                        showErrDiag("用户名不存在");
                        //状态回退。
                        loginReturn = -4;
                        break;
                    case -4:
                        if(usernameLoginInput.getText().toString().trim().equals("") || passwordLoginInput.getText().toString().trim().equals("")){
                        }else{
                            Toast.makeText(getContext(),"抱歉，请重试",Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        Toast.makeText(getContext(),"恭喜，登录成功",Toast.LENGTH_SHORT).show();
                        commonInfo.currentUserId = loginReturn;
                        commonInfo.loginStatus = true;
                        MeContent newMecontent = new MeContent();
                        fm = getActivity().getSupportFragmentManager();
                        ft = fm.beginTransaction();
                        ft.replace(R.id.me_fragment,newMecontent);
                        ft.commit();
                }
                break;
            case R.id.login2RegBtn:
                Intent intent = new Intent(getActivity(),RegActivity.class);
                startActivity(intent);
                break;
        }
    }

    public  void userlogin() {
        if(usernameLoginInput.getText().toString().trim().equals("") || passwordLoginInput.getText().toString().trim().equals("")){
            showErrDiag("用户名或密码不能为空");
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        OkHttpClient client = new OkHttpClient();
                        RequestBody loginRequestBody = new FormBody.Builder()
                                .add("username",usernameLoginInput.getText().toString())
                                .add("password",passwordLoginInput.getText().toString())
                                .build();
                        Request loginrequest = new Request.Builder().url(commonInfo.httpUrl("login")).post(loginRequestBody).build();
                        Response loginResponse = client.newCall(loginrequest).execute();
                        String loginResponseData = loginResponse.body().string();
                        Log.d("loginReturn",loginResponseData);
                        loginReturn = Integer.parseInt(loginResponseData);
                    }catch(Exception e){
                        e.printStackTrace();
                        loginReturn = -3;
                    }
                }
            }).start();
        }

    }
    public void showErrDiag(String str){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("出了点错误...")
                .setContentText(str)
                .show();
    }
    public void showSuccDiag(String str){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("恭喜!")
                .setContentText("str")
                .show();
    }
}
