package io.greatbone.sample.op;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebUtility;
import io.greatbone.web.WebVirtualHost;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class OperationVirtualHost extends WebVirtualHost {

    public OperationVirtualHost(WebUtility web, String key) {
        super(web, key);

        addSub("sales", OrdersControl.class, null);
        addSub("user", ClientsControl.class, null);
        addSub("stocks", SettingsControl.class, null);
        addSub("notice", NoticeControl.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

}
