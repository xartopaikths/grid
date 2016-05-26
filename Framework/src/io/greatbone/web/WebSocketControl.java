package io.greatbone.web;

import io.undertow.websockets.WebSocketProtocolHandshakeHandler;

/**
 */
public class WebSocketControl extends WebControl implements ControlSet {

    WebSocketProtocolHandshakeHandler dv;

    protected WebSocketControl(WebVHost vhost, WebControl parent) {
        super(vhost, parent);
    }

    @Override
    public WebControl locateSub(String key, WebContext wc) {
        return null;
    }

}
