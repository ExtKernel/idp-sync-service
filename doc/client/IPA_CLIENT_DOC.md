# (Free)IPA Client
This client type represents a [FreeIPA](https://www.freeipa.org/)(also known as: IPA, [IdM](https://access.redhat.com/products/identity-management/)) server instance.

Request body:
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
  // FreeIPA certificate configuration
  "certPath": "/freeipa/cert/path/on/local/machine"
}
```
If an FQDN is specified, IP and port will be ignored.
But you need to specify at least one of the options: FQDN or IP and port.

#### (Free)IPA certificate configuration
The `certPath` field should have the path to FreeIPA's `ca.crt`
file **on the local machine where this app is running** as a value.
You should copy the file from the FreeIPA server.
Typically, it's path is `/etc/ipa/ca.crt`

#### Principal user
`principalUsername` and `principalPassword` in case of this client should reflect a (Free)IPA admin

