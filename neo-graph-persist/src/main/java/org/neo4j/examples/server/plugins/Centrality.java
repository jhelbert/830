/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.neo4j.examples.server.plugins;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphalgo.CostAccumulator;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.CommonEvaluators;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Direction;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Name;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.plugins.PluginTarget;

import org.neo4j.tooling.GlobalGraphOperations;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

@Description( "Expose some centrality algorithms (?)" )
public class Centrality extends ServerPlugin
{
    FloydWarshall<Double> floydWarshall = null;
    @Name( "graph_median" )
    @Description( "Get the median of the graph" )
    @PluginTarget( GraphDatabaseService.class )
    public Node GetGraphMedian( @Source GraphDatabaseService graphDb ) {
        initializeFloydWarshall(graphDb);
        /*
        return GraphMedianAlgo(GlobalGraphOperations.at( graphDb ).getAllNodes());
        */
        return null;
    }

    @Name( "graph_center" )
    @Description( "Get the 'center' of the graph" )
    @PluginTarget( GraphDatabaseService.class )
    public String GetGraphCenter( @Source GraphDatabaseService graphDb ) {
        /*
        initializeFloydWarshall();
        return GraphCenterAlgo(GlobalGraphOperations.at( graphDb ).getAllNodes());
        */
        return "hello graph_center";
    }

    public void initializeFloydWarshall(GraphDatabaseService graphDb) {
        if (floydWarshall == null) {
            floydWarshall = new FloydWarshall<Double>(
                (Double)0.0,(Double)Double.MAX_VALUE,Direction.OUTGOING,
                CommonEvaluators.doubleCostEvaluator("cost"),
                new org.neo4j.graphalgo.impl.util.DoubleAdder(),
                new org.neo4j.graphalgo.impl.util.DoubleComparator(),
                GlobalGraphOperations.at( graphDb ).getAllNodes(),
                GlobalGraphOperations.at( graphDb ).getAllRelationships());
        }
    }

    public Node GraphMedianAlgo(ArrayList<Node> nodeSet, FloydWarshall<Double> floydWarshall) {
        /*
        HashMap<Node, Integer> mediansSum = new HashMap<Node, Integer>();

        // sum up shortest distances from eeach node to every other node
        for ( Node startNode : nodeSet) {
            mediansSum.put(startNode, 0);
            for (Node endNode : nodeSet) {

                // does removing this condition speed it up?  even if this is true, you are only adding the path from startNode to startNode which is 0 and still correct
                if (startNode != endNode) {
                    mediansSum.put(startNode, mediansSum.get(startNode) + floydWarshall.getCost(startNode, endNode));
                }


            }
        }

        // can be speed this up by skipping this step altogether?  making the comparison in the prev loop
        Node bestMedianNode = null;
        for (Node n : mediansSum.keySet()) {
            if (bestMedianNode == null || mediansSum.get(n) < mediansSum.get(bestMedianNode)) {
                bestMedianNode = n;
            }
        }

        return bestMedianNode;
        */
        return null;
    }


    public Node GraphCenterAlgo(ArrayList<Node> nodeSet) {
        /*
        HashMap<Node, Integer> radius = new HashMap<Node, Integer>();
        for ( Node startNode : nodeSet) {
            radius.put(startNode, 0);
            for (Node endNode : nodeSet) {
                if (startNode != endNode && fw.getCost(startNode, endNode) > radius.get(startNode) ) {
                    radius.put(startNode, fw.getCost(startNode, endNode));
                }
            }
        }

        Node bestCenterNode = null;
        for (Node n : radius.keySet()) {
            if (bestCenterNode == null || radius.get(n) < radius.get(bestCenterNode)) {
                bestCenterNode = n;
            }
        }
        return bestCenterNode;
        */
        return null;
    }
}

class FloydWarshall<CostType>
{
    protected CostType startCost; // starting cost for all nodes
    protected CostType infinitelyBad; // starting value for calculation
    protected Direction relationDirection;
    protected CostEvaluator<CostType> costEvaluator = null;
    protected CostAccumulator<CostType> costAccumulator = null;
    protected Comparator<CostType> costComparator = null;
    protected Iterable<Node> nodeSet;
    protected Iterable<Relationship> relationshipSet;
    CostType[][] costMatrix;
    Integer[][] predecessors;
    Map<Node,Integer> nodeIndexes; // node ->index
    Node[] IndexedNodes; // index -> node
    protected boolean doneCalculation = false;

    /**
     * @param startCost
     *            The cost for just starting (or ending) a path in a node.
     * @param infinitelyBad
     *            A cost worse than all others. This is used to initialize the
     *            distance matrix.
     * @param costRelationType
     *            The relationship type to traverse.
     * @param relationDirection
     *            The direction in which the paths should follow the
     *            relationships.
     * @param costEvaluator
     * @see {@link CostEvaluator}
     * @param costAccumulator
     * @see {@link CostAccumulator}
     * @param costComparator
     * @see {@link CostAccumulator} or {@link CostEvaluator}
     * @param nodeSet
     *            The set of nodes the calculation should be run on.
     * @param relationshipSet
     *            The set of relationships that should be processed.
     */
    public FloydWarshall( CostType startCost, CostType infinitelyBad,
        Direction relationDirection, CostEvaluator<CostType> costEvaluator,
        CostAccumulator<CostType> costAccumulator,
        Comparator<CostType> costComparator, Iterable<Node> nodeSet,
        Iterable<Relationship> relationshipSet )
    {
        super();
        this.startCost = startCost;
        this.infinitelyBad = infinitelyBad;
        this.relationDirection = relationDirection;
        this.costEvaluator = costEvaluator;
        this.costAccumulator = costAccumulator;
        this.costComparator = costComparator;
        this.nodeSet = nodeSet;
        this.relationshipSet = relationshipSet;
    }

    /**
     * This resets the calculation if we for some reason would like to redo it.
     */
    public void reset()
    {
        doneCalculation = false;
    }

    /**
     * Internal calculate method that will do the calculation. This can however
     * be called externally to manually trigger the calculation.
     */
    @SuppressWarnings( "unchecked" )
    public void calculate()
    {
        // Don't do it more than once
        if ( doneCalculation )
        {
            return;
        }
        doneCalculation = true;
        // Build initial matrix
        //int n = nodeSet.iterator().size();
        int n = 0; // TODO: actually get the true size of ndoeSet.
        costMatrix = (CostType[][]) new Object[n][n];
        predecessors = new Integer[n][n];
        IndexedNodes = new Node[n];
        nodeIndexes = new HashMap<Node,Integer>();
        for ( int i = 0; i < n; ++i )
        {
            for ( int j = 0; j < n; ++j )
            {
                costMatrix[i][j] = infinitelyBad;
            }
            costMatrix[i][i] = startCost;
        }
        int nodeIndex = 0;
        for ( Node node : nodeSet )
        {
            nodeIndexes.put( node, nodeIndex );
            IndexedNodes[nodeIndex] = node;
            ++nodeIndex;
        }
        // Put the relationships in there
        for ( Relationship relationship : relationshipSet )
        {
            Integer i1 = nodeIndexes.get( relationship.getStartNode() );
            Integer i2 = nodeIndexes.get( relationship.getEndNode() );
            if ( i1 == null || i2 == null )
            {
                // TODO: what to do here? pretend nothing happened? cast
                // exception?
                continue;
            }
            if ( relationDirection.equals( Direction.BOTH )
                || relationDirection.equals( Direction.OUTGOING ) )
            {
                costMatrix[i1][i2] = costEvaluator
.getCost( relationship,
                        Direction.OUTGOING );
                predecessors[i1][i2] = i1;
            }
            if ( relationDirection.equals( Direction.BOTH )
                || relationDirection.equals( Direction.INCOMING ) )
            {
                costMatrix[i2][i1] = costEvaluator.getCost( relationship,
                        Direction.INCOMING );
                predecessors[i2][i1] = i2;
            }
        }
        // Do it!
        for ( int v = 0; v < n; ++v )
        {
            for ( int i = 0; i < n; ++i )
            {
                for ( int j = 0; j < n; ++j )
                {
                    CostType alternative = costAccumulator.addCosts(
                        costMatrix[i][v], costMatrix[v][j] );
                    if ( costComparator.compare( costMatrix[i][j], alternative ) > 0 )
                    {
                        costMatrix[i][j] = alternative;
                        predecessors[i][j] = predecessors[v][j];
                    }
                }
            }
        }
        // TODO: detect negative cycles?
    }

    /**
     * This returns the cost for the shortest path between two nodes.
     * @param node1
     *            The start node.
     * @param node2
     *            The end node.
     * @return The cost for the shortest path.
     */
    public CostType getCost( Node node1, Node node2 )
    {
        calculate();
        return costMatrix[nodeIndexes.get( node1 )][nodeIndexes.get( node2 )];
    }

    /**
     * This returns the shortest path between two nodes as list of nodes.
     * @param startNode
     *            The start node.
     * @param targetNode
     *            The end node.
     * @return The shortest path as a list of nodes.
     */
    public List<Node> getPath( Node startNode, Node targetNode )
    {
        calculate();
        LinkedList<Node> path = new LinkedList<Node>();
        int index = nodeIndexes.get( targetNode );
        int startIndex = nodeIndexes.get( startNode );
        Node n = targetNode;
        while ( !n.equals( startNode ) )
        {
            path.addFirst( n );
            index = predecessors[startIndex][index];
            n = IndexedNodes[index];
        }
        path.addFirst( n );
        return path;
    }
}

