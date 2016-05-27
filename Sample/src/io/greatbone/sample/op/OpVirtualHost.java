package io.greatbone.sample.op;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebUtility;
import io.greatbone.web.WebVirtualHost;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class OpVirtualHost extends WebVirtualHost {

    public OpVirtualHost(WebUtility web, String key) {
        super(web, key);

        addSub("orders", OrdersControl.class, null);
        addSub("persons", PersonsControl.class, null);
        addSub("stocks", SettingsControl.class, null);
        addSub("notice", NoticeControl.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

}
