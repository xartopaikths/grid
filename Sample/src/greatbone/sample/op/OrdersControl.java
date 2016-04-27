package greatbone.sample.op;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.Call;
import greatbone.sample.ORDERS;
import greatbone.sample.Shop;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 * The order management handler.
 */
public class OrdersControl extends WebControl {


    final ORDERS dset;

    public OrdersControl(WebHost host, WebControl parent) {
        super(host, parent);

        dset = GridUtility.getDataSet(ORDERS.class);
    }

    @Override
    public void Get(WebContext exch) {
        Shop shop = (Shop) exch.space();

        int id = 213;
        String name = "asdfadf";

        Call obj = new Call();

    }

}
