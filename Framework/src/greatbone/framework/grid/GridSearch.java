package greatbone.framework.grid;

import java.util.concurrent.ForkJoinTask;

/**
 * A query task that works on a particular page
 */
class GridSearch<R extends GridData<R>> extends ForkJoinTask<R> {

    final GridPage<R> page;

    final Critera<R> filter;

    final boolean ascending;

    // return data
    R result;


    GridSearch(GridPage<R> page, Critera<R> filter, boolean ascending) {
        this.page = page;
        this.filter = filter;
        this.ascending = ascending;
    }

    @Override
    public R getRawResult() {
        return result;
    }

    @Override
    protected void setRawResult(R value) {
        this.result = value;
    }

    @Override
    protected boolean exec() {
        result = page.search(filter);
        return true;
    }

}
