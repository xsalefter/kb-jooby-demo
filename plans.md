# Demo Plan

## Goal

Build a minimal runnable demo application that uses the local `org.kill-bill.commons:killbill-jooby:0.27.0-SNAPSHOT` fork to:

- serve a plain HTML page
- expose simple CRUD HTTP endpoints for `GET`, `POST`, `PUT`, and `DELETE`
- include executable integration tests that hit the app over HTTP

## Constraints From The Local Fork

- The `killbill-jooby` snapshot currently available in the local Maven repository is the fork documented in `killbill-commons/jooby/README.md` and `CHANGES.md`.
- The user requirement for this demo is to target Jetty 11 anyway.
- That means this project should force Jetty 11 at the Maven level even though the current local `killbill-jooby` fork is still aligned with the Jetty 10-era servlet boundary described in the fork notes.
- As a result, runtime or integration-test failure is acceptable here and should be treated as an expected compatibility signal, not hidden.

## Implementation Plan

1. Create a simple Maven application in this directory.
2. Add `killbill-jooby` as the main dependency.
3. Force Jetty 11 dependency resolution for the runtime stack.
4. Implement a small `Jooby` app with:
   - `GET /` returning plain HTML
   - CRUD endpoints under `/api/items`
   - explicit Java endpoint classes instead of lambda-heavy route declarations
   - a second CRUD resource under `/jaxrs/items` with JAX-RS annotations layered onto the endpoint class
5. Keep persistence in-memory so the demo stays small and focused.
6. Add a test harness that starts the app on a free local port.
7. Verify the HTML route and each CRUD operation with HTTP-based integration tests.
8. Run the Maven test suite and document the observed behavior, including expected Jetty 11 incompatibilities from the current fork.

## Expected Output

- runnable demo source
- `pom.xml`
- app code under `src/main/java`
- test code under `src/test/java`
- Maven test outcome documented, including failure details if Jetty 11 breaks the current fork

## Current Outcome

- The project source has been created with `killbill-jooby` as the main dependency.
- The Maven build now forces Jetty 11 coordinates through dependency management.
- The app structure has been refactored to explicit endpoint classes:
  - one HTML resource class
  - one class per CRUD operation for `/api/items`
  - one separate resource class for `/jaxrs/items`
- The `/jaxrs/items` resource is layered with `jakarta.ws.rs` annotations in addition to the Jooby MVC annotations so the code reads like a JAX-RS resource while still fitting the current demo wiring.
- The app and tests were previously runnable against the locally cached Jetty 10-era dependency graph from the current `killbill-jooby` snapshot.
- After forcing Jetty 11, the current verification command fails before execution because the required Jetty 11 artifacts are not available in the local Maven cache and the build is being run offline.

Observed failure from:

```bash
env JAVA_HOME=/home/xsalefter/.sdkman/candidates/java/17.0.18-tem /home/xsalefter/.sdkman/candidates/maven/current/bin/mvn -o test
```

Observed blocker:

- `org.eclipse.jetty:jetty-alpn-server:11.0.20`
- `org.eclipse.jetty:jetty-io:11.0.20`
- `org.eclipse.jetty:jetty-server:11.0.20`
- `org.eclipse.jetty:jetty-util:11.0.20`
- `org.eclipse.jetty.http2:http2-server:11.0.20`
- `org.eclipse.jetty.websocket:websocket-jetty-api:11.0.20`

Interpretation:

- The project is now configured to use Jetty 11 as requested.
- The current environment cannot complete verification offline because those Jetty 11 artifacts have not been downloaded yet.
- Even after those artifacts are downloaded, additional runtime incompatibilities may still be expected because the current local `killbill-jooby` fork remains aligned with the pre-Jetty-11 servlet boundary described in the fork notes.
