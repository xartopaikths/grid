package io.greatbone.sample.op;

import io.greatbone.web.*;
import io.greatbone.sample.Shop;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class RegionVHost extends WebVHost {

    public RegionVHost(WebUtility web, String key) {
        super(web, key);

        setHub(SoftControlSet.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

    /**
     * A hub that covers orgs.
     */
    public static class SoftControlSet extends WebControl implements ControlSet {

        public SoftControlSet(WebVHost vhost, WebControl parent) {
            super(vhost, parent);

            // agent functions
//            addSub("shop", OrgControl.class, null);

            // shop functions
            addSub("sales", OrdersControl.class, null);
            addSub("user", ClientsControl.class, null);
            addSub("stocks", SettingsControl.class, null);
            addSub("notice", NoticeControl.class, null);
        }

        @Override
        public WebControl locateSub(String key, WebContext wc) {
            Shop shop = null;
            if (shop != null) {
                wc.setSpace(shop);
                return this;
            }
            return null;
        }

    }


}
