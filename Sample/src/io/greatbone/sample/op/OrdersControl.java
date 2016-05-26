package io.greatbone.sample.op;

import io.greatbone.grid.GridUtility;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebControl;
import io.greatbone.web.WebVHost;
import io.greatbone.sample.ORDERS;
import io.greatbone.sample.Shop;

/**
 * The order management handler.
 */
public class OrdersControl extends WebControl {

    final ORDERS sales;

    public OrdersControl(WebVHost host, WebControl parent) {
        super(host, parent);

        sales = GridUtility.getCache(ORDERS.class);
    }

    @Override
    public void default_(WebContext exch) {
        Shop shop = (Shop) exch.space();

        int id = 213;
        String name = "asdfadf";

    }

}
