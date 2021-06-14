package com.transmedika.transmedikakitui.base;

/**
 * Created by Widiyanto02 on 1/23/2018.
 */

public interface BaseView {
    void showErrorMsg(String msg);
    void showErrorMsg(String msg, boolean usingDialog);
    void gotoPagePerbaikan();
    void showLoading(boolean usingDialog);
    void showLoading();
    void hideLoading(boolean usingDialog);
    void hideLoading();
    void sessionExpired();
    void onDataIsEmpty();
}
