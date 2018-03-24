package com.example.qq12cvhj.chihuo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qq12cvhj on 2018/3/21.
 */

public class FoodInfo {
    int foodId;
    String foodAuthor; //菜品的作者
    String foodName; //菜品名称
    String foodTypeid; //所属菜系的id.
    String FoodAuthorId;
    String foodDesc; //菜品简介
    int starCount; //点赞个数
    //以下两项通过解包json、打包json进行操作
    Map foodMaterial = new HashMap(); //食材一栏，以listView 实现
    Map cookStep = new HashMap();//制作步骤一栏，以listView 实现


    public FoodInfo(int foodId, String foodAuthor, String foodName) {
        this.foodId = foodId;
        this.foodAuthor = foodAuthor;
        this.foodName = foodName;
    }
}
