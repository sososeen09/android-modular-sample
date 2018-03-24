package com.sososeen09.modular.api.exception;

/**
 * Created by yunlong on 2018/3/24.
 */

public class NoRouteFoundException extends RuntimeException {

    public NoRouteFoundException(String detailMessage) {
        super(detailMessage);
    }
}
