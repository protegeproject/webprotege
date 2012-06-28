Dependencies
-------------

1). Google Web Toolkit (GWT)
    URL: http://code.google.com/webtoolkit/
    Version: 2.2.0


Running
-------------

Set the CATALINA_HOME and PROTEGE_HOME environment variables and
update the local.properties to point to the location of GWT and
GWT-Ext.  It is also possible to set the cataline.home and
protege.home in the local.properties file.  Hosted mode can then be
started with 

          ant run.server
          ant hosted.

Note that os x users must modify the extra.hosted.jvm.arg setting in
the local.properties file in order to run the hosted mode.

Web Protege can also be installed into tomcat by running 

          ant deploy

and then starting tomcat.  Note that protege and the various plugins
(protege-owl, standard-extensions, chat, rdf, collaborative protege
and change-management must be up to date in ${PROTEGE_HOME}.  When
running from sources this can be arranged very quickly with an ant
update command in each of the source trees.
