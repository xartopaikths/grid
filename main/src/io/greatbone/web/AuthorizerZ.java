package io.greatbone.web;

/**
 */
public interface AuthorizerZ<Z extends WebZone> {

    boolean authorize(Z zone, WebPrincipal prin);

}
