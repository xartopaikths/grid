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

        addSub("users", UsersActivity.class, (x, y)-> y.check(x, 0));
        addSub("agents", AgentsActivity.class, null);
        addSub("transacts", TransactsActivity.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK(new HTML() {
            @Override
            protected void body() throws IOException {
                $("this is html");
            }
        });
    }

}
