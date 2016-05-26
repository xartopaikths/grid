package greatbone.sample.op;

import greatbone.framework.grid.GridUtility;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebVHost;
import greatbone.sample.ORDERS;
import greatbone.sample.Shop;

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
