package io.greatbone.sample.op;

import io.greatbone.sample.Shop;
import io.greatbone.web.WebControl;
import io.greatbone.web.WebContext;
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
        Shop shop = (Shop) wc.space();

    }

    public void Post(WebContext exch) {

//        String from = exch.getHeader("Dk-From");

        Shop shop = (Shop) exch.space();

        // parse msg


    }

    @Override
    public void run() {

    }

}
