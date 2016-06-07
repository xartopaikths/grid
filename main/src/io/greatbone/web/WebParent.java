package io.greatbone.web;

/**
 * A web service that can contain a number of sub services.
 */
public interface WebParent {

    public WebService subordinate(String key, WebContext wc);

}
