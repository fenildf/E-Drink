package cn.droidlover.xdroid.kit;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * android退出程序的工具类，使用单例模式
 * 1.在Activity的onCreate()的方法中调用addActivity()方法添加到mActivityList
 * 2.可以在Activity的onDestroy()的方法中调用delActivity()来删除已经销毁的Activity实例
 * 这样避免了mActivityList容器中有多余的实例而影响程序退出速度
 */
public class ExitApp {

    /**
     * 转载Activity的容器
     */
    private List<Activity> mActivityList = new LinkedList<Activity>();
    private static ExitApp instance = new ExitApp();


    private ExitApp() {
    }


    /**
     * 获取ExitAppUtils的实例，保证只有一个ExitAppUtils实例存在
     *
     * @return
     */
    public static ExitApp getInstance() {
        return instance;
    }


    /**
     * 添加Activity实例到mActivityList中，在onCreate()中调用
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    /**
     * 从容器中删除多余的Activity实例，在onDestroy()中调用
     *
     * @param activity
     */
    public void delActivity(Activity activity) {
        mActivityList.remove(activity);
    }


    /**
     * 退出程序的方法
     */
    public void exit() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }

//        System.exit(0);
    }
}
