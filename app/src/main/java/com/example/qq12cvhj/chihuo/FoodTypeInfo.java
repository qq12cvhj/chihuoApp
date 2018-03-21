package com.example.qq12cvhj.chihuo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq12cvhj on 2018/3/21.
 */

public class FoodTypeInfo {
    int typeId;
    String typeName;
    String description;
    List<FoodInfo> foodInfoList ;

    public FoodTypeInfo(int typeId, String typeName,String description) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
        this.foodInfoList = new ArrayList<>();
    }

}
