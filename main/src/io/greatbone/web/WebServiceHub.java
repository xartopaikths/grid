package io.greatbone.web;

/**
 */
public abstract class WebServiceHub<X extends Scope> extends WebServiceX<X> {

    WebServiceHub(WebHost host) {
        super(host, host);
    }

    public WebServiceX locate(String key, WebContext wc) {
        wc.scope = key;
        return this;
    }


    protected abstract X resolve(String scope);

}
