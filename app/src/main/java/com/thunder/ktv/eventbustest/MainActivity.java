package com.thunder.ktv.eventbustest;

import android.icu.text.SimpleDateFormat;
import android.icu.text.TimeZoneFormat;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
//        eventBus.register(MySubscribe.getInstance());
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

    @Subscribe(threadMode=ThreadMode.POSTING)
    public void onEventMessage1(String event)
    {
        Log.d(TAG, "onEventMessage1: " + event +
                " thread is " + Thread.currentThread().getName());
    }

    /**
     * 删除黏性事件
     */
    public void delStickyEvent()
    {
        //1.删除bus上的所有黏性事件
        eventBus.removeAllStickyEvents();

        //2.删除bus上的某种类型的黏性事件
        eventBus.removeStickyEvent(String.class);

        //主动获取黏性事件
        String stickyEvent = eventBus.getStickyEvent(String.class);
        if(stickyEvent != null){
            //3.删除bus上指定的某个黏性事件
            eventBus.removeStickyEvent(stickyEvent);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:{
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

                eventBus.post("my string massage time is " + System.currentTimeMillis());
                /**
                 * 发送一个粘性事件
                 */
                eventBus.postSticky("my string massage time is " + System.currentTimeMillis());
                break;
            }
            case R.id.button2:{
                /**
                 *  黏性事件会在注册时发送缓冲里的最近的一个事件
                 *  eventBus.postSticky("event 2");
                 *  eventBus.postSticky("event 3");
                 *  eventBus.postSticky("event 4");
                 *  eventBus.register(MySubscribe.getInstance());
                 *  这时只会接受到"event 4"以及后面的事件
                 *  eventBus.postSticky("event 5");
                 *  eventBus.postSticky("event 6");
                 *  "event 5" "event 6"也会接受到
                 *
                 *  黏性事件类似于Rxjava 的 BehaviorSubject
                 *  普通事件类似于Rxjava 的 PublishSubject
                 */

                /**
                 * Rxjava 的 Subject
                 * 发射行为
                 *
                 * AsyncSubject
                 * 不论订阅发生在什么时候，只会发射最后一个数据
                 * BehaviorSubject
                 * 发送订阅之前一个数据和订阅之后的全部数据
                 * ReplaySubject
                 * 不论订阅发生在什么时候，都发射全部数据
                 * PublishSubject
                 * 发送订阅之后全部数据
                 */

                eventBus.register(MySubscribe.getInstance());
                break;
            }
        }
    }
}
