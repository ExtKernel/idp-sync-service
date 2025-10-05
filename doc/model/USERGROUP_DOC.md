# User group-related functionality
A user group represents a common user group for every client. This is a main model in the service.

Request body:
```json
{
  "name": "user-group-name",
  "description": "user-group-description", // Optional
  "users": [                     // Optional
    {
      "username": "username",
      "firstname": "first-name", // Optional
      "lastname": "last-name",   // Optional
      "email": "email",          // Optional
      "password": "password"     // Optional
    }
  ]
}
```

## Endpoints
### Create
Endpoint: `POST /secured/group`

### Find all
Endpoint: `GET /secured/group`

### Find by ID
Endpoint: `GET /secured/group/<usergroupName>`

### Update
Endpoint: `PUT /secured/group`

This endpoint expects the same request body as for **Create**.

### Delete
Endpoint: `DELETE /secured/group/<usergroupName>`