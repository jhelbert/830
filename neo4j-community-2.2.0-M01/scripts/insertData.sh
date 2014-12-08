python populateCommands.py > cypherCommands.txt
cat cypherCommands.txt | ../neo4j-enterprise-2.1.5/bin/neo4j-shell -config ../neo4j-enterprise-2.1.5/config/neo4j.properties -path ../neo4j-enterprise-2.1.5/data/graph.db 
