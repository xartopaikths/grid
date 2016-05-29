package io.greatbone.sample.op;

import io.greatbone.web.WebContext;
import io.greatbone.web.WebUtility;
import io.greatbone.web.WebHostActivity;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class OpHostActivity extends WebHostActivity {

    public OpHostActivity(WebUtility web, String key) {
        super(web, key);

        addSub("orders", OrdersActivity.class, null);
        addSub("persons", PersonsActivity.class, null);
        addSub("stocks", SettingsActivity.class, null);
        addSub("notice", NoticeActivity.class, null);
    }

    @Override
    public void default_(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

}
