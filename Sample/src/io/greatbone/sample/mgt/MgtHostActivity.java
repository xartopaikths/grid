package io.greatbone.sample.mgt;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebHostActivity;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 * The management subdomain & module. [mgt.company.com]
 */
public class MgtHostActivity extends WebHostActivity {

    public MgtHostActivity(WebUtility web, String key) {
        super(web, key);

        addSub("users", UsersActivity.class, (x, y)-> y.check(x, 0));
        addSub("agents", AgentsActivity.class, null);
        addSub("transacts", TransactsActivity.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

}
