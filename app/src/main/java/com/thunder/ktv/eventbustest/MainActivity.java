package com.thunder.ktv.eventbustest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private EventBus eventBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        eventBus = EventBus.getDefault();
        eventBus = EventBus.
                builder()
                /**
                 * EventBus默认不会向上抛出Subscriber发生的异常，
                 * 会被EventBus catch住。此项默认是false。
                 * 设置为true会向上继续抛出Subscriber生成的异常
                 */
//                .throwSubscriberException(true)
                /**
                 * installDefaultEventBus 会通过builder的链式配置创建一个EventBus
                 * 并且会替换掉默认的 EventBus。这个调用必须在getDefault()之前调用,否则会产生异常
                 */
                .installDefaultEventBus();
//                .build();
        eventBus.register(this);
        eventBus.register(MySubscribe.getInstance());
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        eventBus.unregister(MySubscribe.getInstance());
        super.onDestroy();
    }

    @Subscribe(threadMode=ThreadMode.POSTING)
    public void onEventMessage(MessageEvent event)
    {
        Log.d(TAG, "onEventMessage: " + event.getMsg() +
                " thread is " + Thread.currentThread().getName());
    }

    @Override
    public void onClick(View v) {

        /**
         * 不同的event事件只会发送到event类型相匹配的 Subscribe 回调函数去
         */
        new Thread("myThread"){
            @Override
            public void run() {
                super.run();
                eventBus.post(new MessageEvent("my massage"));
            }
        }.start();
        eventBus.post("my string massage");
    }
}
