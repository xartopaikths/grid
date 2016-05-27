package io.greatbone.sample.mgt;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.SHOPS;
import io.greatbone.sample.Shop;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebControl;
import io.greatbone.web.WebVirtualHost;

/**
 */
public class AgentsControl extends WebControl {

    final SHOPS shops;

    public AgentsControl(WebVirtualHost host, WebControl parent) {
        super(host, parent);

        this.shops = GridUtility.getCache(SHOPS.class);
    }

    public void default_(WebContext wc) {
        String st = wc.qstring("status");
        if (st.equals("")) {

        }
    }

    public void Get(String rsc, WebContext wc) throws Exception {
        Shop ret = shops.get(rsc);
        if (ret == null) {
            wc.sendNotFound();
        } else {
            // send back json
            wc.sendOK(ret);
        }
    }

    public void Post(WebContext wc) {
        Shop agent = shops.newData();

        wc.content(agent);

        shops.put(agent);
    }

}
