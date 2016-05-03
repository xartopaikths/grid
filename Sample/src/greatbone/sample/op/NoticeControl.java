package greatbone.sample.op;

import greatbone.sample.Shop;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebHost;

import java.util.ArrayList;

/**
 * The menu management handler.
 */
public class NoticeControl extends WebControl implements Runnable {

    public NoticeControl(WebHost host, WebControl parent) {
        super(host, parent);
    }

    @Override
    public void _(WebContext wc) {
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
