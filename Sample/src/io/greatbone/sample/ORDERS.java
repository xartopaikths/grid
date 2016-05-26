package io.greatbone.sample;

import io.greatbone.grid.Copy;
import io.greatbone.grid.GridDataCache;
import io.greatbone.grid.GridUtility;
import io.greatbone.grid.Storage;

/**
 * A dataset of sales orders.
 */
@Storage
@Copy
public class ORDERS extends GridDataCache<Order> {

    public ORDERS(GridUtility grid) {
        super(grid, 12);
    }

}
