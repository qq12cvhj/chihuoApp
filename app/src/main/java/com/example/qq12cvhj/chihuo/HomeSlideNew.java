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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qq12cvhj on 2018/4/23.
 */

public class HomeSlideNew extends Fragment {
    Gson gson;
    private ListView newFoodListview;
    private List<FoodInfo> newFoodList;
    private TextView new_tip;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.home_slide_new,container,false);
        return view;
    }

    @Override
    public void onResume() {
        newFoodList = getNewFoodList();
        initViews();
        if(newFoodList.size() == 0){
            new_tip = getActivity().findViewById(R.id.new_tip);
            new_tip.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }

    private void initViews(){
        newFoodListview = getActivity().findViewById(R.id.newFoodList);
        FoodInfoAdapter foodInfoAdapter = new FoodInfoAdapter(getContext(),R.layout.search_food_item,newFoodList);
        newFoodListview.setAdapter(foodInfoAdapter);
        newFoodListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodInfo foodInfo = newFoodList.get(position);
                Intent foodIntent = new Intent(getContext(),FoodDetailActivity.class);
                foodIntent.putExtra("trFoodid",foodInfo.foodId);
                foodIntent.putExtra("trFoodName",foodInfo.foodName);
                foodIntent.putExtra("trImgSrc",foodInfo.foodImgSrc);
                getContext().startActivity(foodIntent);
            }
        });
    }
    private List<FoodInfo> getNewFoodList(){
        gson = new Gson();
        List<FoodInfo> foodlist;
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getNewFoodList"))
                    .build();
            Response response = client.newCall(request).execute();
            String resonseData = response.body().string();
            Log.d("responseData",resonseData);
            foodlist = gson.fromJson(resonseData,new TypeToken<List<FoodInfo>>(){}.getType());
        }catch(Exception e){
            foodlist = new ArrayList<>();
        }
        return foodlist;
    }
}
