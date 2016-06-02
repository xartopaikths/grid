package io.greatbone.web;

/**
 */
public abstract class WebServiceHub<Z extends WebZone> extends WebServiceZ<Z> implements WebParent {

    WebServiceHub(WebHost host) {
        super(host, host);
    }

    public WebServiceZ locate(String key, WebContext wc) {
        wc.scope = key;
        return this;
    }


    protected abstract Z resolve(String scope);

}
