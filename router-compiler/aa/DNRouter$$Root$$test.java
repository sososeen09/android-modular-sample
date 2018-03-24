package com.sososeen09.modular.compiler;

import com.dongnao.router.core.template.IRouteGroup;
import com.dongnao.router.core.template.IRouteRoot;

import java.util.Map;

public class DNRouter$$Root$$test implements IRouteRoot {
  @Override
  public void loadInto(Map<String, Class<? extends IRouteGroup>> routes) {
    routes.put("a", DNRouter$$Group$$a.class);
    routes.put("c", DNRouter$$Group$$c.class);
    routes.put("d", DNRouter$$Group$$d.class);
  }
}
