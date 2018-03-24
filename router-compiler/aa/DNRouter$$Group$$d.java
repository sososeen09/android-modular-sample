package com.sososeen09.modular.compiler;

import com.dongnao.alvin.test.MainActivity;
import com.dongnao.router.annotation.model.RouteMeta;
import com.dongnao.router.core.template.IRouteGroup;

import java.util.Map;

public class DNRouter$$Group$$d implements IRouteGroup {
  @Override
  public void loadInto(Map<String, RouteMeta> atlas) {
    atlas.put("/d/b",RouteMeta.build(RouteMeta.Type.ACTIVITY,MainActivity.C.class,"/d/b","d"));
    atlas.put("/d/c",RouteMeta.build(RouteMeta.Type.ACTIVITY,MainActivity.D.class,"/d/c","d"));
  }
}
