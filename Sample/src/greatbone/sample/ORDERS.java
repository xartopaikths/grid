package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridRecordCache;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Table;

/**
 * A dataset of sales orders.
 */
@Table
@Copy
public class ORDERS extends GridRecordCache<Order> {

    public ORDERS(GridUtility grid) {
        super(grid, 12);
    }

}
