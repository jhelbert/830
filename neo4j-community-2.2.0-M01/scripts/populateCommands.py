#!/usr/bin/python
from sets import Set

i=0
TRANSACTION_LENGTH = 30000

def endTransaction():
  print 'COMMIT'
  print 'BEGIN'

nodeList = Set([])
with open("data/com-amazon.ungraph1klines.txt","r") as data:
#with open("small.txt","r") as data:
  for line in data:
    if '#' not in line:
	nodes=line.strip().split('\t')
	nodeList.add(nodes[0])
	nodeList.add(nodes[1])

print 'BEGIN'

for node in nodeList:
  i+=1
  if i==TRANSACTION_LENGTH:
    i=0
    endTransaction()
  print 'CREATE (n {id:'+node+'});'

with open("data/com-amazon.ungraph1klines.txt","r") as data:
#with open("small.txt","r") as data:
  for line in data:
    if '#' not in line:
	i+=1
	if i==TRANSACTION_LENGTH:
	  i=0
	  endTransaction()
	nodes=line.strip().split('\t')
	print 'MATCH (from {id:'+nodes[0]+'}), (to {id:'+nodes[1]+'}) CREATE from-[:CONNECTED_TO {cost:1}]->to;'
print 'COMMIT'

