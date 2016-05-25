package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridDataCache;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Storage;

/**
 * All shop participants of the business operation.
 */
@Storage
@Copy
public class SHOPS extends GridDataCache<Shop> {

    public SHOPS(GridUtility grid) {
        super(grid, 12);
    }

}
