Dependencies
-------------

1)  Google Web Toolkit (GWT)
    URL: http://code.google.com/webtoolkit/
    
2)  mongoDB
    URL: http://www.mongodb.org/

You also need Java (1.6 or later), Ant, and a servlet container, such as tomcat.


Building
-------------

Copy local.properties.template to local.properties, and edit local.properties to match your file paths.
All properties are well documented. Make sure that you set the property gwt.dir to point to your local installation 
of GWT. Usually, there is no need to touch any of the other properties.

If you want to change the default setup properties for WebProtege, copy the etc/webprotege.properties.template
to war/webprotege.properties, and edit the properties, which are well documented in the file.

To build the webprotege WAR file, run

         ant war
         
This command will create the webprotege.war file in build/webprotege.war. You can either copy this file manually to
your servlet container (e.g. tomcat/webapps), or use "ant deploy" (see below). 


Running
-------------

DevMode can be started with: 

          ant devmode

Note that OSX users must modify the extra.hosted.jvm.arg setting in the local.properties file in order to run the devmode.

Web Protege can also be deployed into into tomcat by running 

          ant deploy

and then starting tomcat. You need to set the CATALINA_HOME environment variable in local.properties.


Documentation
---------------
More documentation at: 
http://protegewiki.stanford.edu/wiki/WebProtege

-------------------------
Last updated: 2013/04/21