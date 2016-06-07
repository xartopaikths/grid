package io.greatbone.web;

import io.greatbone.util.Roll;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A web service target that works on request/response eachanges to carry out tasks on a collection of resources.
 */
public abstract class WebService<Z extends WebZone> {

    // the root handler
    protected final WebHost host;

    // the parent of this instance, if any
    protected final WebParent parent;

    // the key by which this service is added to its parent
    String key;

    final Roll<String, WebAction> actions = new Roll<>(32);

    // access checker
    Authorizer authorizer;

    // execution of background tasks
    Thread cycler;

    @SuppressWarnings("unchecked")
    protected WebService(WebHost host, WebParent parent) {
        this.host = (host != null) ? host : (WebHost) this;
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
                    actions.put(key, new WebAction(this, m));
                }
            }
        }

        // initialize the cycler thread if any
        if (this instanceof Runnable) {
            cycler = new Thread((Runnable) this);
            cycler.start();
        }
    }

    public String key() {
        return key;
    }

    /**
     * To handle a request/response exchange by this or by an appropriate sub controller.
     *
     * @param rsc the relative URI that this controller is based
     * @param wc  the request.response exchange
     */
    @SuppressWarnings("unchecked")
    void perform(String rsc, WebContext<Z> wc) throws Exception {
        if (rsc.isEmpty()) {
            rsc = "default_";
        }
        WebAction action = actions.get(rsc);
        if (action == null) {
            wc.setStatus(HttpResponseStatus.NOT_FOUND);

            return;
        }
        wc.service = this;
        wc.action = action;
        action.invoke(wc);
    }

    public abstract void default_(WebContext<Z> wc) throws Exception;

}
