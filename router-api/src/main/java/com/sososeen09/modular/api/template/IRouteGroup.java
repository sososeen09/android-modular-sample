package com.sososeen09.modular.api.template;

import com.sososeen09.modular.annotation.model.RouteMeta;

import java.util.Map;

/**
 * Created by yunlong on 2018/3/24.
 */

public interface IRouteGroup {
    /**
     * 加载单个分组的路由表信息
     *
     * @param atlas 键是路径、值表示路径所包含的结点信息
     */
    void loadInto(Map<String, RouteMeta> atlas);
}
