# Features

- A data grid is made up of distributed dataets and filesets (pertaining to the underlying relational database and filesystem, respectively), that are partitioned across the cluster's member nodes.
- Load active data into memory data grid
- distributed, lock-free get(), put() search()
- Automatically map to SQL database tables.
- Off-heap data page and cursor data structure avoid object GC hassels when massive data records are engaged
- minimal object creation during aggregation
- configurable partitioning rules, that effectively eliminates use of indexes on datasets
- failover & fault-tolerance server cluster
- transaction support

# Version 2.x Roadmap

- Distributed Locks
- New WebAPI based on Undertow 2
- Runtime Partitioning Management



