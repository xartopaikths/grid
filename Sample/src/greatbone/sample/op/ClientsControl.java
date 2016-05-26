package greatbone.sample.op;

import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebVHost;
import greatbone.sample.Shop;

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
