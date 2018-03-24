package com.sososeen09.modular.api.thead;

import android.support.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yunlong on 2018/3/24.
 */

public class DefaultPoolExecutor {
    //核心线程和最大线程都是cpu核心数+1
    private static final int MAX_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    private static long SURPLUS_THREAD_LIFE = 30;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mAtomicInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "EasyRouter #" + mAtomicInteger.incrementAndGet());
        }
    };

    public static ThreadPoolExecutor newDefaultPoolExecutor(int corePoolSize) {
        corePoolSize = Math.min(corePoolSize, MAX_CORE_POOL_SIZE);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                corePoolSize,
                SURPLUS_THREAD_LIFE,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(50),
                sThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
