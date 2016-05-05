package greatbone.sample.op;

import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;
import greatbone.sample.Party;

/**
 * The order management handler.
 */
public class PartyControl extends WebControl {


    public PartyControl(WebHost host, WebControl parent) {
        super(host, parent);

    }

    @Override
    public void _(WebContext exch) {
        Party party = (Party) exch.space();
    }

}
