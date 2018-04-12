package com.example.qq12cvhj.chihuo;

/**
 * Created by qq12cvhj on 2018/4/12.
 */

public class UserAction {
    public String subjectName;
    public String actionType;
    public String objectName;
    public int subjectId;
    public int objectId;

    public UserAction(String subjectName, String actionType, String objectName, int subjectId, int objectId) {
        this.subjectName = subjectName;
        this.actionType = actionType;
        this.objectName = objectName;
        this.subjectId = subjectId;
        this.objectId = objectId;
    }
}
