package io.greatbone.sample.mgt;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.TRANSACTS;
import io.greatbone.sample.Transact;
import io.greatbone.web.WebContext;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebParent;
import io.greatbone.web.WebService;

/**
 * The menu management handler.
 */
public class TransactService extends WebService {

    final TRANSACTS transacts;

    public TransactService(WebHost host, WebParent parent) {
        super(host, parent);

        transacts = GridUtility.getCache(TRANSACTS.class);

    }

    @Override
    public void default_(WebContext wc) {


    }

    public void Post(WebContext exch) {

        // create and insert

        Transact _new = new Transact();
        if (true) {
//            _new.load(exch);
        } else {
            // explicitly set fields
            _new.setId("");
            _new.setText("asfsdfsadf");

        }

        transacts.put(_new); // set autogen id during the put

    }

}
