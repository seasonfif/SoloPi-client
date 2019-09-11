package com.sogou.solopiapp;

import android.app.Application;

import com.sogou.modulebus.routerbus.RouterBus;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouterBus.Builder builder = new RouterBus.Builder().enableDebug(true);
        RouterBus.getInstance().init(this, builder);

//        ARouter.init(this);
    }
}
