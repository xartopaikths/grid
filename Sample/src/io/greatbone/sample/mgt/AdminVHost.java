package io.greatbone.sample.mgt;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebVHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 * The management subdomain & module. [mgt.company.com]
 */
public class AdminVHost extends WebVHost {

    public AdminVHost(WebUtility web, String key) {
        super(web, key);

        addSub("users", UsersControl.class, null);
        addSub("agents", AgentsControl.class, null);
        addSub("event", TransactsControl.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

}
