package greatbone.sample.op;

import greatbone.framework.web.*;
import greatbone.sample.Shop;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class OpHost extends WebHost {

    public OpHost(WebUtility web, String key) {
        super(web, key);

        setHub(ShopControl.class, null);
    }

    @Override
    public void index(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

    /**
     * A hub that covers orgs.
     */
    public static class ShopControl extends WebControl implements Sub {

        public ShopControl(WebHost host, WebControl parent) {
            super(host, parent);

            // agent functions
//            addSub("shop", OrgControl.class, null);

            // shop functions
            addSub("sales", OrdersControl.class, null);
            addSub("user", ClientsControl.class, null);
            addSub("stocks", SettingsControl.class, null);
            addSub("notice", NoticeControl.class, null);
        }

        @Override
        public WebControl locate(String key, WebContext wc) {
            Shop shop = null;
            if (shop != null) {
                wc.setSpace(shop);
                return this;
            }
            return null;
        }

    }


}
