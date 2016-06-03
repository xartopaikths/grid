package io.greatbone.web;

/**
 */
public abstract class WebServiceHub<Z extends WebZone> extends WebService<Z> implements WebParent {

    WebServiceHub(WebHost host) {
        super(host, host);
    }

    public WebService<Z> locate(String key, WebContext wc) {
//        wc.zone = key;
        return this;
    }


    protected abstract Z resolve(String zone);

}
