package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 *
 */
public class ORDERS extends GridDataSet<Order> {

    public ORDERS(GridUtility grid) {
        super(grid, 12);
    }

}
