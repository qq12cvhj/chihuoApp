package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFoodActivity extends AppCompatActivity {
    private Gson gson = new Gson();
    //暂时先使用最简单的listView,即一个菜品名称构成的listItem,后续会修改成更加多元化的东西
    private List<FoodInfo> foodSearchList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //保证可以在主线程中使用okhttp
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        switch(commonInfo.viewChangeStatus){
            /** 1，用于搜索界面*/
            case 1:
                String foodSearchStr = intent.getStringExtra("foodSearchStr");
                for(FoodTypeInfo foodType : commonInfo.foodTypeList){
                    for(FoodInfo foodInfo:foodType.foodInfoList){
                        if(foodInfo.foodName.contains(foodSearchStr))
                            foodSearchList.add(foodInfo);
                    }
                }
                break;
            case 2:
                int aid = intent.getIntExtra("userId",-1);
                if (aid == -1){
                    toastShow("获取用户id失败");
                    finish();
                }else{
                    foodSearchList = getFavoList(aid);
                }
                break;
            case 3:
                int authodid = intent.getIntExtra("userId",-1);
                if (authodid == -1){
                    toastShow("获取用户id失败");
                    finish();
                }else{
                    foodSearchList = getDesignList(authodid);
                }
                break;
        }

        FoodInfoAdapter foodSearchAdapter = new FoodInfoAdapter(
        this,R.layout.search_food_item,foodSearchList);
        final ListView listView = (ListView) findViewById(R.id.search_food_list);
        listView.setAdapter(foodSearchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodInfo foodInfo = foodSearchList.get(position);
                Toast.makeText(SearchFoodActivity.this,"你点击了第"+foodInfo.foodId+"个菜品",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(SearchFoodActivity.this, FoodDetailActivity.class);
                intent1.putExtra("trFoodid",foodInfo.foodId);
                intent1.putExtra("trFoodName",foodInfo.foodName);
                startActivity(intent1);
            }
        });
    }
    /** 根据作者取得一个作者创作的所有菜品列表*/
    public List<FoodInfo> getDesignList(int foodAuthorId){
        List<FoodInfo> foodlist = new ArrayList<>();
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getDesignList"+foodAuthorId))
                    .build();
            Response response = client.newCall(request).execute();
            String resonseData = response.body().string();
            Log.d("responseData",resonseData);
            foodlist = gson.fromJson(resonseData,new TypeToken<List<FoodInfo>>(){}.getType());
            return foodlist;
        }catch(Exception e){
            e.printStackTrace();
            return foodlist;
        }
    }
    public List<FoodInfo> getFavoList(int foodAuthorId){
        List<FoodInfo> foodlist = new ArrayList<>();
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getFavoList"+foodAuthorId))
                    .build();
            Response response = client.newCall(request).execute();
            String resonseData = response.body().string();
            Log.d("responseData",resonseData);
            foodlist = gson.fromJson(resonseData,new TypeToken<List<FoodInfo>>(){}.getType());
            return foodlist;
        }catch(Exception e){
            e.printStackTrace();
            return foodlist;
        }
    }

    private void toastShow(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
