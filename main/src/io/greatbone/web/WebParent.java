package io.greatbone.web;

/**
 * A web service that can contain a number of sub services.
 */
public interface WebParent {

    public WebService locateSub(String key, WebContext wc);

}
