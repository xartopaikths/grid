package io.greatbone.sample;

import io.greatbone.grid.GridUtility;
import io.greatbone.sample.mgt.MgtHostActivity;
import io.greatbone.sample.op.OpHostActivity;
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
            WebUtility.addVirtualHost("op", OpHostActivity.class, null).start();
            WebUtility.addVirtualHost("admin", MgtHostActivity.class, null).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
