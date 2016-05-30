package io.greatbone.web;

/**
 */
public class WebSocketActivity extends WebActivity implements ControlSet {


    protected WebSocketActivity(WebHost vhost, WebActivity parent) {
        super(vhost, parent);
    }

    @Override
    public WebActivity locateSub(String key, WebContext wc) {
        return null;
    }

}
