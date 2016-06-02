package io.greatbone.sample.mgt;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.ORGS;
import io.greatbone.sample.Org;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebParent;
import io.greatbone.web.WebService;

/**
 */
public class AgentService extends WebService {

    final ORGS shops;

    public AgentService(WebHost host, WebParent parent) {
        super(host, parent);

        this.shops = GridUtility.getCache(ORGS.class);
    }

    public void Get(WebContext wc) {
    }

    public void Get(String rsc, WebContext wc) throws Exception {
        Org ret = shops.get(rsc);
        if (ret == null) {
            wc.sendNotFound();
        } else {
            // send back json
//            wc.sendOK(ret);
        }
    }

    public void Post(WebContext wc) {
        Org agent = shops.newData();

        wc.content(agent);

        shops.put(agent);
    }

}
