package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * All shop participants of the business operation.
 */
public class SHOPS extends GridDataSet<Party> {

    public SHOPS(GridUtility grid) {
        super(grid, 12);
    }

}
