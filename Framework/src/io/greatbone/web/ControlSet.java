package io.greatbone.web;

/**
 * A subsidiary structure of a parent work, can be either a set of sub works, or a hub representing many spaces.
 */
public interface ControlSet {

    WebActivity locateSub(String key, WebContext wc);

}
