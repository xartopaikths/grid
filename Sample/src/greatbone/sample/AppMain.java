package greatbone.sample;

import greatbone.framework.grid.GridUtility;
import greatbone.framework.web.WebUtility;
import greatbone.sample.mgt.MgtHost;
import greatbone.sample.op.OpHost;

import java.io.IOException;

/**
 */
public class AppMain {

    public static void main(String[] args) {

        try {
            GridUtility.initialize(
                    TRANSACTS.class,
                    SALES.class,
                    SHOPS.class,
                    USERS.class,
                    CLIPS.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start virtual hosts
        try {
            WebUtility.createHost("mgt", MgtHost.class, null)
                    .start();
            WebUtility.createHost("op", OpHost.class, null)
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
