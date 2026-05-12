## Main configuration
This section describes the environment variables used by the Java application.
These variables are injected into the code to configure various aspects of the application.

### General
- `EUREKA_URI` - Full URL of the Eureka server. This variable has a default value: `http://localhost:8761/eureka`. But very likely will be required to be changed depending on your specific setup
- `SERVER_PORT`- Server port. This variable has a default value: `8000`

### Database
The database must be PostgreSQL.
- `DATASOURCE_HOST` - URL of the server where the database is hosted
- `DATASOURCE_NAME` - Name of the database
- `DATASOURCE_USERNAME` - Username of a user that has the necessary permissions to access and interact with the database
- `DATASOURCE_PASSWORD` - Password of the user

### Oauth2
- `PRINCIPAL_ROLE_NAME` - A role that the OAuth2 user should have to access `secured` endpoints. Has a default value: `administrator`. **Note that** a token used to access this app should contain the role
- `OAUTH2_PROVIDER_CLIENT_ID` - `Client ID` associated with this application's client on the OAuth2 provider server
- `OAUTH2_PROVIDER_CLIENT_SECRET` - The client's `client secret` associated with this application's client on the OAuth2 provider server

You might want to see OpenID Connect configuration values from the provider's Well-Known Configuration for the following variables:
- `OAUTH2_PROVIDER_ISSUER_URL` - `issuer URL` of your OAuth2 provider server
- `OAUTH2_PROVIDER_INTROSPECTION_URL` - `token introspection URL` of your OAuth2 provider server

### Clients
- `IPA_API_ENDPOINT` - the main endpoint of the FreeIPA's JSON API. Has a default value: `/ipa/session/json`
- `IPA_API_AUTH_ENDPOINT` - the endpoint for FreeIPA's API authentication. Has a default value: `/ipa/session/login_password`

You can visit the [FreeIPA's documentation](https://freeipa.readthedocs.io/en/latest/api/jsonrpc_usage.html) to see if anything has changed.

- `KC_ADMIN_CLI_CLIENT_ID` - the name of Keycloak's (KC) `admin-cli` client. Change only if that changes with a new Keycloak update, which is very unlikely. Has a default value: `admin-cli`

You can visit the [Keycloak's documentation](https://www.keycloak.org/docs/latest/server_admin/) to see if anything has changed.

---
## Integration testing configuration
The above configuration does not apply if you want to run integration tests. Please use the variables described in this section instead.

- WAIT_SYNC - Amount of milliseconds to wait until async operations are done. 
Spring Application context will shutdown and the sync operations won't be completed otherwise. Has a default value: `15000` (15 seconds)
**Keycloak (KC) simulator:**
- KEYCLOAK_SIMULATOR_IP - IP of a [Keycloak (KC) simulator](https://github.com/ExtKernel/keycloak-simulator) instance
- KEYCLOAK_SIMULATOR_PORT - Port of a [Keycloak (KC) simulator](https://github.com/ExtKernel/keycloak-simulator) instance
- KEYCLOAK_SIMULATOR_REALM - Realm that a IP of a [Keycloak (KC) simulator](https://github.com/ExtKernel/keycloak-simulator) instance is configured to accept