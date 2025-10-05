# Identity Provider Synchronization Service
A Java-based microservice for managing and synchronizing user identities across multiple systems 
including [Keycloak](https://www.keycloak.org/), [Windows 10/11](https://www.microsoft.com/en-us/windows), and [(Free)IPA](https://www.freeipa.org/).

### Client-related functionality
**Keycloak**
- [Client documentation](doc/client/KEYCLOAK_CLIENT_DOC.md)
- [Client-related endpoint documentation](doc/client/KEYCLOAK_CLIENT_ENDPOINT_DOC.md)

**(Free)IPA**
- [Client documentation](doc/client/IPA_CLIENT_DOC.md)
- [Client-related endpoint documentation](doc/client/IPA_CLIENT_ENDPOINT_DOC.md)

**Windows**
- [Client documentation](doc/client/WINDOWS_CLIENT_DOC.md)
- [Client-related endpoint documentation](doc/client/WINDOWS_CLIENT_ENDPOINT_DOC.md)

### User-related functionality
Refer to [the user documentation](doc/model/USER_DOC.md)

### User group-related functionality
Refer to [the user group documentation](doc/model/USERGROUP_DOC.md)

## Configuration:
Refer to [the configuration documentation](doc/CONFIGURATION_DOC.md)

## Synchronization
Endpoints:
- `/secured/sync/groups` - expects a JSON list of **user groups**
- `/secured/sync/users` - expects a JSON list of **users**

To understand the synchronization flow, please refer to [the sync flow documentation](doc/SYNC_DOC.md)

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
   - You can map any external port you want to the internal one
   - You can give any name to the container
   
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

   ### If you are going to synchronize FreeIPA clients:
   1) Copy your `ca.crt` certificate file to the container:
      ```bash
        docker cp <path-to-certificate-file-on-your-local-machine> <container-id-or-name>:/app/<desired-certificate-file-name>.crt
      ```
   2) Make sure you specify the path to the certificate(by default `ca.crt`) file correctly when creating the **(Free)IPA Client**:
      ```json
      {
        "certPath": "/app/<specified-in-the-previous-step-certificate-file-name>.crt"
      }
      ```

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