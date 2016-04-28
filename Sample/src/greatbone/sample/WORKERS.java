package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * A platform worker or an platform agent.
 */
public class WORKERS extends GridDataSet<Worker> {

    public WORKERS(GridUtility grid) {
        super(grid, 12);
    }

}
