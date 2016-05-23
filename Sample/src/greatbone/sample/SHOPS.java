package greatbone.sample;

import greatbone.framework.grid.Copy;
import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Storage;

/**
 * All shop participants of the business operation.
 */
@Storage
@Copy
public class SHOPS extends GridDataSet<Shop> {

    public SHOPS(GridUtility grid) {
        super(grid, 12);
    }

}
