package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.List;

public class SearchFoodActivity extends AppCompatActivity {
    //暂时先使用最简单的listView,即一个菜品名称构成的listItem,后续会修改成更加多元化的东西
    //private String foodSearchRst[] = {};
    private List<String> foodSearchList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        Intent intent = getIntent();
        String foodSearchStr = intent.getStringExtra("foodSearchStr");
        for(FoodTypeInfo foodType : commonInfo.foodTypeList){
            for(FoodInfo foodInfo:foodType.foodInfoList){
                if(foodInfo.foodName.contains(foodSearchStr))
                    foodSearchList.add("菜品ID："+foodInfo.foodId+",菜品名称："+foodInfo.foodName+",菜品作者:"+foodInfo.foodAuthor);
            }
        }
        ArrayAdapter<String> foodSearchAdapter = new ArrayAdapter<String>(
        this,android.R.layout.simple_list_item_1,foodSearchList);
        ListView listView = (ListView) findViewById(R.id.search_food_list);
        listView.setAdapter(foodSearchAdapter);
    }
}
