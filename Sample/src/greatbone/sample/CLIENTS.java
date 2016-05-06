package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.Table;

/**
 * All client participants of the business operation that can be either a customer or a delivery person.
 */

@Table
public class CLIENTS extends GridDataSet<Shop> {

    public CLIENTS(GridUtility grid) {
        super(grid, 12);
    }

}
