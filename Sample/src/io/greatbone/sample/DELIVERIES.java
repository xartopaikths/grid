package io.greatbone.sample;

import io.greatbone.grid.Copy;
import io.greatbone.grid.GridDataCache;
import io.greatbone.grid.GridUtility;

/**
 * A transient dataset that keep tracts of delivery tasks.
 */
@Copy
public class DELIVERIES extends GridDataCache<Delivery> {

    public DELIVERIES(GridUtility grid) {
        super(grid, 12);
    }

}
