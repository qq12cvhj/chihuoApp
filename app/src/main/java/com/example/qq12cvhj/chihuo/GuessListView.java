package com.example.qq12cvhj.chihuo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by qq12cvhj on 2018/5/6.
 */

class GuessListview extends ListView{


    public GuessListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
