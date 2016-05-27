package io.greatbone.web;

import io.greatbone.util.Roll;

import java.lang.reflect.Constructor;

/**
 * A control that can contain a number of sub controls.
 */
abstract class WebParentControl extends WebControl {

    Roll<String, WebControl> subs;

    WebParentControl(WebVirtualHost vhost, WebControl parent) {
        super(vhost, parent);
    }

    public WebControl locateSub(String key, WebContext wc) {
        return subs.get(key);
    }

    public <T extends WebControl> void addSub(String key, Class<T> class_, Authorizer authorizer) {
        try {
            Constructor<T> ctor = class_.getConstructor(WebVirtualHost.class, WebControl.class);
            T sub = ctor.newInstance(vhost, this);
            sub.key = key;
            sub.authorizer = authorizer;
            if (subs == null) {
                this.subs = new Roll<>(8);
            }
            subs.put(key, sub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
