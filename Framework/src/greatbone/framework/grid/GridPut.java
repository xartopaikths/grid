package greatbone.framework.grid;

import java.util.concurrent.ForkJoinTask;

/**
 * A put operation task that works on a particular page
 */
class GridPut<D extends GridData<D>> extends ForkJoinTask<D> {

    final GridPage<D> page;

    // 0: prepare, 1: commit or rollback
    int phase;

    // return data
    D result;


    GridPut(GridPage<D> page) {
        this.page = page;
    }

    @Override
    public D getRawResult() {
        return result;
    }

    @Override
    protected void setRawResult(D value) {
        this.result = value;
    }

    @Override
    protected boolean exec() {
//        result = page.search(filter);
        return true;
    }

}
