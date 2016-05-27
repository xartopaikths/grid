package io.greatbone.sample;

import io.greatbone.grid.GridDataCache;
import io.greatbone.grid.GridUtility;
import io.greatbone.grid.Storage;

/**
 * All client participants of the business operation that can be either a customer or a delivery person.
 */

@Storage
public class PERSONS extends GridDataCache<Org> {

    public PERSONS(GridUtility grid) {
        super(grid, 12);
    }

}
