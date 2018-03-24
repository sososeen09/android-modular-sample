package com.sososeen09.modular.api.callback;

import com.sososeen09.modular.api.Postcard;

/**
 * Created by yunlong on 2018/3/24.
 */

public interface NavigationCallback {
    /**
     * 找到跳转页面
     *
     * @param postcard meta
     */
    void onFound(Postcard postcard);

    /**
     * 未找到
     *
     * @param postcard meta
     */
    void onLost(Postcard postcard);

    /**
     * 成功跳转
     *
     * @param postcard meta
     */
    void onArrival(Postcard postcard);
}
