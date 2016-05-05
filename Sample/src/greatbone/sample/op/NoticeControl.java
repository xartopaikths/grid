package greatbone.sample.op;

import greatbone.sample.Party;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebHost;

/**
 * The menu management handler.
 */
public class NoticeControl extends WebControl implements Runnable {

    public NoticeControl(WebHost host, WebControl parent) {
        super(host, parent);
    }

    @Override
    public void _(WebContext wc) {
        Party party = (Party) wc.space();

    }

    public void Post(WebContext exch) {

//        String from = exch.getHeader("Dk-From");

        Party party = (Party) exch.space();

        // parse msg


    }

    @Override
    public void run() {

    }

}
