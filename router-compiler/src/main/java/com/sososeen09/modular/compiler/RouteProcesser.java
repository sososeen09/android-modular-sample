package com.sososeen09.modular.compiler;

import com.google.auto.service.AutoService;
import com.sososeen09.modular.annotation.Route;
import com.sososeen09.modular.annotation.model.RouteMeta;
import com.sososeen09.modular.compiler.utils.Consts;
import com.sososeen09.modular.compiler.utils.EmptyUtils;
import com.sososeen09.modular.compiler.utils.Log;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by yunlong.su on 2018/3/22.
 */

@AutoService(Processor.class)
//当前注解处理器能够处理的注解 代替 getSupportedAnnotationTypes函数
@SupportedAnnotationTypes(Consts.ANN_TYPE_ROUTE)
//java版本 代替 getSupportedAnnotationTypes 函数
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions(Consts.ARGUMENTS_NAME)
public class RouteProcesser extends AbstractProcessor {

    /**
     * 文件生成器 类/资源
     */
    private Filer mFiler;
    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements mElementUtils;
    private Log log;
    /**
     * type(类信息)工具类
     */
    private Types mTypeUtils;
    /**
     * 生成的moduleName，这个可以在模块的build.gradle中配置属性
     */
    private String mModuleName;


    /**
     * key:组名 value:类名
     */
    private Map<String, String> rootMap = new TreeMap<>();

    /**
     * 存放单个分组信息，一个分组可以放置很多个路由表,键是组名
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();

    /**
     * 初始化，为了从ProcessingEnvironment获得一系列工具
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        log = Log.newLog(processingEnvironment.getMessager());
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mTypeUtils = processingEnvironment.getTypeUtils();


        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的 xx$$ROOT$$文件
        Map<String, String> options = processingEnvironment.getOptions();
        if (!EmptyUtils.isEmpty(options)) {
            mModuleName = options.get(Consts.ARGUMENTS_NAME);
        }

        if (EmptyUtils.isEmpty(mModuleName)) {
            throw new RuntimeException("Not set Processor Parmaters.");
        }

        log.i(">> RouteProcesser init: " + mModuleName);
    }

    /**
     * 相当于main函数，正式处理注解,该方法可以被调用多次
     *
     * @param set              使用了支持处理注解  的节点集合
     * @param roundEnvironment 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!EmptyUtils.isEmpty(set)) {
            Set<? extends Element> routeElements = roundEnvironment.getElementsAnnotatedWith(Route.class);
            if (!EmptyUtils.isEmpty(routeElements)) {
                processRoute(routeElements);
            }
            return true;
        }

        return false;
    }

    private void processRoute(Set<? extends Element> routeElements) {
        //获取Activity类的结点信息
        TypeElement typeActivity = mElementUtils.getTypeElement(Consts.ACTIVITY);

        //获取IService服务的结点信息
        TypeElement typeService = mElementUtils.getTypeElement(Consts.ISERVICE);

        for (Element routeElement : routeElements) {
            TypeMirror typeMirror = routeElement.asType();
            log.i("Route class:" + typeMirror.toString());

            Route route = routeElement.getAnnotation(Route.class);

            RouteMeta routeMeta;
            //只能在指定的类上面使用
            if (mTypeUtils.isSubtype(typeMirror, typeActivity.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, routeElement);
            } else if (mTypeUtils.isSubtype(typeMirror, typeService.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ISERVICE, route, routeElement);
            } else {
                throw new RuntimeException("[Just support] Activity and IService Route：" + routeElement);
            }

            //检查路径和分组，如果没有配置group  则从path截取出组名
            categories(routeMeta);
        }

        //至此，全部的路径都已经取出来了
        TypeElement iRouteGroup = mElementUtils.getTypeElement(Consts.IROUTE_GROUP);
        TypeElement iRouteRoot = mElementUtils.getTypeElement(Consts.IROUTE_ROOT);

        //生成 $$Group$$ 记录分组表
        generatedGroup(iRouteGroup);


        //生成 $$Root$$  记录路由表
        /**
         * 生成Root类 作用:记录 <分组，对应的Group类>
         */
        try {
            generatedRoot(iRouteRoot, iRouteGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatedRoot(TypeElement iRouteRoot, TypeElement iRouteGroup) throws IOException {
        log.i("generatedRoot");
        ParameterizedTypeName routes = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(iRouteGroup))));

        //创建参数
        //Map<String, RouteMeta> atlas
        ParameterSpec parameterSpec = ParameterSpec.builder(routes, "routes").build();
        MethodSpec.Builder loadIntoBuiler = MethodSpec.methodBuilder(Consts.METHOD_LOAD_INTO)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameterSpec)
                .returns(void.class);
        for (Map.Entry<String, String> stringStringEntry : rootMap.entrySet()) {
//            routes.put("a", DNRouter$$Group$$a.class);
            String groupName = stringStringEntry.getKey();
            String groupClassName = stringStringEntry.getValue();
            loadIntoBuiler.addStatement("routes.put($S,$T.class)", groupName,
                    ClassName.get(Consts.PACKAGE_OF_GENERATE_FILE, groupClassName));
        }

        String rootClassName = Consts.NAME_OF_ROOT + mModuleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(rootClassName)
                .addMethod(loadIntoBuiler.build())
                .addSuperinterface(ClassName.get(iRouteRoot))
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile javaFile = JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE, typeSpec).build();

        javaFile.writeTo(mFiler);

    }

    private void generatedGroup(TypeElement iRouteGroup) {

        log.i("generatedGroup");
        ParameterizedTypeName atlas = ParameterizedTypeName.get(Map.class, String.class, RouteMeta.class);
        //创建参数
        //Map<String, RouteMeta> atlas
        ParameterSpec parameterSpec = ParameterSpec.builder(atlas, "atlas").build();

        for (Map.Entry<String, List<RouteMeta>> stringListEntry : groupMap.entrySet()) {
            MethodSpec.Builder loadIntoBuiler = MethodSpec.methodBuilder("loadInto")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(parameterSpec)
                    .returns(void.class);

            String groupName = stringListEntry.getKey();
            List<RouteMeta> value = stringListEntry.getValue();
//            atlas.put("/d/b",RouteMeta.build(RouteMeta.Type.ACTIVITY,MainActivity.C.class,"/d/b","d"));
            for (RouteMeta routeMeta : value) {
                loadIntoBuiler.addStatement("atlas.put($S,$T.build($T.$L,$T.class,$S,$S))",
                        routeMeta.getPath(),
                        RouteMeta.class,
                        RouteMeta.Type.class,
                        RouteMeta.Type.ACTIVITY,
                        ClassName.get(((TypeElement) routeMeta.getElement())),
                        routeMeta.getPath(),
                        routeMeta.getGroup());
            }


            String groupClassName = Consts.NAME_OF_GROUP + groupName;
            TypeSpec typeSpec = TypeSpec.classBuilder(groupClassName)
                    .addSuperinterface(ClassName.get(iRouteGroup))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(loadIntoBuiler.build())
                    .build();


            JavaFile javaFile = JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE, typeSpec).build();

            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //文件
            rootMap.put(groupName, groupClassName);
        }


    }

    private void categories(RouteMeta routeMeta) {
        if (routeVerify(routeMeta)) {
            log.i("Group :" + routeMeta.getGroup() + " path=" + routeMeta.getPath());
            //分组与组中的路由信息
            List<RouteMeta> routeMetas = groupMap.get(routeMeta.getGroup());
            if (EmptyUtils.isEmpty(routeMetas)) {
                routeMetas = new ArrayList<>();
                routeMetas.add(routeMeta);
                groupMap.put(routeMeta.getGroup(), routeMetas);
            } else {
                routeMetas.add(routeMeta);
            }
        } else {
            log.i("Group Info Error:" + routeMeta.getPath());
        }
    }

    private boolean routeVerify(RouteMeta routeMeta) {
        String path = routeMeta.getPath();
        String group = routeMeta.getGroup();
        // 必须以 / 开头来指定路由地址
        if (!path.startsWith("/")) {
            return false;
        }
        //如果group没有设置 我们从path中获得group
        if (EmptyUtils.isEmpty(group)) {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            //截取出的group还是空
            if (EmptyUtils.isEmpty(defaultGroup)) {
                return false;
            }
            routeMeta.setGroup(defaultGroup);
        }
        return true;
    }
}
