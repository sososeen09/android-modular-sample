package com.sososeen09.modular.api;

import com.sososeen09.modular.annotation.model.RouteMeta;
import com.sososeen09.modular.api.template.IRouteGroup;
import com.sososeen09.modular.api.template.IService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunlong on 2018/3/24.
 */

public class WareHouse {

    // root 映射表 保存分组信息
    static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<String, RouteMeta> routes = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<Class, IService> services = new HashMap<>();
    // TestServiceImpl.class , TestServiceImpl 没有再反射

}
