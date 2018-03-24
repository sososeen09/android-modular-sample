package com.sososeen09.modular.api;

import android.app.Activity;
import android.util.LruCache;

import com.sososeen09.modular.api.template.IExtra;

/**
 * Created by yunlong on 2018/3/24.
 */

class ExtraManager {

    public static final String SUFFIX_EXTRA = "$$Extra";
    private static ExtraManager instance;
    private LruCache<String, IExtra> classCache;

    public static ExtraManager getInstance() {
        if (instance == null) {
            synchronized (EasyRouter.class) {
                if (instance == null) {
                    instance = new ExtraManager();
                }
            }
        }
        return instance;
    }

    private ExtraManager() {
        classCache = new LruCache<>(66);
    }

    /**
     * 加载数据到目标类中
     *
     * @param target
     */
    public void loadExtras(Activity target) {
        String className = target.getClass().getName();
        IExtra iExtra = classCache.get(className);

        try {
            if (iExtra == null) {
                iExtra = (IExtra) Class.forName(className + SUFFIX_EXTRA).newInstance();
                classCache.put(className, iExtra);
            }

            iExtra.loadExtra(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
