package io.greatbone.sample.op;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebControl;
import io.greatbone.web.WebVHost;
import io.greatbone.sample.Shop;

/**
 * The order management handler.
 */
public class ClientsControl extends WebControl {


    public ClientsControl(WebVHost host, WebControl parent) {
        super(host, parent);

    }

    @Override
    public void default_(WebContext exch) {
        Shop shop = (Shop) exch.space();
    }

}
