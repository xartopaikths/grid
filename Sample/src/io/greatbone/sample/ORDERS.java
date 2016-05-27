package io.greatbone.sample;

import io.greatbone.grid.GridDataCache;
import io.greatbone.grid.GridUtility;
import io.greatbone.grid.Storage;

/**
 * A dataset of sales orders.
 */
@Storage
public class ORDERS extends GridDataCache<Order> {

    public ORDERS(GridUtility grid) {
        super(grid, 12);
    }

}
