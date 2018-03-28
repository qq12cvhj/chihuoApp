package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView testLoginText;
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
                Toast.makeText(getContext(),"你点击了登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.login2RegBtn:
                Intent intent = new Intent(getActivity(),RegActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userlogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody loginRequestBody = new FormBody.Builder()
                            .add("username",usernameLoginInput.getText().toString())
                            .add("password",passwordLoginInput.getText().toString())
                            .build();
                    Request loginrequest = new Request.Builder().url("http://192.168.1.110:5000/login").post(loginRequestBody).build();
                    Response loginResponse = client.newCall(loginrequest).execute();
                    String loginResponseData = loginResponse.body().string();
                    //Toast.makeText(getContext(),loginResponseData,Toast.LENGTH_SHORT).show();
                    Log.d("login",loginResponseData);
                    testLoginText = getActivity().findViewById(R.id.testLoginText);
                    testLoginText.setText(loginResponseData);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
