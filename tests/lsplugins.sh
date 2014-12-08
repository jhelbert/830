#!/bin/bash

KEY=$(cat auth.key)
URI=http://neo4j:$KEY@localhost:7474

curl -v http://neo4j:$KEY@localhost:7474/db/data/

