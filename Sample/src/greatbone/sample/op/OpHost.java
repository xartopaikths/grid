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

        setHub(OrgControl.class, null);
    }

    @Override
    public void Get(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

    @Override
    public void Put(String rsc, WebContext exch) {

    }

    @Override
    public void Delete(String rsc, WebContext exch) {

    }


    /**
     * A hub that covers orgs.
     */
    public static class OrgControl extends WebControl implements Sub {

        public OrgControl(WebHost host, WebControl parent) {
            super(host, parent);

            // agent functions
//            addSub("shop", OrgControl.class, null);

            // shop functions
            addSub("order", SalesControl.class, null);
            addSub("user", PartyControl.class, null);
            addSub("stocks", StocksControl.class, null);
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
