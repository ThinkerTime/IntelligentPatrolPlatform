package com.techstar.intelligentpatrolplatform.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.techstar.intelligentpatrolplatform.utils.LogUtils;

public class BaseApplication extends Application {
    private static Context context;
    private static int mainThreadId;
    private static Thread mainThread;
    private static Handler handler;
    private static final int maxDiskCacheSize = 32 * 1024 * 1024;
    private static final int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
    private static final String IMAGE_CACHE_PATH = "lanjiejie/Cache";
    private int appCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //Context
        context = getApplicationContext();
        //mainThreadId
        mainThreadId = android.os.Process.myTid();
        //Thread-->object
        mainThread = Thread.currentThread();
        //Handler
        handler = new Handler();
//        initImageLoader(context);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.e("sbaseapplication..onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.e("sbaseapplication..onActivityStarted");
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
                LogUtils.d("appCount:"+appCount);
                LogUtils.e("sbaseapplication..onActivityStopped");

                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });


    }



    public static Context getContext() {
        return context;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Thread getMainThread() {
        return mainThread;
    }

    public static Handler getHandler() {
        return handler;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }

}
