package io.greatbone.web;

import io.greatbone.util.Roll;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A set of actions working on request/response eachanges to carry out management tasks on a collection of resources.
 */
public abstract class WebControl {

    // the root handler
    protected final WebVirtualHost vhost;

    // the parent of this work instance, if any
    protected final WebControl parent;

    final Roll<String, Action> actions = new Roll<>(32);

    // name as appeared in the uri
    String key;

    // access checker
    Authorizer authorizer;

    // the subordinate structures
    ControlSet subordinates;

    // execution of background tasks
    Thread cycler;

    protected WebControl(WebVirtualHost vhost, WebControl parent) {
        this.vhost = (vhost != null) ? vhost : (WebVirtualHost) this;
        this.parent = parent;

        // initialize web methods
        for (Method m : getClass().getMethods()) {
            int mod = m.getModifiers();
            // public non-static void
            if (Modifier.isPublic(mod) && !Modifier.isStatic(mod)) {
                Class<?>[] pts = m.getParameterTypes();
                // with the two parameters
                if (pts.length == 1 && WebContext.class == pts[0]) {
                    String key = m.getName().toLowerCase();
                    if ("$".equals(key)) {
                        key = "";
                    }
                    actions.put(key, new Action(this, m));
                }
            }
        }

        // initialize the cycler thread if any
        if (this instanceof Runnable) {
            cycler = new Thread((Runnable) this);
            cycler.start();
        }
    }

    public <T extends WebControl> void addSub(String key, Class<T> controlClass, Authorizer auth) {
        try {
            Constructor<T> ctor = controlClass.getConstructor(WebVirtualHost.class, WebControl.class);
            T sub = ctor.newInstance(vhost, this);
            sub.key = key;
            sub.authorizer = auth;
            if (this.subordinates == null) {
                this.subordinates = new HardControlSet(8);
            }
            ((HardControlSet) this.subordinates).add(sub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String key() {
        return key;
    }

    public ControlSet children() {
        return subordinates;
    }

    /**
     * To handle a request/response exchange by this or by an appropriate sub controller.
     *
     * @param base the relative URI that this controller is based
     * @param exch the request.response exchange
     */
    protected void perform(String base, WebContext exch) throws Exception {
        int slash = base.indexOf('/');
        if (slash == -1) { // without a slash then handle by this controller instance
            exch.control = this;
            HttpString method = exch.method();
            if (method == Methods.GET) default_(exch);
        } else if (subordinates != null) { // resolve the sub structure
            WebControl control = subordinates.locateSub(base.substring(0, slash), exch);
            if (control != null) {
                control.perform(base.substring(slash + 1), exch);
            } else {
                exch.sendNotFound();
            }
        } else {
            exch.sendNotFound();
        }
    }

    public void default_(WebContext exch) throws Exception {
    }

    /**
     * A hashmap-based implemention that holds a set of sub works.
     */
    static class HardControlSet implements ControlSet {

        final Roll<String, WebControl> controllers;

        HardControlSet(int initial) {
            this.controllers = new Roll<>(initial);
        }

        public WebControl locateSub(String key, WebContext wc) {
            return controllers.get(key);
        }

        void add(WebControl v) {
            controllers.put(v.key, v);
        }

    }

}
