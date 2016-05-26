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
public class USERS extends GridDataCache<User> {

    public USERS(GridUtility grid) {
        super(grid, 12);
    }

}
