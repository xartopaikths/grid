package zerotek;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * The ordinary users dataset, whose SHOP column value is false, related to the underlying database view "users"
 */
public class USERS extends GridDataSet<Party> {

    public USERS(GridUtility grid) {
        super(grid, 12);
    }

}
