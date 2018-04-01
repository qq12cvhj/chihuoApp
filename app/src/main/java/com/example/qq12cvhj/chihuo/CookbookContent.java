package com.example.qq12cvhj.chihuo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.james.biuedittext.BiuEditText;

/**
 * Created by qq12cvhj on 2018/3/18.
 */

public class CookbookContent extends Fragment implements View.OnClickListener {
    public BiuEditText searchFoodEditText;
    public Button searchFoodBtn;
    private RecyclerView recyclerView;
    private RecyclerViewExpandableItemManager expMgr;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cookbook_content, container, false);
        recyclerView = view.findViewById(R.id.recycler_cookbook);
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
        recyclerView = getActivity().findViewById(R.id.recycler_cookbook);
        super.onStart();
    }

    @Override
    public void onResume() {
        commonInfo.getAndSetFoodInfo();

        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expMgr.createWrappedAdapter(new MyAdapter()));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        expMgr.attachRecyclerView(recyclerView);
        super.onResume();
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

    static abstract class MyBaseItem {
        public final long id;
        public final String text;

        public MyBaseItem(long id, String text) {
            this.id = id;
            this.text = text;
        }
    }

    static class FoodTypeItem extends MyBaseItem {
        public final List<FoodItem> foodItems; //一个菜系之下的菜品名称列表
        public  String foodTypeDescription; //一个菜系的介绍
        public FoodTypeItem(long id, String foodTypeName,String desc) {
            super(id, foodTypeName);
            foodItems = new ArrayList<>();
            foodTypeDescription = desc;
        }
    }

    static class FoodItem extends MyBaseItem {
        public String authorName;
        public FoodItem(long id, String foodName,String author) {
            super(id, foodName);
            authorName = author;
        }
    }

    static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        TextView nameView;
        public MyBaseViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
        }
    }
    //
    static class FoodTypeViewHolder extends MyBaseViewHolder {
        TextView descView;
        public FoodTypeViewHolder(View itemView) {
            super(itemView);
            descView = itemView.findViewById(R.id.foodtype_desc);
        }
    }

    static class FoodViewHolder extends MyBaseViewHolder {
        public FoodViewHolder(View itemView) {
            super(itemView);
        }
    }
    //这个地方要引入所有菜系以及菜系之下的菜品名称等
    public class MyAdapter extends AbstractExpandableItemAdapter<FoodTypeViewHolder, FoodViewHolder> {
        List<FoodTypeItem> mItems;
        public MyAdapter() {
            setHasStableIds(true); // this is required for expandable feature.

            mItems = new ArrayList<>();
            for (int i = 0; i < commonInfo.foodTypeList.size(); i++) {
                FoodTypeItem group = new FoodTypeItem(commonInfo.foodTypeList.get(i).typeId, commonInfo.foodTypeList.get(i).typeName,commonInfo.foodTypeList.get(i).description);
                for (int j = 0;j < commonInfo.foodTypeList.get(i).foodInfoList.size(); j++) {
                    group.foodItems.add(new FoodItem(commonInfo.foodTypeList.get(i).foodInfoList.get(j).foodId, commonInfo.foodTypeList.get(i).foodInfoList.get(j).foodName ,commonInfo.foodTypeList.get(i).foodInfoList.get(j).foodAuthor));
                }
                mItems.add(group);
            }
        }
        //这个不需要动
        @Override
        public int getGroupCount() {
            return mItems.size();
        }
        //这个不需要动
        @Override
        public int getChildCount(int groupPosition) {
            return mItems.get(groupPosition).foodItems.size();
        }
        //这个不需要动
        @Override
        public long getGroupId(int groupPosition) {
            // This method need to return unique value within all group items.
            return mItems.get(groupPosition).id;
        }
        //这个不需要动
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // This method need to return unique value within the group.
            return mItems.get(groupPosition).foodItems.get(childPosition).id;
        }

        //这个不需要动
        @Override
        public FoodTypeViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_foodtype_item_for_expandable_minimal, parent, false);
            FoodTypeViewHolder foodTypeViewHolder = new FoodTypeViewHolder(v);
            return foodTypeViewHolder;
        }
        //这个不需要动
        @Override
        public FoodViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_food_item_for_expandable_minimal, parent, false);
            FoodViewHolder foodViewHolder = new FoodViewHolder(v);
            return foodViewHolder;
        }
        //这个在定义完菜系之后进行相应的修改
        @Override
        public void onBindGroupViewHolder(FoodTypeViewHolder holder, int groupPosition, int viewType) {
            FoodTypeItem foodTypeItem = mItems.get(groupPosition);
            holder.nameView.setText(foodTypeItem.text);
            holder.descView.setText(foodTypeItem.foodTypeDescription);
            int colorWhite = Color.parseColor("#ffddffdd");
            int colorGray = Color.parseColor("#ffeeccff");
            if(groupPosition%2!=0){
                holder.nameView.setBackgroundColor(colorWhite);
                holder.descView.setBackgroundColor(colorWhite);
            }else{
                holder.nameView.setBackgroundColor(colorGray);
                holder.descView.setBackgroundColor(colorGray);
            }
        }
        //这个在定义完菜品之后进行相应的修改
        @Override
        public void onBindChildViewHolder(FoodViewHolder holder, final int groupPosition, final int childPosition, int viewType) {
            final FoodItem foodItem = mItems.get(groupPosition).foodItems.get(childPosition);
            holder.nameView.setText(foodItem.text);
            //设置每个条目的点击事件
            holder.nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int trFoodid = commonInfo.foodTypeList.get(groupPosition).foodInfoList.get(childPosition).foodId;
                    String trFoodName = commonInfo.foodTypeList.get(groupPosition).foodInfoList.get(childPosition).foodName;
                    Toast.makeText(v.getContext(),"你点击的Id为："+trFoodid,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),FoodDetailActivity.class);
                    intent.putExtra("trFoodid",trFoodid);
                    intent.putExtra("trFoodName",trFoodName);
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean onCheckCanExpandOrCollapseGroup(FoodTypeViewHolder holder, int groupPosition, int x, int y, boolean expand) {
            return true;
        }
    }
}
