package com.transmedika.transmedikakitui.base;

import com.transmedika.transmedikakitui.component.RxBus;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.realm.Realm;

/**
 * Created by Widiyanto02 on 1/23/2018.
 */

public class RxPresenter <T extends BaseView> implements BasePresenter<T> {
    protected T mView;
    protected CompositeDisposable mCompositeDisposable;
    protected Realm realm;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    public void remove(Disposable subscription) {
        if (subscription!=null) {
            mCompositeDisposable.remove(subscription);
        }
    }

    protected CompositeDisposable compositeDisposable(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
        return mCompositeDisposable;
    }

    protected <U> void addRxBusSubscribe(Class<U> eventType, Consumer<U> act) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(RxBus.getDefault().toDefaultFlowable(eventType, act));
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
        if(realm!=null)
            realm.close();
    }
}
