package com.example.qq12cvhj.chihuo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq12cvhj on 2018/3/21.
 */

public  class commonInfo {
    public static String httpUrl(String url) {
        return "http://192.168.1.101:5000/"+url;
    }
    public static int currentUserId;
    public static User currentUser;
    public static int viewChangeStatus = 0;
    public static boolean loginStatus = false;
    public static String wechatAppId = "wx088cb04b9063e415";
    public static String wechatSecret = "2edf642ce10dc3bee3a7c8a10d91a07b";
    /** 说明一下这里使用全局变量的原因，主要是在菜谱一栏的点击事件中能快速定位到所点击的对象
     * 由于程序的UI使用的第三方UI，水平有限，API没读太懂，所以暂时使用全局变量实现。以下的getAndSetFoodInfo
     * 用来模拟，实际使用中主要通过网络，从数据库服务器中获取内容。
     * */
    public static List<FoodTypeInfo> foodTypeList = new ArrayList<>();
    /** 下面是使用数组和列表对菜品进行模拟的代码，现在已弃用*/
    /*public static void getAndSetFoodInfo(){
        foodTypeList = new ArrayList<>();
        for(int i = 0;i < 30;i++){
            commonInfo.foodTypeList.add( new FoodTypeInfo(i,"菜系"+(i),"没有描述"));
            for (int j = 0;j<5;j++){
                commonInfo.foodTypeList.get(i).foodInfoList.add(new FoodInfo((i*5+j),"qq12cvhj","菜品"+String.valueOf(i*5+j)));
            }
        }
    }*/

}
