package greatbone.sample.op;

import greatbone.framework.web.WebZone;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 * The menu management handler.
 */
public class ShopControl extends WebControl {

    public ShopControl(WebHost root, WebControl parent) {
        super(root, parent);
    }

    @Override
    public void _(WebContext exch) {
        WebZone shopid = exch.space();

    }

    public void Post(WebContext exch) {
        WebZone shopid = exch.space();

    }

}
