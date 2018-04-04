package com.example.qq12cvhj.chihuo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewFoodTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView foodTypeInput,descInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_type);
        foodTypeInput = findViewById(R.id.foodTypeInput);
        foodTypeInput.setOnClickListener(this);
        descInput = findViewById(R.id.descInput);
        descInput.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.foodTypeInput:
                break;
            case R.id.descInput:
                break;
        }
    }

    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
