package io.greatbone.sample.op;

import io.greatbone.sample.HTML;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebUtility;
import io.greatbone.web.WebHost;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class OperationHost extends WebHost {

    public OperationHost(WebUtility web, String key) {
        super(web, key);

        addSub("orders", OrderService.class, null);
        addSub("persons", PersonService.class, null);
        addSub("stocks", SettingService.class, null);
        addSub("notice", NoticeService.class, null);
    }

    @Override
    public void Get(WebContext wc) throws IOException {
        wc.sendOK(new HTML() {
            protected void body() throws IOException {
                $("another html output");
            }
        });
    }

}
