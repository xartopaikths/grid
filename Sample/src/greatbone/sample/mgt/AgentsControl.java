package greatbone.sample.mgt;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.PARTIES;
import greatbone.sample.Party;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 */
public class AgentsControl extends WebControl {

    final PARTIES parties;

    public AgentsControl(WebHost host, WebControl parent) {
        super(host, parent);

        this.parties = GridUtility.getDataSet(PARTIES.class);
    }

    public void Get(WebContext wc) {
        String st = wc.qstring("status");
        if (st.equals("")) {

        }
    }

    public void Get(String rsc, WebContext wc) throws Exception {
        Party ret = parties.getData(rsc);
        if (ret == null) {
            wc.sendNotFound();
        } else {
            // send back json
            wc.sendOK(ret);
        }
    }

    public void Post(WebContext wc) {
        Party agent = parties.newData();

        wc.content(agent);

        parties.put(null, agent);
    }

}
