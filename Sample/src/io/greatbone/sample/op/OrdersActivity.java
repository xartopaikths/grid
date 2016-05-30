package io.greatbone.sample.op;

import io.greatbone.grid.GridUtility;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebActivity;
import io.greatbone.web.WebHost;
import io.greatbone.sample.ORDERS;

/**
 * The order management handler.
 */
public class OrdersActivity extends WebActivity {

    final ORDERS sales;

    public OrdersActivity(WebHost host, WebActivity parent) {
        super(host, parent);

        sales = GridUtility.getCache(ORDERS.class);
    }

    @Override
    public void default_(WebContext wc) {

        int id = 213;
        String name = "asdfadf";

    }

}
