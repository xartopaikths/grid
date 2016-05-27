package io.greatbone.sample.op;

import io.greatbone.grid.GridUtility;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebControl;
import io.greatbone.web.WebVirtualHost;
import io.greatbone.sample.ORDERS;

/**
 * The order management handler.
 */
public class OrdersControl extends WebControl {

    final ORDERS sales;

    public OrdersControl(WebVirtualHost host, WebControl parent) {
        super(host, parent);

        sales = GridUtility.getCache(ORDERS.class);
    }

    @Override
    public void default_(WebContext wc) {

        int id = 213;
        String name = "asdfadf";

    }

}
