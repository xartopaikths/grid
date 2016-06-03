package io.greatbone.sample.mgt;

import io.greatbone.sample.HTML;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 * The management subdomain & module. [mgt.company.com]
 */
public class ManagementHost extends WebHost {

    public ManagementHost(WebUtility web, String key) {
        super(web, key);

        addSub("users", UserService.class, null);
        addSub("agents", AgentService.class, null);
        addSub("transacts", TransactService.class, null);
    }

    @Override
    public void Get(WebContext wc) throws IOException {
        wc.sendOK(new HTML() {
            @Override
            protected void body() throws IOException {
                $("this is html");
            }
        });
    }

}
