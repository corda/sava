# Corda Fork

The upstream library follows the JDK's [tip & tail](https://openjdk.org/jeps/14) release model and keeps in-sync 
with the latest Java version. So at the time of writing it only supports from Java 25 (the tip) to Java 21 
(the tail). This forks extends this to Java 17. However, doing so required some API changes which depend on features 
not in Java 17, such as [SequencedCollection](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html).

The main branch of this fork is `corda`, which tracks `upstream/main`. It keeps the same Maven coordinates and 
version. The only difference is `-j17-<forked-patch>` is appended to the latest version it's forked from. The
`forked-patch` number is for tracking changes in this fork which are still based on the same upstream version.

To make a new release against a newer upstream version, merge `upstream/main` into `corda`. Make sure to update the 
[`version` property](gradle.properties) to the latest version with `-17-1` appended at the end.

## Forked Dependencies

You may find the `software.sava.build` plugin [version](settings.gradle.kts) was also updated upstream. This plugin 
also needed to be [forked](https://github.com/corda/sava-build) as the upstream version doesn't work with Java 17. 
If the upstream version was updated then the `sava-build` fork will also need to be updated to be based on this new 
upstream version. It follows the exact same branching and versioning strategy as this fork.

It's also possible the [BOM version](gradle/sava.properties) was updated. This is another repo that needed to be
[forked](https://github.com/corda/solana-version-catalog), and it also follows the branching and versioning 
strategy.

The final repo that needed to be forked for Java 17 is [json-iterator](https://github.com/corda/json-iterator). Its 
version is tracked in the BOM, and so in updating that you may find the upstream version for it has changed and so 
this fork will need to be updated accordingly.

## CI

There is a single [Jenkins stage](Jenkinsfile) that both tests and publishes from the `corda` branch. So make sure 
any changes have the relevant change to the [version](gradle.properties). This also applies to the other forks.

![Sava](assets/images/solana_java_cup.svg)

# Sava [![Gradle Check](https://github.com/sava-software/sava/actions/workflows/build.yml/badge.svg)](https://github.com/sava-software/sava/actions/workflows/build.yml) [![Publish Release](https://github.com/sava-software/sava/actions/workflows/publish.yml/badge.svg)](https://github.com/sava-software/sava/actions/workflows/publish.yml)

## Documentation

User documentation lives at [sava.software](https://sava.software/).

* [Dependency Configuration](https://sava.software/quickstart)
* [Core](https://sava.software/libraries/core): Common Solana cryptography and serialization utilities.
* [RPC](https://sava.software/libraries/rpc): HTTP and WebSocket Clients.

## Contributions

Please note that all contributions require agreeing to
the [Sava Engineering, Inc. CLA](https://gist.github.com/jpe7s/09546e42783187c6d04f38e04184ecfa).

Tests are needed and welcomed. Otherwise, [please reach out](https://github.com/sava-software) before working on a pull
request.

### RPC Tests

A mini framework for testing RPC calls is provided to make it as easy as possible to test the calls you rely on.
See [RoundTripRpcRequestTests](sava-rpc/src/test/java/software/sava/rpc/json/http/client/RoundTripRpcRequestTests.java)
for example usage.

* If you plan to add several tests, create a new class to avoid merge conflicts.
* If you feel there is already enough response test coverage, you can [skip it by only providing the expected request
  JSON.](https://github.com/sava-software/sava/blob/55e41207d932708affd05be54168f6bfb6105ec6/sava-rpc/src/test/java/software/sava/rpc/json/http/client/RoundTripRpcRequestTests.java#L30)
  * See [ParseRpcResponseTests](sava-rpc/src/test/java/software/sava/rpc/json/http/client/ParseRpcResponseTests.java)
    for additional response parsing tests.
* If the response JSON is large, add it to the [resource](sava-rpc/src/test/resources/rpc_response_data) directory as a
  JSON file.
  * If the JSON file is larger than 1MB, apply zip compression to it.
  * If it is large because it is a collection of items, consider trimming the list down to at least two items.

#### Capture Request JSON

* Start a test with the desired call, the test will fail with the difference between the expected and actual requests.

```java

@Test
void getHealth() {
  registerRequest("{}");
  rpcClient.getHealth().join();
}
```

```text
SEVERE: Expected request body does not match the actual. Note: The JSON RPC "id" does not matter.
 - expected: {}
 - actual:   {"jsonrpc":"2.0","id":123,"method":"getHealth"}
```

* Or, enable debug logging by using a [logging.properties](logging.properties) file and pass it to the VM via:

``` 
-Djava.util.logging.config.file=logging.properties
```

#### Capture Response JSON

```java
var rpcClient = SolanaRpcClient.build().testResponse((_, body) -> {
  final var json = new String(body);
  System.out.println(json); // Write to a file if large.
  return true;
}).createClient();
```

#### Validation

Reference the [official Solana RPC documentation](https://solana.com/docs/rpc/http) to verify that the expected
parameters are passed in the request, and if applicable, all the desired response data is parsed correctly.

## Build

[Generate a classic token](https://github.com/settings/tokens) with the `read:packages` scope needed to access
dependencies hosted on GitHub Package Repository.

#### ~/.gradle/gradle.properties

```properties
savaGithubPackagesUsername=GITHUB_USERNAME
savaGithubPackagesPassword=GITHUB_TOKEN
```

```shell
./gradlew check
```
