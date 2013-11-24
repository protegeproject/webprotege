WebProtégé
==========

What is WebProtégé?
-------------------

WebProtégé is a free, open source collaborative ontology development environment for the Web.

It provides the following features:
- Support for editing OWL 2 ontologies
- A default simple editing interface, which provides access to commonly used OWL constructs
- Full change tracking and revision history
- Collaboration tools such as, sharing and permissions, threaded notes and discussions, watches and email notifications
- Customizable user interface
- Customizable Web forms for application/domain specific editing
- Support for editing OBO ontologies
- Multiple file formats for upload and download of ontologies (supported formats: RDF/XML, Turtle, OWL/XML, OBO, and others)

WebProtégé runs as a Web application. End users access it through their Web browsers. 
They do not need to download nor install any software. We encourage end users to use our hosted solution at:
http://webprotege.stanford.edu 


This README file
----------------

This README file describes how to build and run WebProtege. This file is intended for developers or administrators
who want to use WebProtege locally. 

If you have downloaded the webprotege war file from GitHub, and would like to deploy it on your own server,
please follow the instructions at:
http://protegewiki.stanford.edu/wiki/WebProtegeAdminGuide

If you would like to build WebProtege yourself, or if you are developing a plugin
for WebProtege, please read the information below. 


Dependencies
------------

1)  Google Web Toolkit (GWT)
    URL: http://code.google.com/webtoolkit/
    
2)  mongoDB
    URL: http://www.mongodb.org/

You also need Java (1.6 or later), Ant, and a servlet container, such as tomcat.


Building
--------

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
-------

DevMode can be started with: 

          ant devmode

Note that OSX users must modify the extra.hosted.jvm.arg setting in the local.properties file in order to run the devmode.

Web Protege can also be deployed into into tomcat by running 

          ant deploy

and then starting tomcat. You need to set the CATALINA_HOME environment variable in local.properties.

Note: you need mongodb to run WebProtege. Please make sure mongodb is running. Find more information at:
http://protegewiki.stanford.edu/wiki/WebProtegeAdminGuide#Install_mongoDB


Configuration
-------------

WebProtege can be configured and customized in different ways, e.g. UI layout, email, etc. Please find more information here:
http://protegewiki.stanford.edu/wiki/WebProtegeAdminGuide#Configuration_and_Customization_.28optional.29


Documentation
-------------
More documentation at: 
http://protegewiki.stanford.edu/wiki/WebProtege