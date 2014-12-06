Setup notes

These were done a fresh install of Ubuntu 14.04 with openjdk 7.

    $ git clone git@github.com:jhelbert/830.git
    
    $ sudo apt-get install graphviz nodejs make maven openjdk-7-jdk

    $ cd neo4j-master/community/licensecheck-config

    $ mvn install

    $ cd ../server

    $ mvn clean package -P initial-build

There might be some jasmine spec errors

This initial build will fetch a lot of Java packages and
then compile them all, so be prepared for it to take on the
order of 10s of minutes.

    $ mvn exec:java


