package com.transmedika.transmedikakitui.base;

/**
 * Created by Widiyanto02 on 1/23/2018.
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
