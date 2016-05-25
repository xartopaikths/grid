package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridDataCache;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Storage;

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
