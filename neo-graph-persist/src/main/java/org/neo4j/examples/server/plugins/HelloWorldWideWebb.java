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
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.server.plugins.Description;
import org.neo4j.server.plugins.Name;
import org.neo4j.server.plugins.PluginTarget;
import org.neo4j.server.plugins.ServerPlugin;
import org.neo4j.server.plugins.Source;
import org.neo4j.tooling.GlobalGraphOperations;

// START SNIPPET: HelloWorldWideWebb
@Description( "An extension to the Neo4j Server for printing Hello World from WWWebb" )
public class HelloWorldWideWebb extends ServerPlugin
{
    private int counter = 0;

    @Name( "hello_world_wide_webb" )
    @Description( "Retrieve a friendly greeting from your project partner :)." )
    @PluginTarget( GraphDatabaseService.class )
    public Iterable<String> getHello( @Source GraphDatabaseService graphDb )
    {
	  counter++;
        ArrayList<String> s = new ArrayList<String>();
        s.add("Hello");
        s.add("from");
        s.add("the");
        s.add("world");
        s.add("wide");
        s.add("webb");
	  s.add("counter:");
	  s.add(Integer.toString(counter));
        return s;
    }
}
// END SNIPPET: HelloWorldWideWebb
