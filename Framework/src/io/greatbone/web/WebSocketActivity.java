package io.greatbone.web;

import io.undertow.websockets.WebSocketProtocolHandshakeHandler;

/**
 */
public class WebSocketActivity extends WebActivity implements ControlSet {

    WebSocketProtocolHandshakeHandler dv;

    protected WebSocketActivity(WebHostActivity vhost, WebActivity parent) {
        super(vhost, parent);
    }

    @Override
    public WebActivity locateSub(String key, WebContext wc) {
        return null;
    }

}
