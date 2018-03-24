package com.sososeen09.modular.compiler;

import com.dongnao.alvin.test.MainActivity;
import com.dongnao.router.annotation.model.RouteMeta;
import com.dongnao.router.core.template.IRouteGroup;

import java.util.Map;

public class DNRouter$$Group$$c implements IRouteGroup {
  @Override
  public void loadInto(Map<String, RouteMeta> atlas) {
    atlas.put("/c/b",RouteMeta.build(RouteMeta.Type.ACTIVITY,MainActivity.B.class,"/c/b","c"));
  }
}
