package greatbone.framework.web;

/**
 * A subsidiary structure of a parent work, can be either a set of sub works, or a hub representing many spaces.
 */
public interface ControlSet {

    WebControl locateSub(String key, WebContext wc);

}
