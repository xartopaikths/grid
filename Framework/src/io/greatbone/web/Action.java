package io.greatbone.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 */
class Action {

    final WebControl instance;

    final Method method;

    Action(WebControl instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    void invoke(WebContext wc) throws InvocationTargetException, IllegalAccessException {
        method.invoke(instance, wc);
    }

}
