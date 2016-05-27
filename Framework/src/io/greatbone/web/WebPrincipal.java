package io.greatbone.web;

/**
 */
public interface WebPrincipal{

    String getName();

    String getCredential();

    boolean check(String scope, int roles);

}
