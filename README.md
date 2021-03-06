_plugin: neo-graph-persist_

Plugin setup
------------

To make a HelloWorld plugin, I took the following steps:

1. Append the class name of the new plugin to META-INF:

    $ echo 'org.neo4j.examples.server.plugins.HelloWorldWideWebb' >> \
      neo-graph-persist/src/main/resources/META-INF/services/org.neo4j.server.plugins.ServerPlugin

2. Create the plugin itself. I saved it in
   neo-graph-persist/src/main/java/org/neo4j/examples/server/plugins/HelloWorldWideWebb.java

The next steps are to build the plugin and drop it into neo4j, and test it out
over the REST interface exposed by neo4j.

To build the plugin:

    $ cd neo-graph-persist

    $ mvn clean install

There should now be a jar of the plugins specified in META_INF. We just need to
copy this jar into the plugins directory of an installed neo4j server.

The way I got this to work was to drop them into the plugins directory of a
neo4j enterprise installation:

    $ cp target/neo4j-server-examples-2.2-SNAPSHOT.jar \
      ~/neo4j-enterprise-2.1.5/plugins

Now you can run neo4j with the plugin:

    $ cd ~/neo4j-enterprise-2.1.5
    $ bin/neo4j start

To verify that the plugin installed:

    $ curl -v http://localhost:7474/db/data/

This GET request should return a map with a lot of info about the services that
this neo4j installation offers, including a section called "extensions".

If all went well, there should be an entry in extensions called
"HelloWorldWideWebb", including all of the methods it exposes and the URI from
wihch they should be accessed.

In our case, we can access "hello_world_wide_webb" by seing a POST request to
http://localhost:7474/db/data/ext/HelloWorldWideWebb/graphdb/hello_world_wide_webb:

    $ curl -X POST \
      http://localhost:7474/db/data/ext/HelloWorldWideWebb/graphdb/hello_world_wide_webb

If this is successful, we will get an array back:

    [ "Hello", "from", "the", "world", "wide", "webb" ]


It should be 'straightforward' to modify the hello world plugin to do something
more interesting, like cache intermediate results of graph computations :).

