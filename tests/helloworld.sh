#!/bin/bash

KEY=$(cat auth.key)
URI=http://neo4j:$KEY@localhost:7474

curl -X POST \
  $URI/db/data/ext/HelloWorldWideWebb/graphdb/hello_world_wide_webb

echo '\n'
