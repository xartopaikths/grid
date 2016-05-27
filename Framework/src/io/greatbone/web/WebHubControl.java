package io.greatbone.web;

/**
 */
public class WebHubControl extends WebParentControl {

    WebHubControl(WebVirtualHost vhost, WebControl parent) {
        super(vhost, parent);
    }

    @Override
    public WebControl locateSub(String key, WebContext wc) {
        wc.space = key;
        return this;
    }


}