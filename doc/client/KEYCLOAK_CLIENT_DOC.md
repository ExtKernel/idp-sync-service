# Client types

Base of a Keycloak client request body:
```json
{
  // Base Client body
  "id": "client-id",
  "name": "client-name",
  "fqdn": "client-machine-fqdn",                 
  "ip": "client-machine-ip",                
  "port": "client-machine-port",
  "principalUsername": "client-principal-user-username",
  "principalPassword": "client-principal-user-password",
  "usergroupBlacklist": ["blacklisted-user-group-name"], // Optional
  "userBlacklist": ["blacklisted-user-name"],            // Optional
  // Keycloak specific entries
  "clientSecret": "client-secret",
  "realm": "realm-client-belongs-to",
  "kcFqdn": "fqdn-of-machine-keycloak-is-running-on",
  "kcIp": "ip-of-server-keycloak-is-running-on",
  "kcPort": "port-server-keycloak-is-running-on"
}
```
If an FQDN is specified, IP and port will be ignored.
But you need to specify at least one of the options: FQDN or IP and port. `kcFqdn`, `kcIp` - Are also under this rule

#### Principal user
`principalUsername` and `principalPassword` in case of this client should reflect a Keycloak user
that will be used to get an initial refresh token
using "password" grant type.

#### Keycloak configuration
The service has no problem working with a client that is connected to a different Keycloak server than it itself is *(if it is)*.
The client can be connected to any Keycloak server, while the service is connected to any Keycloak server or any OAuth2 IDP at all.

Which requires the configuration to be specified correctly:
`realm`, `kcFqdn`, `kcIp`, `kcPort` - Should reflect the Keycloak connection properties of **the client**, not this service. 

*Note:* if they are connected to the same Keycloak instance, then it's fine for them to have similar properties. 
But you might find that they are present in different realm or any other difference, which is okay in case if the configuration reflects that.

### API Access Keycloak (KC) Client
This is a client type to access Keycloak REST API.
It extends [the base KcClient class](../../src/main/java/com/iliauni/idpsyncservice/model/KcClient.java) and has a similar to base request body.

Keycloak typically has such a client.
At the moment of 05/09/2025, the client is "admin-cli".
This client is not meant to be synchronized
and will be ignored during the sync process!

### Sync Keycloak (KC) Client
Extends [the base KcClient class](../../src/main/java/com/iliauni/idpsyncservice/model/KcClient.java) and also has a similar to base request body.
Unlike the API Access Keycloak (KC) Client, it represents a Keycloak client that will be synchronized.

