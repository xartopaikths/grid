package greatbone.framework.grid;

/**
 * The partitioning is based on integral key ranges
 */
public abstract class GridRangedDataSet<D extends GridData<D>> extends GridDataSet<D> {

    protected GridRangedDataSet(GridUtility parent) {
        super(parent, 16);
    }

    @Override
    GridAbstractPage<D> shard(String key) {


        return null;
    }


}
