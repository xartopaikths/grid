package greatbone.sample.op;

import greatbone.framework.grid.GridUtility;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;
import greatbone.sample.SALES;
import greatbone.sample.Party;

/**
 * The order management handler.
 */
public class SalesControl extends WebControl {

    final SALES sales;

    public SalesControl(WebHost host, WebControl parent) {
        super(host, parent);

        sales = GridUtility.getDataSet(SALES.class);
    }

    @Override
    public void _(WebContext exch) {
        Party party = (Party) exch.space();

        int id = 213;
        String name = "asdfadf";

    }

}
