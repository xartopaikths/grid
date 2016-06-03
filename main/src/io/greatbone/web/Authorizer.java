package io.greatbone.web;

/**
 */
public interface Authorizer {

    boolean authorize(WebPrincipal prin);

}
