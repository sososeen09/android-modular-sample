package com.sososeen09.modular.compiler;

import com.dongnao.alvin.test.MainActivity;
import com.dongnao.router.annotation.model.RouteMeta;
import com.dongnao.router.core.template.IRouteGroup;

import java.util.Map;

public class DNRouter$$Group$$a implements IRouteGroup {
  @Override
  public void loadInto(Map<String, RouteMeta> atlas) {
    atlas.put("/a/b",RouteMeta.build(RouteMeta.Type.ACTIVITY,MainActivity.class,"/a/b","a"));
    atlas.put("/a/c",RouteMeta.build(RouteMeta.Type.ACTIVITY,MainActivity.A.class,"/a/c","a"));
  }
}
