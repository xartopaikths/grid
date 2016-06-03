package io.greatbone.sample;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.mgt.ManagementHost;
import io.greatbone.sample.op.OperationHost;
import io.greatbone.web.WebHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 */
public class Application {

    public static void main(String[] args) {

        try {
            GridUtility.setup(
                    STAFFERS.class,
                    ORGS.class,
                    PERSONS.class,
                    ORDERS.class,
                    TASKS.class,
                    TRANSACTS.class,
                    CLIPS.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start virtual hosts
        try {
            WebHost op = WebUtility.addHost("op", OperationHost.class,
                    null
            );
            op.start();
            WebHost mgt = WebUtility.addHost("mgt", ManagementHost.class,
                    wc -> true
            );
            mgt.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
