package io.greatbone.web;

/**
 */
public interface Authorizer {

    boolean authorize(String scope, WebPrincipal prin);

}
