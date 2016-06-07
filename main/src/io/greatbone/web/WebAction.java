package io.greatbone.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 */
public class WebAction<Z extends WebZone> {

    // the service instance that this action operates on
    final WebService<Z> service;

    final Method method;

    WebAction(WebService<Z> service, Method method) {
        this.service = service;
        this.method = method;
    }

    void invoke(WebContext<Z> wc) throws InvocationTargetException, IllegalAccessException {
        method.invoke(service, wc);
    }

}
