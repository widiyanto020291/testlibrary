package com.transmedika.transmedikakitui.modul.password;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({PasswordTask.CREATE_NEW,
        PasswordTask.CHANGE,
        PasswordTask.FORGOT,
        PasswordTask.CONFIRM
})
public @interface PasswordTask {
    String CREATE_NEW = "CREATE_NEW";
    String CHANGE = "CHANGE";
    String FORGOT = "FORGOT";
    String CONFIRM = "CONFIRM";
}
