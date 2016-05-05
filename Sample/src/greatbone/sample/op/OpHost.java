package greatbone.sample.op;

import greatbone.framework.web.*;
import greatbone.sample.Party;

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
    public void _(WebContext wc) throws IOException {
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
            addSub("sales", SalesControl.class, null);
            addSub("user", PartyControl.class, null);
            addSub("stocks", StocksControl.class, null);
            addSub("notice", NoticeControl.class, null);
        }

        @Override
        public WebControl locate(String key, WebContext wc) {
            Party party = null;
            if (party != null) {
                wc.setSpace(party);
                return this;
            }
            return null;
        }

    }


}
