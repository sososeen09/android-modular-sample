package com.sososeen09.modular.compiler;

import com.google.auto.service.AutoService;
import com.sososeen09.modular.annotation.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by yunlong.su on 2018/3/22.
 */

@AutoService(Processor.class)
//当前注解处理器能够处理的注解 代替 getSupportedAnnotationTypes函数
@SupportedAnnotationTypes("com.sososeen09.modular.annotation.Router")
//java版本 代替 getSupportedAnnotationTypes 函数
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LoginCheckProcesser extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;
    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mMessager.printMessage(Diagnostic.Kind.NOTE, ">> LoginCheckProcesser init");

        mFiler = processingEnvironment.getFiler();

        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for (TypeElement typeElement : set) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, ">> LoginCheckProcesser process" + typeElement.getQualifiedName());


            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Router.class);
            for (Element element : elementsAnnotatedWith) {
                if (element instanceof TypeElement) {
                    PackageElement packageElement = mElementUtils.getPackageOf(element);
                    String packageName = packageElement.getQualifiedName().toString();
                    String prefixName = element.getSimpleName().toString();
                    ClassName activity = ClassName.get("android.app", "Activity");
                    MethodSpec inject = MethodSpec.methodBuilder("inject")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(void.class)
                            .addParameter(activity, "activity")
                            .addStatement("$T.out.println($S)", System.class, "inject activity success!!!")
                            .build();

                    ClassName loginInject = ClassName.get("com.sososeen09.modular.api", "ActivityInject");
                    TypeSpec typeSpec = TypeSpec.classBuilder(prefixName + "$$ActivityInject")
                            .addSuperinterface(loginInject)
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addMethod(inject)
                            .build();

                    JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                            .build();

                    try {
                        javaFile.writeTo(mFiler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        }


        return true;
    }
}
