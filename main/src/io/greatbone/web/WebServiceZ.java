package io.greatbone.web;

import io.netty.handler.codec.http.HttpMethod;

import javax.naming.OperationNotSupportedException;

/**
 * A set of actions working on request/response eachanges to carry out management tasks on a collection of resources.
 */
public abstract class WebServiceZ<Z extends WebZone> {

    static final Exception NOT_SUPPORTED = new OperationNotSupportedException();

    // the container virtual host
    protected final WebHost host;

    // the parent host or hub
    protected final WebParent parent;

    // the key by which this control is added to a parent
    String key;

    // access checker
    Authorizer authorizer;

    // execution of background tasks
    Thread cycler;

    protected WebServiceZ(WebHost host, WebParent parent) {
        this.host = host;
        this.parent = parent;

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
     * @param base the relative URI that this controller is based
     * @param ctx  the request.response exchange
     */
    @SuppressWarnings("unchecked")
    protected void perform(String base, WebContext ctx) throws Exception {
        int slash = base.indexOf('/');
        if (slash == -1) { // without a slash then handle by this controller instance
//            ctx.activity = this;
            HttpMethod method = ctx.method();
            Z scope = (Z) ctx.scope;
            if (method == HttpMethod.GET) Get(ctx, scope);
            else if (method == HttpMethod.POST) Post(scope, ctx);
            else if (method == HttpMethod.PUT) Put(scope, ctx);
            else if (method == HttpMethod.PATCH) Patch(scope, ctx);
            else if (method == HttpMethod.DELETE) Delete(scope, ctx);
//        } else if (subordinates != null) { // resolve the sub structure
//            WebControl control = subordinates.locateSub(base.substring(0, slash), exch);
//            if (control != null) {
//                control.perform(base.substring(slash + 1), exch);
//            } else {
//                exch.sendNotFound();
//            }
        } else {
            ctx.sendNotFound();
        }
    }

    public void Get(WebContext wc, Z zone) throws Exception {
        throw NOT_SUPPORTED;
    }

    public void Get(Z zone, String rsc, WebContext wc) throws Exception {
        throw NOT_SUPPORTED;
    }

    public void Post(Z zone, WebContext wc) throws Exception {
        throw NOT_SUPPORTED;
    }

    public void Put(Z zone, WebContext wc) throws Exception {
        throw NOT_SUPPORTED;
    }

    public void Patch(Z zone, WebContext wc) throws Exception {
        throw NOT_SUPPORTED;
    }

    public void Delete(Z zone, WebContext wc) throws Exception {
        throw NOT_SUPPORTED;
    }

}
