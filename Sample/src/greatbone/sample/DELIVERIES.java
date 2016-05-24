package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridRecordCache;
import greatbone.framework.grid.GridUtility;

/**
 * A transient dataset that keep tracts of delivery tasks.
 */
@Copy
public class DELIVERIES extends GridRecordCache<Delivery> {

    public DELIVERIES(GridUtility grid) {
        super(grid, 12);
    }

}
