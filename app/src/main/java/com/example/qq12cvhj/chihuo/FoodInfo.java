package com.example.qq12cvhj.chihuo;

/**
 * Created by qq12cvhj on 2018/3/21.
 */

public class FoodInfo {
    int foodId;
    String foodAuthor; //菜品的作者
    String foodName; //菜品名称
    String foodImgSrc; //菜品封面图片地址
    int starCount; //菜品的收藏人数
    int foodAuthorId; //菜品作者的ID

    public FoodInfo(int foodId, String foodAuthor, String foodName) {
        this.foodId = foodId;
        this.foodAuthor = foodAuthor;
        this.foodName = foodName;
    }
}
