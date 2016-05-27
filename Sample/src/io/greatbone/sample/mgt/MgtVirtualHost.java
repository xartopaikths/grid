package io.greatbone.sample.mgt;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebVirtualHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 * The management subdomain & module. [mgt.company.com]
 */
public class MgtVirtualHost extends WebVirtualHost {

    public MgtVirtualHost(WebUtility web, String key) {
        super(web, key);

        addSub("users", UsersControl.class, (x, y)-> y.check(x, 0));
        addSub("agents", AgentsControl.class, null);
        addSub("transacts", TransactsControl.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

}
