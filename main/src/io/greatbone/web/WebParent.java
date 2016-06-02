package io.greatbone.web;

/**
 * An activity that can contain a number of sub activities.
 */
public interface WebParent {

    public WebService locateSub(String key, WebContext wc);

}
