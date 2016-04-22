# Features

- supoort datadets and filesets, based on database and filesystem respectively.
- Load active data into memory data grid
- distributed, lock-free get(), put() search()
- Automatically map to SQL database tables.
- Support TB memory each node. data are placed in off-heap memory, avoid GC hassels when massive data records are engaged
- minimal object creation during aggregation
- configurable partitioning rules, that effectively eliminates use of indexes on datasets
- failover & fault-tolerance server cluster
- transaction support

# Version 2.x Roadmap

- Distributed Locks
- New WebAPI based on Undertow 2
- Runtime Partitioning Management



