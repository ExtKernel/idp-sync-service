# Identity Provider Synchronization Service
A Java-based microservice for managing and synchronizing user identities across multiple systems including Keycloak, Windows, and FreeIPA.

## Endpoints
Every `/secured` endpoint requires an Oauth2 Bearer token
that contains the role specified in the `PRINCIPAL_ROLE_NAME` environment variable.
See the configuration documentation under the **Configuration** section.

# Client
## Base client
Base endpoint that should be added before every following client endpoint: `/secured/client`.

Base client request body:
```json
  "id": "client-id",
  "name": "client-name",
  "fqdn": "client-machine-fqdn", (optional)
  "ip": "client-machine-ip", (optional)
  "port": "client-machine-port", (optional)
  "principalUsername": "client-principal-user-username", (optional)
  "principalPassword": "client-principal-user-password" (optional)
```
These fields should be present in every client below.
Every example of a request body for the clients below will include only specific for the client fields,
but these are included implicitly. 

**Note**: if the FQDN is specified, IP and port will be ignored.
But you need to specify at least one of the options: FQDN or IP and port

## API Access Keycloak (KC) Client
This is a client type to access Keycloak REST API.
It extends [the base KcClient class](./src/main/java/com/iliauni/idpsyncservice/model/KcClient.java).
Keycloak typically has such a client.
At the moment of 13.08.2024, that client is admin-cli. 
This client is not meant to be synchronized
and will be ignored during the sync process!

### Create
Endpoint: `POST /API/KC`

JSON request body explanation:
```json
{
  "clientSecret": "client-secret",
  "realm": "realm-client-belongs-to",
  "kcFqdn": "fqdn-of-machine-keycloak-is-running-on",
  "kcIp": "ip-of-server-keycloak-is-running-on",
  "kcPort": "port-server-keycloak-is-running-on" 
}
```
**Warning**: 
- "id" field should match the client ID given to this client in the Keycloak realm
- The rule about FQDN or IP and port options is applicable to Keycloak server fields as well
- "principalUsername" and "principalPassword" fields are actually required here. Although you can create the client without specifying them, operations will fail

### Find all
Endpoint: `GET /API/KC`

### Find by ID
Endpoint: `GET /API/KC/<client-id>`

### Update
Endpoint: `PUT /API/KC`

This endpoint expects the same request body as the **Create** one.

### Delete
Endpoint: `DELETE /API/KC/<client-id>`

## Sync Keycloak (KC) Client
This client type also belongs to Keycloak family
and extends [the base KcClient class](./src/main/java/com/iliauni/idpsyncservice/model/KcClient.java).
Unlike the API Access one, it represents a Keycloak client that should be synchronized.

The only difference in endpoints is that you should replace `/API/KC/` with `/KC`.
Everything else from the documentation for **API Access Keycloak (KC) Client** works here in the same way.

## Windows (Win) Client
This client type represents a [Windows User Sync Local Server](https://github.com/ExtKernel/win-user-sync-local-server) instance.
It extends [the base KcClient class](./src/main/java/com/iliauni/idpsyncservice/model/KcClient.java)
and belongs to Keycloak family.
That is because the server is supposed to be registered in a Keycloak realm.
Which makes it a Keycloak client in a nutshell.

The only difference in endpoints is that you should replace previous Keycloak client endpoints(`/API/KC/` or `/KC`)
with `/Win`.
Everything else from the documentation for **API Access Keycloak (KC)Client**
and **Sync Keycloak (KC) Client** works here in the same way.

## (Free)IPA Client
This client type represents a FreeIPA(also known as: IPA, IdM) server instance.

### Create
Endpoint: `POST /IPA`

JSON request body explanation:
```json
{
  "certPath": "freeipa-cert-path-on-local-machine"
}
```
**Note**:
the `certPath` field should have the path to FreeIPA's `ca.crt`
file **on the local machine where this app is running** as a value.
You should copy the file from the FreeIPA server.
Typically, it appears at `/etc/ipa/ca.crt`

### Find all
Endpoint: `GET /IPA`

### Find by ID
Endpoint: `GET /IPA/<client-id>`

### Update
Endpoint: `PUT /IPA`

This endpoint expects the same request body as the **Create** one.

### Delete
Endpoint: `DELETE /IPA/<client-id>`

# User
### Create
Endpoint: `POST /secured/user`

JSON request body explanation:
```json
{
  "username": "username",
  "firstname": "first-name", (optional)
  "lastname": "last-name", (optional)
  "email": "email", (optional)
  "password": "password" (optional) 
}
```
**Note**:
- Passwords are stored in plain text at this point
- Although `firstname`, `lastname` and `email` fields are optional, it is recommended to fill them to avoid any unexpected behavior during synchronization of FreeIPA clients. If not filled, the app will fill them with placeholders during synchronization.

### Find all
Endpoint: `GET /secured/user`

### Find by ID
Endpoint: `GET /secured/user/<username>`

### Update
Endpoint: `PUT /secured/user`

This endpoint expects the same request body as the **Create** one.

### Update password
Endpoint: `PATCH /secured/user/<username>`

URL parameters:
- `newPassword` - **required**
- `sync` - optional. Set to boolean true, if you want to synchronize the password changes across all clients that contain the user. 

### Delete
Endpoint: `DELETE /secured/user/<username>`

# User group
### Create
Endpoint: `POST /secured/group`

JSON request body explanation:
```json
{
  "name": "user-group-name",
  "description": "user-group-description", (optional)
  "users": [ (optional)
    {
      "username": "username",
      "firstname": "first-name", (optional)
      "lastname": "last-name", (optional)
      "email": "email", (optional)
      "password": "password" (optional)
    }
  ]
}
```

### Find all
Endpoint: `GET /secured/group`

### Find by ID
Endpoint: `GET /secured/group/<usergroupName>`

### Update
Endpoint: `PUT /secured/group`

This endpoint expects the same request body as the **Create** one.

### Delete
Endpoint: `DELETE /secured/group/<usergroupName>`

# Synchronization
Endpoints:
- `/secured/sync/groups` - expects an array of **user groups**
- `/secured/sync/users` - expects an array of **users**

# Configuration:
This section describes the environment variables used by the Java application.
These variables are injected into the code to configure various aspects of the application.

### General
- `EUREKA_URI` - the full URL of the Eureka server. This variable has a default value: `http://localhost:8761/eureka`. But very likely will be required to be changed depending on your specific setup
- `SERVER_PORT`- the server port. This variable has a default value: `8000`

### Database
The database must be PostgreSQL.
- `DATASOURCE_HOST` - the URL of the server where the database is hosted
- `DATASOURCE_NAME` - the name of the database
- `DATASOURCE_USERNAME` - The username that has the necessary permissions to access and interact with the database
- `DATASOURCE_PASSWORD` - the password of the user

### Oauth2
- `PRINCIPAL_ROLE_NAME` - the role that the OAuth2 user should have to access `secured` endpoints. Has a default value: `administrator`. **Note that** the token used to access this app should contain the role 
- `OAUTH2_PROVIDER_ISSUER_URL` - the `issuer URL` of your OAuth2 provider server
- `OAUTH2_PROVIDER_CLIENT_ID` - the `client ID` associated with this application's client on the OAuth2 provider server
- `OAUTH2_PROVIDER_CLIENT_SECRET` - the client `client secret` associated with this application's client on the OAuth2 provider server
- `OAUTH2_PROVIDER_INTROSPECTION_URL` - the `token introspection URL` of your OAuth2 provider server

### Clients
- `IPA_API_ENDPOINT` - the main endpoint of the FreeIPA's JSON API. Has a default value: `/ipa/session/json`
- `IPA_API_AUTH_ENDPOINT` - the endpoint for FreeIPA's API authentication. Has a default value: `/ipa/session/login_password`

You can visit the [FreeIPA's documentation](https://freeipa.readthedocs.io/en/latest/api/jsonrpc_usage.html) to see if something has changed.

- `KC_ADMIN_CLI_CLIENT_ID` - the name of Keycloak's (KC) `admin-cli` client. Change only if that changes with a new Keycloak update, which is very unlikely. Has a default value: `admin-cli`

You can visit the [Keycloak's documentation](https://www.keycloak.org/docs/latest/server_admin/) to see if something has changed.

# Usage
## Java jar
1) Clone the repository:
    ```bash
      git clone https://github.com/ExtKernel/idp-sync-service
    ```
2) Navigate to the directory:
    ```bash
      cd idp-sync-service
    ```
3) Run:
    ```bash
      mvn package    
    ```
4) Run the .jar file:
    ```bash
      java -jar target/idp-sync-service-<VERSION>.jar    
    ```
## Maven plugin
1) Clone the repository:
    ```bash
      git clone https://github.com/ExtKernel/idp-sync-service
    ```
2) Navigate to the directory:
    ```bash
      cd idp-sync-service
    ```
3) Run:
    ```bash
      mvn spring-boot:run
    ```
## Docker
1) Pull the Docker image:
    ```bash
      docker pull exkernel/idp-sync-service:<VERSION>
    ```
2) Run the container:
    ```bash
      docker run --name=idp-sync-service -p 8000:8000 exkernel/idp-sync-service:<VERSION>
    ```
   You can map any external port you want to the internal one.
   Remember to specify environment variables using the `-e` flag:
   - `-e EUREKA_URI=<value>`
   - `-e DATASOURCE_HOST=<value>`
   - `-e DATASOURCE_USERNAME=<value>`
   - `-e DATASOURCE_PASSWORD=<value>`
   - `-e PRINCIPAL_ROLE_NAME=<value>`
   - `-e OAUTH2_PROVIDER_ISSUER_URL=<value>`
   - `-e OAUTH2_PROVIDER_CLIENT_ID=<value>`
   - `-e OAUTH2_PROVIDER_CLIENT_SECRET=<value>`
   - `-e OAUTH2_PROVIDER_INTROSPECTION_URL=<value>`
   
   You may also specify the optional ones if you want:
   - `-e IPA_API_ENDPOINT=<value>`
   - `-e IPA_API_AUTH_ENDPOINT=<value>`
   - `-e KC_ADMIN_CLI_CLIENT_ID=<value>`

**BUT BE AWARE**: `-e SERVER_PORT=<value>` - changes the internal port of the service, which won't be considered by the [Dockerfile](Dockerfile). There always will be port `8000` exposed, until you change it and build the image yourself. 

## Build the Docker image yourself
If you want to alter the [Dockerfile](Dockerfile) in any way, you'll need to build the image from scratch.
1) Clone the repository:
    ```bash
      git clone https://github.com/ExtKernel/idp-sync-service
    ```
2) Navigate to the directory:
    ```bash
      cd idp-sync-service
    ```
3) Alter the [Dockerfile](Dockerfile)
4) Build the image:
    ```bash
      docker build -t <IMAGE-NAME> .
    ```
5) Run the container as explained in the previous section, but using your <IMAGE-NAME>, instead of `exkernel/idp-sync-service:<VERSION>`

## Deprecated!
### Development moves
This service will be developed
**only until** it is safe and rational to decouple into smaller microservice applications.
Links to repositories will be provided later.

### Why
- This application reached the point that it's unnecessarily difficult to improve stability and performance.
- Basic changes like multithreading require too much effort, which could be spent on more significant features, than minor optimization.
- No room for future-proof technology integration. E.g. a decoupled version of the system will combine a message broker with REST requests, which complements a lot in the long run.
- Hard to scale. At the moment, there's almost no solution for bottlenecks in specific operation categories.
- E.g. we can't launch more instances of Identity Provider server client manager classes if something is bottlenecking there. The example applies to every operation category included in the app.

### Refactoring is the way
Changes should be accepted, it's the way we improve.
