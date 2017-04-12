package com.you.mm.util;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2017/4/6.
 */

public class RxBus
{
    private static volatile RxBus mInstance;
    private Subject<Object> mSubject;
    private HashMap<String, CompositeDisposable> mDisposableMap;

    private RxBus()
    {
        mSubject = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance()
    {
        if (mInstance == null)
        {
            synchronized (RxBus.class)
            {
                if (mInstance == null)
                    mInstance = new RxBus();
            }
        }

        return mInstance;
    }

    /**
     * 发送事件
     *
     * @param o
     */
    public void post(Object o)
    {
        mSubject.onNext(o);
    }

    /**
     * 返回指定类型的Observable实例
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final Class<T> type)
    {
        return mSubject.ofType(type);
    }

    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public boolean hasObservers()
    {
        return mSubject.hasObservers();
    }

    /**
     * 一个默认的订阅方法
     *
     * @param type
     * @param onNext
     * @param onError
     * @param <T>
     * @return
     */
    public <T> Disposable doSubscribe(Class<T> type, Consumer<? super T> onNext, Consumer<? super Throwable> onError)
    {
        return toObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    /**
     * 保存订阅后的subscription
     *
     * @param o
     * @param disposable
     */
    public void addSubscription(Object o, Disposable disposable)
    {
        if (mDisposableMap == null)
            mDisposableMap = new HashMap<>();

        String key = o.getClass().getName();
        if (mDisposableMap.get(key) != null)
        {
            mDisposableMap.get(key).add(disposable);
        }
        else
        {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
            mDisposableMap.put(key, compositeDisposable);
        }
    }

    /**
     * 取消订阅
     * @param o
     */
    public void unSubscribe(Object o)
    {
        if (mDisposableMap == null)
            return;

        String key = o.getClass().getName();
        if (! mDisposableMap.containsKey(key))
            return;

        if (mDisposableMap.get(key) != null)
            mDisposableMap.get(key).dispose();

        mDisposableMap.remove(key);
    }


}
