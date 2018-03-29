package com.example.qq12cvhj.chihuo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq12cvhj on 2018/3/21.
 */

public  class commonInfo {
    public static String httpUrl(String url) {
        return "http://192.168.1.110:5000/"+url;
    }
    public static int currentUserId;
    public static int viewChangeStatus = 0;
    public static boolean loginStatus = false;
    /** 说明一下这里使用全局变量的原因，主要是在菜谱一栏的点击事件中能快速定位到所点击的对象
     * 由于程序的UI使用的第三方UI，水平有限，API没读太懂，所以暂时使用全局变量实现。以下的getAndSetFoodInfo
     * 用来模拟，实际使用中主要通过网络，从数据库服务器中获取内容。
     * */
    public static List<FoodTypeInfo> foodTypeList = new ArrayList<>();
    public static void getAndSetFoodInfo(){
        foodTypeList = new ArrayList<>();
        for(int i = 0;i < 30;i++){
            commonInfo.foodTypeList.add( new FoodTypeInfo(i,"菜系"+(i),"没有描述"));
            for (int j = 0;j<5;j++){
                commonInfo.foodTypeList.get(i).foodInfoList.add(new FoodInfo((i*5+j),"qq12cvhj","菜品"+String.valueOf(i*5+j)));
            }
        }
    }

}
