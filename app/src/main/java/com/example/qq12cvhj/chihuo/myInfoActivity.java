package com.example.qq12cvhj.chihuo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                break;
        }
    }
}
