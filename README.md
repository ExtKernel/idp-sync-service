## Endpoints
Every `/secured` endpoint requires an Oauth2 Bearer token.
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
