package io.greatbone.web;

/**
 */
public interface Authorize {

    boolean authorize(String scope, WebPrincipal prin);

}
