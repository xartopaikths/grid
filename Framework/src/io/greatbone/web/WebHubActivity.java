package io.greatbone.web;

/**
 */
public class WebHubActivity extends WebParent {

    WebHubActivity(WebHost vhost, WebActivity parent) {
        super(vhost, parent);
    }

    @Override
    public WebActivity locateSub(String key, WebContext wc) {
        wc.space = key;
        return this;
    }


}
