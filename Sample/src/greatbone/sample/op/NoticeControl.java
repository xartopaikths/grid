package greatbone.sample.op;

import greatbone.sample.Shop;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebVirtualHost;

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
