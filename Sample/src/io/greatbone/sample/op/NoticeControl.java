package io.greatbone.sample.op;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebControl;
import io.greatbone.web.WebVirtualHost;

/**
 * The menu management handler.
 */
public class NoticeControl extends WebControl implements Runnable {

    public NoticeControl(WebVirtualHost host, WebControl parent) {
        super(host, parent);
    }

    @Override
    public void default_(WebContext wc) {

    }

    @Override
    public void run() {

    }

}
