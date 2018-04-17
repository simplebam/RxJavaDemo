package com.yueyue.rxjavademo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * author : yueyue on 2018/4/14 22:30
 * desc   :
 */
public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext=this;
    }

    public static Context getContext() {
        return sContext;
    }
}
