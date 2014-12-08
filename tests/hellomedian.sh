#!/bin/bash

KEY=$(cat auth.key)
URI=http://neo4j:$KEY@localhost:7474

curl -X POST \
  $URI/db/data/ext/Centrality/graphdb/graph_median

echo ''
