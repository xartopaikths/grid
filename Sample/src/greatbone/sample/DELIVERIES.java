package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * A transient dataset that keep tracts of delivery tasks.
 */
@Copy
public class DELIVERIES extends GridDataSet<Delivery> {

    public DELIVERIES(GridUtility grid) {
        super(grid, 12);
    }

}
