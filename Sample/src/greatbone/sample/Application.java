package greatbone.sample;

import greatbone.framework.grid.GridUtility;
import greatbone.framework.web.WebUtility;
import greatbone.sample.mgt.MgtHost;
import greatbone.sample.op.OperationVirtualHost;

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
            WebUtility.addVirtualHost("mgt", MgtHost.class, null).start();
            WebUtility.addVirtualHost("op", OperationVirtualHost.class, null).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
