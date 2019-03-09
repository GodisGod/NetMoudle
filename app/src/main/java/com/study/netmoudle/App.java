package com.study.netmoudle;

import android.app.Application;

import com.study.netmoudle.reponsitory.base.RepositoryProvider;

/**
 * Created by LHD on 2019/3/9.
 */
public class App extends Application {

    private RepositoryProvider provider = new RepositoryProvider();

    @Override
    public void onCreate() {
        super.onCreate();
        provider.addRepository();
    }
}
