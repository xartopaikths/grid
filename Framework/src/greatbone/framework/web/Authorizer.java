package greatbone.framework.web;

/**
 */
public interface Authorizer {

    Grantable authorize(String scope, WebPrincipal prin);

}
