package com.sososeen09.easy.routes;

import com.sososeen09.modular.annotation.model.RouteMeta;
import com.sososeen09.modular.api.template.IRouteGroup;
import com.sososeen09.modular.sample.SecondActivity;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

public class EasyRouter$$Group$$test implements IRouteGroup {
  @Override
  public void loadInto(Map<String, RouteMeta> atlas) {
    atlas.put("/test/second",RouteMeta.build(RouteMeta.Type.ACTIVITY,SecondActivity.class,"/test/second","test"));
  }
}
