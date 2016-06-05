package io.greatbone.sample.mgt;

import io.greatbone.sample.HTML;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 * The management subdomain & module. [mgt.company.com]
 */
public class MgtHost extends WebHost {

    public MgtHost(WebUtility web, String key) {
        super(web, key);

        addSub("users", UserService.class, null);
        addSub("agents", AgentService.class, null);
        addSub("transacts", TransactService.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK(new HTML() {
            @Override
            protected void body() {
                $("this is html");
            }
        });
    }

}
