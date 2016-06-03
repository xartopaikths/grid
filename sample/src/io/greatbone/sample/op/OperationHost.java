package io.greatbone.sample.op;

import io.greatbone.sample.HTML;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class OperationHost extends WebHost {

    public OperationHost(WebUtility web, String key) {
        super(web, key);

        addSub("order", OrderService.class, null);
        addSub("person", PersonService.class, null);
        addSub("setting", SettingService.class, null);
        addSub("notice", NoticeService.class, null);

        setHub(OrgServiceHub.class,
                wc -> false
        );
    }

    @Override
    public void _(WebContext wc) throws IOException {
        wc.sendOK(new HTML() {
            protected void body() throws IOException {
                $("another html output");
            }
        });
    }

}
