package xx.utils;


import xx.ConstDef;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/7/31.
 */
public class ThreadPoolManager {

    private ThreadPoolManager() {

    }

    /**
     * 默认超时时间3秒
     */
    private static final int DEFAULT_TIMEOUT = 3000;

    /**
     * 最大业务处理线程200
     */
    private static final int MAX_THREAD_SIZE = 5;

    /**
     * ACK线程数
     */
    private static final int ACK_THREAD_SIZE = 1;

    static private class SignaltenHolder {
        private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
        return SignaltenHolder.threadPoolManager;
    }

    /**
     * 业务线程
     */
    private ExecutorService service = Executors.newCachedThreadPool();//new ThreadPoolExecutor(0, MAX_THREAD_SIZE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new CustomThreadFactory(ConstDef.BUSINESS_THREAD_NAME));

    /**
     * ack线程
     */
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(ACK_THREAD_SIZE, new CustomThreadFactory(ConstDef.ACK_THREAD_NAME));

    /**
     * exectThread
     *
     * @param runnable
     */
    public void exectThread(Runnable runnable) {
        service.execute(runnable);
    }

    /**
     * execSchedule
     *
     * @param runnable
     * @param timeout
     * @return
     */
    public Future<?> execSchedule(Runnable runnable, int timeout) {
        return scheduledExecutor.schedule(runnable,
                timeout > 0 ? timeout : DEFAULT_TIMEOUT,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * 设置线程池名称
     */
    class CustomThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CustomThreadFactory(String threadName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = threadName + ":";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

        ThreadPoolExecutor test = new ThreadPoolExecutor(0, MAX_THREAD_SIZE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new CustomThreadFactory(ConstDef.BUSINESS_THREAD_NAME));

    }

    public static void main(String []  args){


        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始");
        for(int i=0;i < 100;i++){
            ThreadPoolManager.getInstance().exectThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName());
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
