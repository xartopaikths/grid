package io.greatbone.sample.mgt;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.ORGS;
import io.greatbone.sample.Org;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebActivity;
import io.greatbone.web.WebHostActivity;

/**
 */
public class AgentsActivity extends WebActivity {

    final ORGS shops;

    public AgentsActivity(WebHostActivity host, WebActivity parent) {
        super(host, parent);

        this.shops = GridUtility.getCache(ORGS.class);
    }

    public void default_(WebContext wc) {
        String st = wc.qstring("status");
        if (st.equals("")) {

        }
    }

    public void Get(String rsc, WebContext wc) throws Exception {
        Org ret = shops.get(rsc);
        if (ret == null) {
            wc.sendNotFound();
        } else {
            // send back json
            wc.sendOK(ret);
        }
    }

    public void Post(WebContext wc) {
        Org agent = shops.newData();

        wc.content(agent);

        shops.put(agent);
    }

}
