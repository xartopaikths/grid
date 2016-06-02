package io.greatbone.web;

/**
 * A login principal.
 */
public interface WebPrincipal {

    String getName();

    String getCredential();

    boolean check(String zone, int roles);

}
