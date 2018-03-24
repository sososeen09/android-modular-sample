package com.sososeen09.modular.compiler;

import com.google.auto.service.AutoService;
import com.sososeen09.modular.annotation.Extra;
import com.sososeen09.modular.annotation.Route;
import com.sososeen09.modular.compiler.utils.Consts;
import com.sososeen09.modular.compiler.utils.EmptyUtils;
import com.sososeen09.modular.compiler.utils.LoadExtraBuilder;
import com.sososeen09.modular.compiler.utils.Log;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by yunlong on 2018/3/24.
 */

@AutoService(Processor.class)
@SupportedAnnotationTypes(Consts.ANN_TYPE_EXTRA)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions(Consts.ARGUMENTS_NAME)
public class ExtraProcessor extends AbstractProcessor {
    private Log log;
    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;
    private String moduleName;

    /**
     * 记录所有需要注入的属性 key:类节点 value:需要注入的属性节点集合
     */
    private Map<TypeElement, List<Element>> parentAndChild = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        log = Log.newLog(processingEnvironment.getMessager());
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();

        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的 xx$$ROOT$$文件
        Map<String, String> options = processingEnvironment.getOptions();
        if (!EmptyUtils.isEmpty(options)) {
            moduleName = options.get(Consts.ARGUMENTS_NAME);
        }

        if (EmptyUtils.isEmpty(moduleName)) {
            throw new RuntimeException("Not set Processor Parmaters.");
        }

        log.i(">> ExtraProcessor init: " + moduleName);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!EmptyUtils.isEmpty(set)) {
            Set<? extends Element> extraElements = roundEnvironment.getElementsAnnotatedWith(Extra.class);
            if (!EmptyUtils.isEmpty(extraElements)) {
                try {
                    categories(extraElements);
                    generateAutoWired();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        return false;
    }

    private void generateAutoWired() throws IOException {

        TypeMirror typeActivity = elementUtils.getTypeElement(Consts.ACTIVITY).asType();
        TypeElement iExtra = elementUtils.getTypeElement(Consts.IEXTRA);
        ParameterSpec parameterSpec = ParameterSpec.builder(Object.class, "target").build();

        for (Map.Entry<TypeElement, List<Element>> typeElementListEntry : parentAndChild.entrySet()) {
            //@Extra注解所在的类
            TypeElement rawClassElement = typeElementListEntry.getKey();

            if (!typeUtils.isSubtype(rawClassElement.asType(), typeActivity)) {
                throw new RuntimeException("[Just Support Activity Field]:" +
                        rawClassElement);
            }

            LoadExtraBuilder loadExtra = new LoadExtraBuilder(parameterSpec);
            loadExtra.setElementUtils(elementUtils);
            loadExtra.setTypeUtils(typeUtils);


            ClassName className = ClassName.get(rawClassElement);
            loadExtra.injectTarget(className);

            for (Element extraElement : typeElementListEntry.getValue()) {
                loadExtra.buildStatement(extraElement);
            }

           //生成的java类名
            String extraClassName = rawClassElement.getSimpleName() + Consts.NAME_OF_EXTRA;
            TypeSpec typeSpec=TypeSpec.classBuilder(extraClassName).addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(iExtra))
                    .addMethod(loadExtra.build())
                    .build();

            JavaFile.builder( className.packageName(),typeSpec).build().writeTo(filer);

            log.i("Generated Extra: " + className.packageName() + "." + extraClassName);

        }
    }

    private void categories(Set<? extends Element> extraElements) {
        for (Element extraElement : extraElements) {
            TypeElement typeElement = (TypeElement) extraElement.getEnclosingElement();
            if (parentAndChild.containsKey(typeElement)) {
                parentAndChild.get(typeElement).add(extraElement);
            } else {
                List<Element> elements = new ArrayList<>();
                elements.add(extraElement);
                parentAndChild.put(typeElement, elements);
            }
        }
    }
}
