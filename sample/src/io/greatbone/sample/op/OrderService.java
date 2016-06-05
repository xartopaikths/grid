package io.greatbone.sample.op;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.ORDERS;
import io.greatbone.sample.Org;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebParent;
import io.greatbone.web.WebService;

/**
 * The order management handler.
 */
public class OrderService extends WebService<Org> {

    final ORDERS sales;

    public OrderService(WebHost host, WebParent parent) {
        super(host, parent);

        sales = GridUtility.getCache(ORDERS.class);
    }

    @Override
    public void default_(WebContext<Org> wc) {

        int id = 213;
        String name = "asdfadf";

    }

}
