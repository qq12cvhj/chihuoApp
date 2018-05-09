package com.example.qq12cvhj.chihuo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.james.biuedittext.BiuEditText;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qq12cvhj on 2018/3/18.
 */

public class CookbookContent extends Fragment implements View.OnClickListener {
    private BiuEditText searchFoodEditText;
    public Button searchFoodBtn;
    private View cbSlideView;
    private GridView grid_foodtype;
    private List<FoodTypeInfo> foodTypeInfoList;
    private Gson gson;
    private FragmentManager fm;
    private FragmentTransaction ft;
    public static SlideUp cbSlideUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //保证可以在主线程中使用okhttp
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view = inflater.inflate(R.layout.cookbook_content, container, false);
        searchFoodBtn = view.findViewById(R.id.foodSearchBtn);
        searchFoodEditText = view.findViewById(R.id.foodSearchEditText);
        searchFoodBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        gson = new Gson();
        foodTypeInfoList = getTypeList();
        grid_foodtype = getActivity().findViewById(R.id.grid_foodtype);
        FoodTypeAdapter foodTypeAdapter = new FoodTypeAdapter(getContext(),R.layout.food_type_item,foodTypeInfoList);
        cbSlideView = getActivity().findViewById(R.id.cb_slideView);
        cbSlideUp = new SlideUpBuilder(cbSlideView)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .build();
        grid_foodtype.setAdapter(foodTypeAdapter);
        grid_foodtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodTypeInfo foodTypeInfo = foodTypeInfoList.get(position);
                CbSlide cbSlide = new CbSlide(foodTypeInfo);
                fm = getFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.cb_slide_fragment,cbSlide);
                ft.commit();
                cbSlideUp.show();
            }
        });

        super.onResume();
    }

    static class CbFoodAdapter extends ArrayAdapter<FoodInfo>{
        int resourceId;
        public CbFoodAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<FoodInfo> objects) {
            super(context,  textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            TextView cbFoodName = view.findViewById(R.id.cb_food_name);
            FoodInfo foodInfo = getItem(position);
            cbFoodName.setText(foodInfo.foodName);
            return view;
        }
    }

    @SuppressLint("ValidFragment")
    public static class CbSlide extends Fragment implements View.OnClickListener {
        private FoodTypeInfo typeInfo;
        private ImageView cbSlideClose;
        private List<FoodInfo> foodInfoList;
        private ListView foodlistView;
        public  CbSlide(FoodTypeInfo foodTypeInfo){
            typeInfo = foodTypeInfo;
            foodInfoList = foodTypeInfo.foodInfoList;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view;
            view = inflater.inflate(R.layout.cb_slide,container,false);
            return view;
        }

        @Override
        public void onResume() {
            initViews();
            super.onResume();
        }
        public void initViews(){
            TextView typeNameText = getActivity().findViewById(R.id.typeNameText);
            typeNameText.setText(typeInfo.foodTypeName);
            TextView typeDescText = getActivity().findViewById(R.id.typeDescText);
            typeDescText.setText(typeInfo.foodTypeDesc);
            cbSlideClose = getActivity().findViewById(R.id.cbSlideClose);
            cbSlideClose.setOnClickListener(this);
            foodlistView = getActivity().findViewById(R.id.foodlistView);
            if(foodInfoList.size()==0){
                TextView cbFoodListHint = getActivity().findViewById(R.id.cbFoodListHint);
                cbFoodListHint.setVisibility(View.VISIBLE);
            }else{
                CbFoodAdapter cbFoodAdapter = new CbFoodAdapter(getContext(),R.layout.cb_food_item,foodInfoList);
                foodlistView.setAdapter(cbFoodAdapter);
                foodlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FoodInfo foodInfo = foodInfoList.get(position);
                        Intent foodIntent = new Intent(getContext(),FoodDetailActivity.class);
                        foodIntent.putExtra("trFoodid",foodInfo.foodId);
                        foodIntent.putExtra("trFoodName",foodInfo.foodName);
                        getContext().startActivity(foodIntent);
                    }
                });
            }

        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.cbSlideClose:
                CookbookContent.cbSlideUp.hide();
            }
        }
    }

    class FoodTypeAdapter extends ArrayAdapter<FoodTypeInfo>{
        private int resourceId;
        public FoodTypeAdapter(@NonNull Context context,  int textViewResourceId, @NonNull List<FoodTypeInfo> objects) {
            super(context,  textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            FoodTypeInfo foodTypeInfo = getItem(position);
            ImageView coverImg = view.findViewById(R.id.typeCover);
            TextView typeName = view.findViewById(R.id.typeName);
            Picasso.get()
                    .load("http://192.168.1.101:5000/static/imgsUpload/chihuo.png")
                    .resize(150,150)
                    .config(Bitmap.Config.RGB_565)
                    .into(coverImg);
            typeName.setText(foodTypeInfo.foodTypeName);
            return view;
        }
    }

    /** 取得所有的菜系列表，其中每个菜系下还有一个菜品列表*/
    public List<FoodTypeInfo> getTypeList(){
        List<FoodTypeInfo> list;
        try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(commonInfo.httpUrl("getFoodTypeList"))
                            .build();
                    Response response = client.newCall(request).execute();
                    String resonseData = response.body().string();
                    Log.d("responseData",resonseData);
                    list = gson.fromJson(resonseData,new TypeToken<List<FoodTypeInfo>>(){}.getType());
                    for(FoodTypeInfo fti : list){
                        fti.foodInfoList = getFoodList(fti.foodTypeId);
                        commonInfo.foodTypeList.add(fti);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    list = new ArrayList<>();
                }
                return list;
        }


    /** 取得一个菜系下的所有菜品列表*/
    public List<FoodInfo> getFoodList(int foodTypeId){
        List<FoodInfo> foodlist = new ArrayList<>();
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(commonInfo.httpUrl("getFoodList"+foodTypeId))
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.foodSearchBtn:
                String searchFoodStr = searchFoodEditText.getText().toString();
                if(searchFoodStr.equals("")){
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误...")
                            .setContentText("你并没有输入任何内容!")
                            .show();
                }else{
                    Intent intent = new Intent(getActivity(),SearchFoodActivity.class);
                    intent.putExtra("foodSearchStr",searchFoodStr);
                    commonInfo.viewChangeStatus = 1;
                    startActivity(intent);
                }
                break;
        }
    }

}
