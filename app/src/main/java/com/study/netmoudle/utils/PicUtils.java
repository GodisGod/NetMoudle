package com.study.netmoudle.utils;

import android.util.Log;

import com.study.netmoudle.bean.BlogBean;
import com.study.netmoudle.usecase.BlogUsecase;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LHD on 2019/3/10.
 */
public class PicUtils {

    BlogUsecase blogUsecase = new BlogUsecase();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    Observable<BlogBean> observable1 =  blogUsecase.getBlogJson();
    Observable<BlogBean> observable2 =  blogUsecase.getBlogJson();
    Observable<BlogBean> observable3 =  blogUsecase.getBlogJson();
    Observable<BlogBean> observable4 =  blogUsecase.getBlogJson();
    Observable<BlogBean> observable5 =  blogUsecase.getBlogJson();
    Observable<BlogBean> observable6 =  blogUsecase.getBlogJson();
    Observable<BlogBean> observable7 =  blogUsecase.getBlogJson();
    Observable<BlogBean> observable8 =  blogUsecase.getBlogJson();
    public PicUtils() {

    }

    public void getPicSigns1(){
        DisposableObserver<BlogBean> disposableObserver1 = new DisposableObserver<BlogBean>() {
            @Override
            public void onNext(BlogBean blogBean) {
                Log.i("LHD","concat请求成功  = "+blogBean.getName()+"    "+blogBean.getEmail());
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LHD","concat请求失败 =  "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        DisposableObserver<BlogBean> disposableObserver2 = new DisposableObserver<BlogBean>() {
            @Override
            public void onNext(BlogBean blogBean) {
                Log.i("LHD","concat111请求成功  = "+blogBean.getName()+"    "+blogBean.getEmail());
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LHD","concat111请求失败 =  "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(Observable.concat(observable1,observable2,observable3,observable4)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(disposableObserver1));
        compositeDisposable.add(Observable.concat(observable5,observable6,observable7,observable8)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver2));
        //disposableObserver 不能被subscribeWith多次
        //io.reactivex.exceptions.ProtocolViolationException: It is not allowed to subscribe with a(n) com.study.netmoudle.utils.PicUtils$1 multiple times. Please create a fresh instance of com.study.netmoudle.utils.PicUtils$1 and subscribe that to the target source instead.
    }
    public void getPicSigns2(){
        Observable<BlogBean> observableBlog1 =   Observable.concat(observable1,observable2,observable3,observable4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observable<BlogBean> observableBlog2 =    Observable.concat(observable5,observable6,observable7,observable8)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

      Disposable disposable =  Observable.concat(observableBlog1,observableBlog2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BlogBean>() {
                    @Override
                    public void accept(BlogBean blogBean) throws Exception {
                        Log.i("LHD","getPicSigns2 = "+blogBean.getName()+"  "+blogBean.getEmail());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("LHD","getPicSigns2 = "+throwable.getMessage());
                    }
                });
    }

}
