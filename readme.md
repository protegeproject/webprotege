WebProtégé
==========

PLEASE NOTE
===========

**This repository is in the process of being superceded** with a collection of other, more fine-grained, [repositories](https://github.com/search?q=topic%3Awebprotege+org%3Aprotegeproject&type=Repositories). We are moving WebProtégé to a microservice architecture and each microservice and each common library now has its own repository.  While this repository still serves as the repository for the current WebProtégé release, all active development is now taking place in these repositories.  You can read more about this on the [WebProtégé Next Gen Wiki](https://github.com/protegeproject/webprotege-next-gen/wiki/WebProtégé-Next-Generation-Overview).


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

https://github.com/protegeproject/webprotege/wiki/WebProtégé-4.0.0-beta-x-Installation

Building
--------

To build WebProtégé from source

1) Clone the github repository
   ```
   git clone https://github.com/protegeproject/webprotege.git
   ```
2) Open a terminal in the directory where you clone the repository to
3) Use maven to package WebProtégé
   ```
   mvn clean package
   ```
5) The WebProtege .war file will be built into the webprotege-server directory

Running from Maven
------------------

To run WebProtégé in SuperDev Mode using maven

1) Start the GWT code server in one terminal window
    ```
    mvn gwt:codeserver
    ```
2) In a different terminal window start the tomcat server
    ```
    mvn -Denv=dev tomcat7:run
    ```
3) Browse to WebProtégé in a Web browser by navigating to [http://localhost:8080](http://localhost:8080)

Running from Docker
-------------------

To run WebProtégé using Docker containers:

1. Enter this following command in the Terminal to start the docker container in the background

   ```bash
   docker-compose up -d
   ```

2. Create the admin user (follow the questions prompted to provider username, email and password)

   ```bash
   docker exec -it webprotege java -jar /webprotege-cli.jar create-admin-account
   ```

3. Browse to WebProtégé Settings page in a Web browser by navigating to [http://localhost:5000/#application/settings](http://localhost:5000/#application/settings)
   1. Define the `System notification email address` and `application host URL`
   2. Enable `User creation`, `Project creation` and `Project import`

To stop WebProtégé and MongoDB:

   ```bash
   docker-compose down
   ```

Sharing the volumes used by the WebProtégé app and MongoDB allow to keep persistent data, even when the containers stop. Default shared data storage:

* WebProtégé will store its data in the source code folder at `./.protegedata/protege` where you run `docker-compose`
* MongoDB will store its data in the source code folder at `./.protegedata/mongodb` where you run `docker-compose`

> Path to the shared volumes can be changed in the `docker-compose.yml` file.

OIDC (OpenID Connect SSO)
-------------------------

WebProtégé can delegate sign-in to any OIDC-compatible identity provider (for example Keycloak) using the **authorization code flow**. The server loads `{issuer}/.well-known/openid-configuration`, redirects users to the provider, exchanges the code for tokens, validates the ID token, then provisions or matches a **local** WebProtégé user and starts a normal session.

OIDC is **not** configured in `webprotege.properties`. Set **environment variables** on the JVM process (or equivalent JVM `-D` system properties; environment wins over `-D`).

**Required** (all three must be set; if any is missing, OIDC stays disabled and local login works as usual):

| Variable | Purpose |
|----------|---------|
| `WEBPROTEGE_OIDC_ISSUER_URI` | Issuer base URL, e.g. `https://auth.example.com/realms/myrealm` (used to discover authorization, token, and JWKS endpoints). |
| `WEBPROTEGE_OIDC_CLIENT_ID` | OIDC client id registered at the provider. |
| `WEBPROTEGE_OIDC_CLIENT_SECRET` | Client secret for a **confidential** client. |

**Optional**:

| Variable | Default / behavior |
|----------|-------------------|
| `WEBPROTEGE_OIDC_REDIRECT_URI` | If unset, the callback URL is derived from the incoming HTTP request. For reverse proxies, ensure the public URL the browser sees matches what you register at the IdP. Callback path is always `…/webprotege/oidc/callback`. |
| `WEBPROTEGE_OIDC_SCOPES` | `openid profile email` |
| `WEBPROTEGE_OIDC_USERNAME_CLAIM` | `preferred_username` — used to **link** OIDC accounts to local users; must match an existing username or a new local user is created on first login. |
| `WEBPROTEGE_OIDC_HIDE_LOCAL_LOGIN` | When `true`, the username/password form is hidden and users rely on OIDC (login URL: `…/webprotege/oidc/login`). |

**IdP registration**: Register a redirect URI of the form `https://<your-public-host><context>/webprotege/oidc/callback` (same scheme/host as end users use).

**JVM equivalents** (if you prefer `-D` instead of env): `webprotege.oidc.issuer.uri`, `webprotege.oidc.client.id`, `webprotege.oidc.client.secret`, `webprotege.oidc.redirect.uri`, `webprotege.oidc.scopes`, `webprotege.oidc.username.claim`, `webprotege.oidc.hide.local.login`.

In Docker Compose, add variables under the `webprotege` service `environment:` list (same names as above).

