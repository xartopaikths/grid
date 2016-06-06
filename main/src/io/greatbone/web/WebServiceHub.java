package io.greatbone.web;

import io.greatbone.util.Roll;

import java.lang.reflect.Constructor;

/**
 */
public abstract class WebServiceHub<Z extends WebZone> extends WebService<Z> implements WebParent {

    Roll<String, WebService> subs;

    protected WebServiceHub(WebHost host) {
        super(host, host);
    }

    public <S extends WebService> void addSub(String key, Class<S> serviceClass, Authorizer authorizer) {
        try {
            Constructor<S> ctor = serviceClass.getConstructor(WebHost.class, WebParent.class);
            S sub = ctor.newInstance(host, this);
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

    @Override
    @SuppressWarnings("unchecked")
    protected void perform(String base, WebContext<Z> wc) throws Exception {
        int slash = base.indexOf("/");
        if (slash == -1) { // without a slash then handle by this host
            perform(base, wc);
        } else { // sub-perform
            WebService sub = subs.get(key);
            if (sub == null) { // sub unavailable
                wc.sendNotFound();
                return;
            }
            sub.perform(base.substring(slash + 1), wc);
        }
    }

    public WebService<Z> locate(String key, WebContext wc) {
//        wc.zone = key;
        return this;
    }


    protected abstract Z resolve(String zone);

}
