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
            addSub("order", OrdersControl.class, null);
            addSub("user", PartyControl.class, null);
            addSub("item", ItemControl.class, null);
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
