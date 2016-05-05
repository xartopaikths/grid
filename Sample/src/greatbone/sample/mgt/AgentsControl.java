package greatbone.sample.mgt;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.SHOPS;
import greatbone.sample.Party;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 */
public class AgentsControl extends WebControl {

    final SHOPS SHOPS;

    public AgentsControl(WebHost host, WebControl parent) {
        super(host, parent);

        this.SHOPS = GridUtility.getDataSet(SHOPS.class);
    }

    public void _(WebContext wc) {
        String st = wc.qstring("status");
        if (st.equals("")) {

        }
    }

    public void Get(String rsc, WebContext wc) throws Exception {
        Party ret = SHOPS.getData(rsc);
        if (ret == null) {
            wc.sendNotFound();
        } else {
            // send back json
            wc.sendOK(ret);
        }
    }

    public void Post(WebContext wc) {
        Party agent = SHOPS.newData();

        wc.content(agent);

        SHOPS.put(null, agent);
    }

}
