package io.greatbone.sample;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.admin.AdminVirtualHost;
import io.greatbone.sample.op.OpVirtualHost;
import io.greatbone.web.WebUtility;

import java.io.IOException;

/**
 */
public class Application {

    public static void main(String[] args) {

        try {
            GridUtility.initialize(
                    USERS.class,
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
            WebUtility.addVirtualHost("admin", AdminVirtualHost.class, null).start();
            WebUtility.addVirtualHost("region", OpVirtualHost.class, null).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
