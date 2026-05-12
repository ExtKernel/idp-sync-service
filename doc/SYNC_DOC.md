## Synchronization

#### Sync flow
**New user groups**

A user group from the JSON list given in a sync request will be considered new
if it's not present in the local database of the service.  

For new user groups, the synchronization flow is as simple as:
```mermaid
flowchart TD
    A(You) -->|usergroups| B(SyncController)
    B --> C[SyncHandlers]

    subgraph NEW [New Usergroup]
        direction LR
        D(New) -->|save Usergroup| E[Local Database]
        D -->|create Usergroup| F[Keycloak Clients]
        D -->|create Usergroup| G[FreeIPA Clients]
        D -->|create Usergroup| H[Windows Clients]
    end

    C --> D
```
**Altered user groups**

A user group from the JSON list given in a sync request will be considered altered
if it's present in the local database of the service, but its list of users(members) is different.

The synchronization flow will reflect additions and removals of user group users(members):
```mermaid
flowchart TD
A(You) -->|usergroups| B(SyncController)
B --> C[SyncHandlers]

    subgraph ALTERED [Altered Usergroup]
        direction LR
        I(Altered) -->|add/remove members| J[Local Database]
        I -->|add/remove Usergroup members| K[Keycloak Clients]
        I -->|add/remove Usergroup members| L[FreeIPA Clients]
        I -->|add/remove Usergroup members| M[Windows Clients]
    end

    C --> I
```

**Missing user groups**

A user group from the JSON list given in a sync request will be considered missing
if it's present in the local database of the service, but is missing in the JSON list of user group given in the request.

The synchronization flow will reflect deletions of the user groups:
```mermaid
flowchart TD
    A(You) -->|usergroups| B(SyncController)
    B --> C[SyncHandlers]

    subgraph MISSING [Missing Usergroup]
        direction LR
        N(Missing) -->|delete Usergroup| O[Local Database]
        N -->|delete Usergroup| P[Keycloak Clients]
        N -->|delete Usergroup| Q[FreeIPA Clients]
        N -->|delete Usergroup| R[Windows Clients]
    end

    C --> N
```

#### Overall Sync flow
```mermaid
flowchart TD
    A(You) -->|usergroups| B(SyncController)
    B --> C[SyncHandlers]

    subgraph NEW [New Usergroup]
        direction LR
        D(New) -->|save Usergroup| E[Local Database]
        D -->|create Usergroup| F[Keycloak Clients]
        D -->|create Usergroup| G[FreeIPA Clients]
        D -->|create Usergroup| H[Windows Clients]
    end

    subgraph ALTERED [Altered Usergroup]
        direction LR
        I(Altered) -->|add/remove members| J[Local Database]
        I -->|add/remove Usergroup members| K[Keycloak Clients]
        I -->|add/remove Usergroup members| L[FreeIPA Clients]
        I -->|add/remove Usergroup members| M[Windows Clients]
    end

    subgraph MISSING [Missing Usergroup]
        direction LR
        N(Missing) -->|delete Usergroup| O[Local Database]
        N -->|delete Usergroup| P[Keycloak Clients]
        N -->|delete Usergroup| Q[FreeIPA Clients]
        N -->|delete Usergroup| R[Windows Clients]
    end

    C --> LocalDB(User group present in the local database)
    LocalDB --> NotPresentInLocalDB(No)
    NotPresentInLocalDB --> D
    LocalDB --> PresentInLocalDB(Yes)
    PresentInLocalDB --> NoChanges(Is similar) --> NoSync(No synchronization will be done)
    PresentInLocalDB --> DifferentMembers(Has different members list)
    DifferentMembers --> I

    PresentInLocalDB --> AbsentInJSON(Absent in the request JSON)
    AbsentInJSON --> N
```