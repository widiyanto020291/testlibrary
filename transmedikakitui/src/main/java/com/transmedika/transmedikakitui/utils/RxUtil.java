package com.transmedika.transmedikakitui.utils;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Widiyanto02 on 1/23/2018.
 */

public class RxUtil {
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {
        return new FlowableTransformer<T, T>() {
            @NotNull
            @Override
            public Flowable<T> apply(@NotNull Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }


        };
    }

    public static <T> FlowableTransformer<T, T> rxSchedulerHelperR() {
        return new FlowableTransformer<T, T>() {
            @NotNull
            @Override
            public Flowable<T> apply(@NotNull Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }


        };
    }

    /**
     * Apply scheduler:
     * observable
     * .subscribeOn(Schedulers.computation())
     * .observeOn(AndroidSchedulers.mainThread());
     */
    public static <T> FlowableTransformer<T, T> applyLogicSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Apply scheduler io for call api
     */
    public static <T> FlowableTransformer<T, T> applyIoSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Apply scheduler with newThread:
     * observable
     * .subscribeOn(Schedulers.newThread())
     * .observeOn(AndroidSchedulers.mainThread());
     */
    public static <T> FlowableTransformer<T, T> applyNewThreadSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * onNext with subscriber and item
     */
    public static <T> void onNext(Subscriber<T> subscriber, T item) {
        subscriber.onNext(item);
    }

    /**
     * onError with subscriber and exception
     */
    public static void onError(Subscriber<?> subscriber, Exception e) {
        subscriber.onError(e);
    }

    /**
     * onCompleted with subscriber
     */
    public static void onCompleted(Subscriber<?> subscriber) {
        subscriber.onComplete();
    }

    /**
     * onStop with Disposable
     */
    public static void onStop(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    /**
     * Run method in aSync mode
     *
     * @return Disposable
     */
    public static <T> Disposable aSyncTask(FlowableOnSubscribe<T> onSubscribe) {
        return aSyncTask(onSubscribe, null);
    }

    /**
     * Run method in aSync mode
     *
     * @return Disposable
     */
    public static <T> Disposable aSyncTask(FlowableOnSubscribe<T> onSubscribe,
                                           Consumer<? super T> onNext) {
        return aSyncTask(onSubscribe, onNext, null);
    }

    /**
     * Run method in aSync mode
     */
    public static <T> Disposable aSyncTask(FlowableOnSubscribe<T> onSubscribe,
                                           Consumer<? super T> onNext,
                                           Consumer<Throwable> onError) {
        return aSyncTask(onSubscribe, onNext, onError, null);
    }

    /**
     * Run method in aSync mode
     */
    public static <T> Disposable aSyncTask(FlowableOnSubscribe<T> onSubscribe,
                                           Consumer<? super T> onNext,
                                           Consumer<Throwable> onError,
                                           Action onCompleted) {
        return safeSubscribe(Flowable.create(onSubscribe, BackpressureStrategy.BUFFER).compose(RxUtil.applyLogicSchedulers()),
                onNext, onError, onCompleted);
    }

    /**
     * Run method in new thread with async mode
     */
    public static <T> Disposable aSyncTaskNewThread(FlowableOnSubscribe<T> onSubscribe) {
        return aSyncTaskNewThread(onSubscribe, null);
    }

    /**
     * Run method in new thread with async mode
     */
    public static <T> Disposable aSyncTaskNewThread(FlowableOnSubscribe<T> onSubscribe,
                                                    Consumer<? super T> onNext) {
        return aSyncTaskNewThread(onSubscribe, onNext, null, null);
    }

    /**
     * Run method in new thread with async mode
     */
    public static <T> Disposable aSyncTaskNewThread(FlowableOnSubscribe<T> onSubscribe,
                                                    Consumer<? super T> onNext,
                                                    Consumer<Throwable> onError) {
        return aSyncTaskNewThread(onSubscribe, onNext, onError, null);
    }

    /**
     * Run method in new thread with async mode
     */
    public static <T> Disposable aSyncTaskNewThread(FlowableOnSubscribe<T> onSubscribe,
                                                    Consumer<? super T> onNext,
                                                    Consumer<Throwable> onError,
                                                    Action onCompleted) {
        return safeSubscribe(Flowable.create(onSubscribe, BackpressureStrategy.BUFFER).compose(RxUtil.applyNewThreadSchedulers()),
                onNext, onError, onCompleted);
    }

    /**
     * Run method on Ui
     *
     * @return Disposable
     */
    public static Disposable runOnUi(Consumer<? super Object> onNext) {
        return safeSubscribe(Flowable
                        .create(subscriber -> subscriber.onNext(true),BackpressureStrategy.BUFFER)
                        .compose(RxUtil.applyLogicSchedulers()),
                onNext);
    }

    /**
     * Run method with sync mode
     *
     * @return Disposable
     */
    public static <T> Disposable syncTask(FlowableOnSubscribe<T> onSubscribe) {
        return safeSubscribe(Flowable.create(onSubscribe, BackpressureStrategy.BUFFER));
    }

    /**
     * Safe Subscribe
     */
    public static <T> Disposable safeSubscribe(Flowable<T> observable) {
        return safeSubscribe(observable, null);
    }

    /**
     * Safe Subscribe
     */
    public static <T> Disposable safeSubscribe(Flowable<T> observable,
                                               Consumer<? super T> onNext) {
        return safeSubscribe(observable, onNext, null);
    }

    /**
     * Safe Subscribe
     */
    public static <T> Disposable safeSubscribe(Flowable<T> observable,
                                               Consumer<? super T> onNext,
                                               Consumer<Throwable> onError) {
        return safeSubscribe(observable, onNext, onError, null);
    }

    /**
     * Safe Subscribe
     */
    public static <T> Disposable safeSubscribe(Flowable<T> observable,
                                               Consumer<? super T> onNext,
                                               Consumer<Throwable> onError,
                                               Action onCompleted) {
        if (onNext == null) onNext = t -> {
        };
        if (onError == null) onError = throwable -> {
        };
        if (onCompleted == null) onCompleted = () -> {
        };

        return observable.subscribe(onNext, onError, onCompleted);
    }

}
