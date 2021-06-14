package com.transmedika.transmedikakitui.widget.dynamicview;

import android.content.Context;

import com.transmedika.transmedikakitui.models.bean.JawabanMultiple;


public abstract class MultipleValueWidget<T> extends BaseWidget<T, JawabanMultiple> {
    public MultipleValueWidget(Context context) {
        super(context, JawabanMultiple.class);
    }
}
