package io.greatbone.web;

/**
 */
public interface Authorizer {

    boolean authorize(WebContext wc);

}
