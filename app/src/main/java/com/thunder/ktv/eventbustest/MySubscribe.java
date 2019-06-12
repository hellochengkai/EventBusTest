package com.thunder.ktv.eventbustest;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * ThreadMode.POSTING
 * 订阅者方法将在发布事件所在的线程中被调用。这是 默认的线程模式。
 * 事件的传递是同步的，一旦发布事件，所有该模式的订阅者方法都将被调用。
 * 这种线程模式意味着最少的性能开销，因为它避免了线程的切换。
 * 因此，对于不要求是主线程并且耗时很短的简单任务推荐使用该模式。
 * 使用该模式的订阅者方法应该快速返回，以避免阻塞发布事件的线程，这可能是主线程。
 *
 * ThreadMode.MAIN
 * 订阅者方法将在主线程（UI线程）中被调用。因此，可以在该模式的订阅者方法中直接更新UI界面。
 * 如果发布事件的线程是主线程，那么该模式的订阅者方法将被直接调用。
 * 使用该模式的订阅者方法必须快速返回，以避免阻塞主线程。
 *
 * ThreadMode.MAIN_ORDERED
 * 订阅者方法将在主线程（UI线程）中被调用。因此，可以在该模式的订阅者方法中直接更新UI界面。
 * 事件将先进入队列然后才发送给订阅者，所以发布事件的调用将立即返回。这使得事件的处理保持严格的串行顺序。
 * 使用该模式的订阅者方法必须快速返回，以避免阻塞主线程。
 *
 * ThreadMode.BACKGROUND
 * 订阅者方法将在后台线程中被调用。如果发布事件的线程不是主线程，那么订阅者方法将直接在该线程中被调用。
 * 如果发布事件的线程是主线程，那么将使用一个单独的后台线程，该线程将按顺序发送所有的事件。
 * 使用该模式的订阅者方法应该快速返回，以避免阻塞后台线程。
 *
 * ThreadMode.ASYNC
 * 订阅者方法将在一个单独的线程中被调用。因此，发布事件的调用将立即返回。
 * 如果订阅者方法的执行需要一些时间，例如网络访问，那么就应该使用该模式。
 * 避免触发大量的长时间运行的订阅者方法，以限制并发线程的数量。
 * EventBus使用了一个线程池来有效地重用已经完成调用订阅者方法的线程。
 *
 */
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

    /**
     * 在Subscribe注解中总共有3个参数，上面我们用到了其中的两个，
     * 这里我们使用以下第三个参数，即priority。
     * 它用来指定订阅方法的优先级，是一个整数类型的值，默认是0，值越大表示优先级越大。
     * 在某个事件被发布出来的时候，优先级较高的订阅方法会首先接受到事件。
     * 与线程有关，只有在相同的线程中是事件具有优先级区别，不同的线程之间不存在优先级限制
     */
    @Subscribe(priority = 2,threadMode= ThreadMode.POSTING)
    public void onEventMessage2(String event) throws Throwable {
        Log.d(TAG, "onEventMessage2: " + event +
                " thread is " + Thread.currentThread().getName());
    }

    @Subscribe(priority = 3,threadMode= ThreadMode.POSTING)
    public void onEventMessage3(String event) throws Throwable {
        Log.d(TAG, "onEventMessage3: " + event +
                " thread is " + Thread.currentThread().getName());
    }

    @Subscribe(priority = 4,threadMode= ThreadMode.POSTING)
    public void onEventMessage4(String event) throws Throwable {
        Log.d(TAG, "onEventMessage4: " + event +
                " thread is " + Thread.currentThread().getName());

        //终止事件的继续发送
        EventBus.getDefault().cancelEventDelivery(event);
    }
}
