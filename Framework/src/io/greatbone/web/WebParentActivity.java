package io.greatbone.web;

import io.greatbone.util.Roll;

import java.lang.reflect.Constructor;

/**
 * An activity that can contain a number of sub activities.
 */
abstract class WebParentActivity extends WebActivity {

    Roll<String, WebActivity> subs;

    WebParentActivity(WebHost root, WebActivity parent) {
        super(root, parent);
    }

    public WebActivity locateSub(String key, WebContext wc) {
        return subs.get(key);
    }

    public <T extends WebActivity> void addSub(String key, Class<T> class_, Authorizer authorizer) {
        try {
            Constructor<T> ctor = class_.getConstructor(WebHost.class, WebActivity.class);
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
