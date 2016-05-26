package io.greatbone.sample;

import io.greatbone.grid.GridUtility;
import io.greatbone.web.WebUtility;
import io.greatbone.sample.mgt.AdminVHost;
import io.greatbone.sample.op.RegionVHost;

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
            WebUtility.addVirtualHost("mgt", AdminVHost.class, null).start();
            WebUtility.addVirtualHost("op", RegionVHost.class, null).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
