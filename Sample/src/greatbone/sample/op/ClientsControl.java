package greatbone.sample.op;

import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;
import greatbone.sample.Shop;

/**
 * The order management handler.
 */
public class ClientsControl extends WebControl {


    public ClientsControl(WebHost host, WebControl parent) {
        super(host, parent);

    }

    @Override
    public void index(WebContext exch) {
        Shop shop = (Shop) exch.space();
    }

}