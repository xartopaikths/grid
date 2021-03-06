package io.greatbone.sample;

import io.greatbone.grid.Copy;
import io.greatbone.grid.GridDataCache;
import io.greatbone.grid.GridUtility;
import io.greatbone.grid.Storage;

/**
 * All shop participants of the business operation.
 */
@Storage
@Copy
public class ORGS extends GridDataCache<Org> {

    public ORGS(GridUtility grid) {
        super(grid, 12);
    }

}
