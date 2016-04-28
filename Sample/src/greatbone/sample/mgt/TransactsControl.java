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

         transacts = GridUtility.getDataSet(TRANSACTS.class);

    }

    @Override
    public void Get(WebContext exch) {


    }

    @Override
    public void Get(String rsc, WebContext exch) throws Exception {

        int key = Integer.parseInt(rsc);
//        events.partition(key);
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

        transacts.put(null,_new) ; // set autogen id during the put

    }

}
