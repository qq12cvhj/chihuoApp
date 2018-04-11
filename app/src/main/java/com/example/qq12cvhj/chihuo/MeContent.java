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

import com.google.gson.Gson;

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
    Button myInfoBtn;
    Button myFavoriteBtn;
    Button myDesignBtn;
    Button myShareBtn;
    Button myFollowBtn;
    Button logoutBtn;
    FragmentManager fm;
    FragmentTransaction ft;
    public int loginReturn = -4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view ;
        if(commonInfo.loginStatus){
            view = inflater.inflate(R.layout.me_content_in, null, false);
            getCurrentUserInfo();
        }
        else{
            view = inflater.inflate(R.layout.me_content_out, null, false);
        }
        return view;
    }

    @Override
    public void onResume() {
        if(commonInfo.loginStatus){
            myInfoBtn = (Button) getView().findViewById(R.id.myInfoBtn);
            myInfoBtn.setOnClickListener(this);
            myFavoriteBtn = (Button)getView().findViewById(R.id.myFavoriteBtn);
            myFavoriteBtn.setOnClickListener(this);
            myDesignBtn =(Button) getView().findViewById(R.id.myDesignBtn);
            myDesignBtn.setOnClickListener(this);
            myShareBtn = (Button)getView().findViewById(R.id.myShareBtn);
            myShareBtn.setOnClickListener(this);
            myFollowBtn = (Button)getView().findViewById(R.id.myFollowBtn);
            myFollowBtn.setOnClickListener(this);
            logoutBtn = (Button)getView().findViewById(R.id.logoutBtn);
            logoutBtn.setOnClickListener(this);
        }else{
            usernameLoginInput = (EditText) getActivity().findViewById(R.id.usernameLoginInput);
            passwordLoginInput = (EditText) getActivity().findViewById(R.id.passwordLoginInput);
            Button userLoginBtn = (Button)getActivity().findViewById(R.id.userLoginBtn);
            Button login2RegBtn = (Button)getActivity().findViewById(R.id.login2RegBtn);
            userLoginBtn.setOnClickListener(this);
            login2RegBtn.setOnClickListener(this);
        }
        super.onResume();
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
            case R.id.myInfoBtn:
                Log.d("aaa","111");
                Intent myinfointent = new Intent(getActivity(),myInfoActivity.class);
                startActivity(myinfointent);
                break;
            case R.id.myFavoriteBtn:
                Log.d("aaa","222");
                commonInfo.viewChangeStatus = 2;
                Intent favoIntent = new Intent(getActivity(),SearchFoodActivity.class);
                favoIntent.putExtra("userId",commonInfo.currentUserId);
                startActivity(favoIntent);
                break;
            case R.id.myDesignBtn:
                commonInfo.viewChangeStatus = 3;
                Intent designIntent = new Intent(getActivity(),SearchFoodActivity.class);
                designIntent.putExtra("userId",commonInfo.currentUserId);
                Log.d("aaa","333");
                startActivity(designIntent);
                break;
            case R.id.myShareBtn:
                Intent intent1 = new Intent(getActivity(),ShareListActivity.class);
                startActivity(intent1);
                Log.d("aaa","444");
                break;
            case R.id.myFollowBtn:
                Log.d("aaa","555");
                break;
            case R.id.logoutBtn:
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要退出登录么?")
                        .setContentText("退出后只能查看食谱")
                        .setConfirmText("退出")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                commonInfo.loginStatus = false;
                                commonInfo.currentUserId = -1;
                                MeContent newMecontent1 = new MeContent();
                                fm = getFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.me_fragment,newMecontent1);
                                ft.commit();
                                sDialog.setTitleText("退出成功!")
                                        .setContentText("信息不再保留，请重新登录!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        }).show();
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
    public void getCurrentUserInfo(){
        if(commonInfo.loginStatus){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        OkHttpClient client = new OkHttpClient();
                        RequestBody getInfoRequestBody = new FormBody.Builder()
                                .add("currentUserId",String.valueOf(commonInfo.currentUserId))
                                .build();
                        Request getInforequest = new Request.Builder().url(commonInfo.httpUrl("getCurrentUserInfo")).post(getInfoRequestBody).build();
                        Response getInfoResponse = client.newCall(getInforequest).execute();
                        String getInfoResponseData = getInfoResponse.body().string();
                        Gson gson = new Gson();
                        commonInfo.currentUser = gson.fromJson(getInfoResponseData,User.class);

                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(getContext(),"获取用户信息失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();
        }else{
            //错误，没登录是获取不到信息的
        }
    }
}
