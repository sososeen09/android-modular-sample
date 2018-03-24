package com.sososeen09.modular.api.template;

import java.util.Map;

/**
 * Created by yunlong on 2018/3/24.
 * 存放路由组信息
 */

public interface IRouteRoot {

    /**
     * 把路由组的信息加载进来，键表示组名、值是分组
     *
     * @param routes
     */
    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);
}
