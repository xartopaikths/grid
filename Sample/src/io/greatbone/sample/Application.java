package io.greatbone.sample;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.mgt.ManagementVirtualHost;
import io.greatbone.sample.op.OperationVirtualHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 */
public class Application {

    public static void main(String[] args) {

        try {
            GridUtility.initialize(
                    STAFFERS.class,
                    SHOPS.class,
                    CLIENTS.class,
                    ORDERS.class,
                    DELIVERIES.class,
                    TRANSACTS.class,
                    CLIPS.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start virtual hosts
        try {
            WebUtility.addVirtualHost("op", OperationVirtualHost.class, null).start();
            WebUtility.addVirtualHost("admin", ManagementVirtualHost.class, null).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
