package greatbone.sample.mgt;

import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebVirtualHost;
import greatbone.framework.web.WebUtility;

import java.io.IOException;

/**
 * The management subdomain & module. [mgt.company.com]
 */
public class MgtHost extends WebVirtualHost {

    public MgtHost(WebUtility web, String key) {
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
