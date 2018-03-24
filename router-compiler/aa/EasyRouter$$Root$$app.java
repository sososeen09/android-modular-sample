package com.sososeen09.easy.routes;

import com.sososeen09.modular.api.template.IRouteGroup;
import com.sososeen09.modular.api.template.IRouteRoot;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

public class EasyRouter$$Root$$app implements IRouteRoot {
  @Override
  public void loadInto(Map<String, Class<? extends IRouteGroup>> routes) {
    routes.put("main",EasyRouter$$Group$$main.class);
  }
}
