package greatbone.framework.grid;

import org.xnio.StreamConnection;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * A proxy that points to its remote origin page.
 */
class GridPageRef<D extends GridData<D>> extends GridPage<D> {

    // connector to the origin peer
    GridConnector connector;

    // status of origin
    int status;

    GridPageRef(GridDataCache<D> parent, String id, GridConnector connector) {
        super(parent, id);

        this.connector = connector;
    }

    @Override
    public D get(String key) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = this.connector.checkout())) {
            //
            // parent.key
            // id;
            // key
            this.connector.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.connector.checkin(conn);
        }
        return null;
    }

    @Override
    public D put(String key, D dat) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = this.connector.checkout())) {
            //
            // parent.key
            // id;
            // key
            this.connector.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.connector.checkin(conn);
        }
        return null;
    }

    @Override
    public D search(Critera<D> filter) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = this.connector.checkout())) {
            //
            // parent.key
            // id;
            // key

            // serialize the critera object or lambda expression as call body
            ObjectOutputStream out = new ObjectOutputStream(gc.output);
            out.writeObject(filter);
            out.close();

            this.connector.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.connector.checkin(conn);
        }
        return null;
    }

}
