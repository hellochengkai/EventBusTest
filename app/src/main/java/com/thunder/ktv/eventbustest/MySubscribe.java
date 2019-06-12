package com.thunder.ktv.eventbustest;

import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MySubscribe {
    private static MySubscribe subscribe = new MySubscribe();
    public static MySubscribe getInstance() {
        return subscribe;
    }

    private static final String TAG = "MySubscribe";
    @Subscribe(threadMode= ThreadMode.POSTING)
    public void onEventMessage(MessageEvent event)
    {
        Log.d(TAG, "onEventMessage: " + event.getMsg() +
                " thread is " + Thread.currentThread().getName());
    }

    /**
     * 接受粘性事件
     */
    @Subscribe(sticky =  true,threadMode= ThreadMode.POSTING)
    public void onEventMessage1(String event) throws Throwable {
        Log.d(TAG, "onEventMessage1: " + event +
                " thread is " + Thread.currentThread().getName());
//        throw new Throwable("hahah");
    }
}
