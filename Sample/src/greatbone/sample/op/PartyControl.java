package greatbone.sample.op;

import greatbone.sample.Party;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebHost;

/**
 * The order management handler.
 */
public class PartyControl extends WebControl {


    public PartyControl(WebHost service, WebControl parent) {
        super(service, parent);

    }

    @Override
    public void Get(WebContext exch) {
        Party party = (Party)exch.space()  ;


    }
}
