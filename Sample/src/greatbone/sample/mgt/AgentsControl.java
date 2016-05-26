package greatbone.sample.mgt;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.SHOPS;
import greatbone.sample.Shop;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebVirtualHost;

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
