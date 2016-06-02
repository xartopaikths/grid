package io.greatbone.web;

import io.netty.handler.codec.http.HttpMethod;

/**
 * A web service target that works on request/response eachanges to carry out tasks on a collection of resources.
 */
public abstract class WebService implements WebZone {

    // the root handler
    protected final WebHost host;

    // the parent of this instance, if any
    protected final WebParent parent;

    // the key by which this service is added to its parent
    String key;

    // access checker
    Authorize authorize;

    // execution of background tasks
    Thread cycler;

    protected WebService(WebHost host, WebParent parent) {
        this.host = (host != null) ? host : (WebHost) this;
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
    public WebSocketDoer doer() {
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
