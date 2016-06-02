package io.greatbone.web;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;

import java.util.List;

/**
 * A set of actions working on request/response eachanges to carry out management tasks on a collection of resources.
 */
public abstract class WebService implements Scope {

    // the root handler
    protected final WebHost vhost;

    // the parent of this work instance, if any
    protected final WebParent parent;

    // the key by which this control is added to a parent
    String key;

    // access checker
    Authorize authorize;

    // execution of background tasks
    Thread cycler;

    protected WebService(WebHost vhost, WebParent parent) {
        this.vhost = (vhost != null) ? vhost : (WebHost) this;
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

    @Override
    public String id() {
        return null;
    }

    @Override
    public List<ChannelHandlerContext> contexts() {
        return null;
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
            exch.activity = this;
            HttpMethod method = exch.method();
            if (method == HttpMethod.GET) Get(exch);
            else if (method == HttpMethod.POST) Post(exch);
            else if (method == HttpMethod.PUT) Put(exch);
            else if (method == HttpMethod.PATCH) Patch(exch);
            else if (method == HttpMethod.DELETE) Delete(exch);
//        } else if (subordinates != null) { // resolve the sub structure
//            WebControl control = subordinates.locateSub(base.substring(0, slash), exch);
//            if (control != null) {
//                control.perform(base.substring(slash + 1), exch);
//            } else {
//                exch.sendNotFound();
//            }
        } else {
            exch.sendNotFound();
        }
    }

    public void Get(WebContext wc) throws Exception {
    }

    public void Get(String rsc, WebContext wc) throws Exception {
    }

    public void Post(WebContext wc) throws Exception {
    }

    public void Put(WebContext wc) throws Exception {
    }

    public void Patch(WebContext wc) throws Exception {
    }

    public void Delete(WebContext wc) throws Exception {
    }

}
