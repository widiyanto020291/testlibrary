package com.transmedika.transmedikakitui.modul.pin;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({PinTask.CREATE_NEW,
        PinTask.CHANGE,
        PinTask.FORGOT,
        PinTask.CONFIRM
})
public @interface PinTask {
    String CREATE_NEW = "CREATE_NEW";
    String CHANGE = "CHANGE";
    String FORGOT = "FORGOT";
    String CONFIRM = "CONFIRM";
}
