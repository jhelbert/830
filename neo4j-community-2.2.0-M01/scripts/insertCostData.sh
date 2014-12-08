python populateCommandsWithCosts.py > cypherCommands.txt
cat cypherCommands.txt | ../bin/neo4j-shell -config ../conf/neo4j.properties -path ../data/graph.db 
