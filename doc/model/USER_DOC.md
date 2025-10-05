# User
A user represents a common user for every client.

Request body:
```json
{
  "username": "username",
  "firstname": "first-name", // Optional
  "lastname": "last-name",   // Optional
  "email": "email",          // Optional
  "password": "password"     // Optional
}
```

## Endpoints
### Create
Endpoint: `POST /secured/user`

**Note**:
- Passwords are stored in plain text at this point
- Although `firstname`, `lastname` and `email` fields are optional, it is recommended to fill them to avoid any unexpected behavior during synchronization of FreeIPA clients. If not filled, the app will fill them with placeholders during synchronization.

### Find all
Endpoint: `GET /secured/user`

### Find by ID
Endpoint: `GET /secured/user/<username>`

### Update
Endpoint: `PUT /secured/user`

This endpoint expects the same request body as for **Create**.

### Update password
Endpoint: `PATCH /secured/user/<username>`

URL parameters:
- `newPassword` - **required**
- `sync` - optional. Set to boolean true, if you want to synchronize the password changes across all clients that contain the user.

### Delete
Endpoint: `DELETE /secured/user/<username>`