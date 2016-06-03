package io.greatbone.web;

/**
 */
public interface Authorizer<Z extends WebZone> {

    boolean authorize(WebContext<Z> wc);

}
