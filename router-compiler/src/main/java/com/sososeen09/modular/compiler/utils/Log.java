package com.sososeen09.modular.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by yunlong on 2018/3/24.
 */

public class Log {
    private Messager messager;

    private Log(Messager messager) {
        this.messager = messager;
    }

    public static Log newLog(Messager messager) {
        return new Log(messager);
    }

    public void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    public void e(String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
