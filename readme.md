WebProtégé
==========

What is WebProtégé?
-------------------

WebProtégé is a free, open source collaborative ontology development environment.

It provides the following features:
- Support for editing OWL 2 ontologies
- A default simple editing interface, which provides access to commonly used OWL constructs
- Full change tracking and revision history
- Collaboration tools such as, sharing and permissions, threaded notes and discussions, watches and email notifications
- Customizable user interface
- Support for editing OBO ontologies
- Multiple file formats for upload and download of ontologies (supported formats: RDF/XML, Turtle, OWL/XML, OBO, and others)

WebProtégé runs as a Web application. End users access it through their Web browsers.
They do not need to download or install any software. We encourage end-users to use

https://webprotege.stanford.edu

If you have downloaded the webprotege war file from GitHub, and would like to deploy it on your own server,
please follow the instructions at:

https://github.com/protegeproject/webprotege/wiki/WebProtégé-3.0.0-Installation

Building
--------

To build WebProtégé from source

1) Clone the github repository
   ```git clone https://github.com/protegeproject/webprotege.git```
2) Open a terminal in the directory where you clone the repository to
3) Use maven to package WebProtégé
```mvn package```
5) The WebProtege .war file will be built into the webprotege-server directory

Running from Maven
------------------

To run WebProtégé in SuperDev Mode using maven

1) Start the GWT code server in one terminal window
    ```mvn gwt:codeserver```
2) In a different terminal window start the tomcat server
    ```mvn -Denv=dev tomcat7:run```
3) Browse to WebProtégé in a Web browser by navigating to ```http://localhost:8080```
