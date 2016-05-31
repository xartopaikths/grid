package io.greatbone.sample;

import io.greatbone.grid.Copy;
import io.greatbone.grid.GridDataCache;
import io.greatbone.grid.GridUtility;
import io.greatbone.grid.Storage;

/**
 *
 */
@Storage
@Copy
public class STAFFERS extends GridDataCache<Staffer> {

    public STAFFERS(GridUtility grid) {
        super(grid, 12);
    }

}
