package com.example.qq12cvhj.chihuo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq12cvhj on 2018/3/21.
 */

public class FoodTypeInfo {
    int foodTypeId;
    String foodTypeName;
    String foodTypeDesc;
    String coverImg;
    List<FoodInfo> foodInfoList ;

    public FoodTypeInfo(int typeId, String typeName,String description) {
        this.foodTypeId = typeId;
        this.foodTypeName = typeName;
        this.foodTypeDesc = description;
        this.foodInfoList = new ArrayList<>();
    }

}
