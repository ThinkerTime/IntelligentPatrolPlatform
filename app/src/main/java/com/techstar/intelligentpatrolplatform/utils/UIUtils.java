package com.techstar.intelligentpatrolplatform.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.techstar.intelligentpatrolplatform.application.BaseApplication;


public class UIUtils {
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    public static int getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    public static Thread getMainThread() {
        return BaseApplication.getMainThread();
    }

    public static Handler getHandler() {
        return BaseApplication.getHandler();
    }

    //string
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    //drawable
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    //stringArray
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    //dip--->px
    public static int dip2px(int dip, Context context) {
        //dp和px的转换关系比例值
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    //px---->dp
    public static int px2dip(int px, Context context) {
        //dp和px的转换关系比例值
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    //判断是否是主线的方法
    public static boolean isRunInMainThread() {
        return getMainThreadId() == android.os.Process.myTid();
    }

    //保证当前的UI操作在主线程里面运行
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            //如果现在就是在主线程，就直接运行run方法
            runnable.run();
        } else {
            //否则将其传递到主线程中运行
            getHandler().post(runnable);
        }
    }

    //java代码区设置颜色选择器的方法
    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        return getContext().getResources().getColorStateList(mTabTextColorResId);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static int getDimens(int id) {
        //根据dimens中提供的id，将其对应的dp值转换成相应的像素值大小
        return UIUtils.getContext().getResources().getDimensionPixelSize(id);
    }

    public static void postDelayed(Runnable runnable, long delayTime) {
        getHandler().postDelayed(runnable, delayTime);
    }

    public static void removeCallback(Runnable runnable) {
        //移除在当前handler中维护的任务(传递进来的任务)
        getHandler().removeCallbacks(runnable);
    }

    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }
}
