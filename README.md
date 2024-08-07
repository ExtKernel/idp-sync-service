# Deprecated!
## Development moves
This service will be developed **only until** it is safe and rational to decouple into smaller microservice applications. Links to repositories will be provided later.

## Why
- This application reached the point that it's unnecessarily difficult to improve stability and performance.
- Basic changes like multithreading require too much effort, which could be spent on more significant features, than minor optimization.
- No room for future-proof technology integration. E.g. a decoupled version of the system will combine a message broker with REST requests, which complements a lot in the long run.
- Hard to scale. At the moment, there's almost no solution for bottlenecks in specific operation categories. E.g. We can't launch more instances of Identity Provider server client manager classes if something is bottlenecking there. The example applies to every operation category included in the app.

## Refactoring is the way
Changes should be accepted, it's the way we improve.
