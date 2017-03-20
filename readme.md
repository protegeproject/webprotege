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

1)  mongoDB
    URL: http://www.mongodb.org/

You also need Java (1.6 or later), Maven, and a servlet container, such as tomcat.


Building WebProtege for Use on Local Host
-----------------------------------------

Open a terminal in the webprotege directory and type

    mvn package

WebProtege will be built as a .war file in the ```target``` directory.  By default, the WebProtege data directory
(where WebProtege stores project data etc.) is ```/data/webprotege```.  If you want to change this location you
can specify a different location at build time using ```-Ddata.directory=<YOURPATH>```.  For example,

    mvn -Ddata.directory=/mypath/mydirectory package

or on Windows

    mvn -Ddata.directory=C:\mypath\mydirectory package

```data.directory``` is a configuration property.  You may want to change some of the other WebProtege configuration
properties.  You can do this when building by specifying the relevant property as a command line argument as above.

For a detailed list of supported properties please see ```webprotege.properties```

Running
-------

DevMode can be started with:

    mvn gwt:run

Note that, if you need to override any default property values then you must specify these as arguments
(otherwise default values will be used).  For example,

    mvn -Ddata.directory=/mypath/mydirectory gwt:run

WebProtege can also be deployed into into tomcat by copying the .war file to the webapps folder in your tomcat
installation.

Note: you need mongodb to run WebProtege. Please make sure mongodb is running. Find more information at:
http://protegewiki.stanford.edu/wiki/WebProtegeAdminGuide#Install_mongoDB


Building WebProtege for Deployment on your Servers
--------------------------------------------------

The above steps build WebProtege for local testing on your machine (localhost).  For deployment to another machine for
production use you should active the ```deployment``` profile.  To do this include ```-Pdeployment``` as an argument.
You MUST also include a value for the property ```application.hostSupplier```, which is the domain nameSupplier where you will deploy
WebProtege.  If you do not include this property value the build will fail with an error message.  For example,

    mvn -Pdeployment -Dapplication.hostSupplier=mycompany.com package

Configuration
-------------

WebProtege can be configured and customized in different ways, e.g. UI layout, email, etc.
Please find more information here:
http://protegewiki.stanford.edu/wiki/WebProtegeAdminGuide#Configuration_and_Customization_.28optional.29


Documentation
-------------
More documentation at:
http://protegewiki.stanford.edu/wiki/WebProtege
