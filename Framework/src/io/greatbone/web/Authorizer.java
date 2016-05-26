package io.greatbone.web;

/**
 */
public interface Authorizer {

    Grantable authorize(String scope, WebPrincipal prin);

}
