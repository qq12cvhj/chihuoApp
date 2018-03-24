package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class SearchFoodActivity extends AppCompatActivity {
    //暂时先使用最简单的listView,即一个菜品名称构成的listItem,后续会修改成更加多元化的东西
    private List<FoodInfo> foodSearchList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        Intent intent = getIntent();
        String foodSearchStr = intent.getStringExtra("foodSearchStr");
        for(FoodTypeInfo foodType : commonInfo.foodTypeList){
            for(FoodInfo foodInfo:foodType.foodInfoList){
                if(foodInfo.foodName.contains(foodSearchStr))
                    foodSearchList.add(foodInfo);
            }
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
}
