# Extensions / Helpers for Java FuelSDK

This repository has some classes that we've found useful when developing against the ExactTarget API.

You can download the library here. JavaDoc can be found on [BuaBook.github.io](http://buabook.github.io/javadoc/sf-et-fuel-sdk/).

To-do:

* Release to Maven Central

## Included Classes

### `AutoRefreshETClient`

We've found that the default `ETClient` class does not support refreshing of the OAuth refresh token. This token is defined by the OAuth standard to expire after 14 days after which point all API requests will fail.

This class extends `ETClient` and provides automatic refreshing of the refresh token after a duration which can be configured by the caller (there is a default of refreshing every 24 hours if none is provided). The rest of the class behaves identically to `ETClient`.

Use the static method `AutoRefreshETClient.newRefreshClient` to get a new object.

### `ExactTargetInterface`

This class provides some interface methods to retrieving / sending data via the ExactTarget API.

Available methods:

* `getDataExtension`: Retrieves a data extension by using the name as appears on the ExactTarget web UI 