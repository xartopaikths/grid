package greatbone.sample.mgt;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.TRANSACTS;
import greatbone.sample.Transact;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 * The menu management handler.
 */
public class TransactsControl extends WebControl {

    final TRANSACTS transacts;

    public TransactsControl(WebHost host, WebControl parent) {
        super(host, parent);

         transacts = GridUtility.getCache(TRANSACTS.class);

    }

    @Override
    public void index(WebContext exch) {


    }

    public void Post(WebContext exch) {

        // create and insert

        Transact _new = new Transact();
        if (true) {
//            _new.load(exch);
        }  else {
            // explicitly set fields
            _new.setId("");
            _new.setText("asfsdfsadf");

        }

        transacts.put(_new) ; // set autogen id during the put

    }

}
