package io.greatbone.sample;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.mgt.ManagementHost;
import io.greatbone.sample.op.OperationHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 */
public class Application {

    public static void main(String[] args) {

        try {
            GridUtility.initialize(
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
            WebUtility.addHost("op", OperationHost.class, null).start();
            WebUtility.addHost("admin", ManagementHost.class, null).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
