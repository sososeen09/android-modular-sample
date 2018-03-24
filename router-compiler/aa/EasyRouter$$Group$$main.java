package com.sososeen09.easy.routes;

import com.sososeen09.modular.annotation.model.RouteMeta;
import com.sososeen09.modular.api.template.IRouteGroup;
import com.sososeen09.modular.sample.SecondActivity;
import com.sososeen09.modular.sample.TestServiceImpl1;
import com.sososeen09.modular.sample.TestServiceImpl2;
import java.lang.Override;
import java.lang.String;
import java.util.Map;

public class EasyRouter$$Group$$main implements IRouteGroup {
  @Override
  public void loadInto(Map<String, RouteMeta> atlas) {
    atlas.put("/main/service1",RouteMeta.build(RouteMeta.Type.ISERVICE,TestServiceImpl1.class,"/main/service1","main"));
    atlas.put("/main/service2",RouteMeta.build(RouteMeta.Type.ISERVICE,TestServiceImpl2.class,"/main/service2","main"));
    atlas.put("/main/test",RouteMeta.build(RouteMeta.Type.ACTIVITY,SecondActivity.class,"/main/test","main"));
  }
}
