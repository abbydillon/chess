# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Phase 2 link:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDp6gAzJCcGCsyg8mA6SwwDmzMSYHQo4AAa0lAxgmyQYHiCoGnJgwAQao48pQAA80RoebT+YLESp6lKoDyySIVE7qs8YAozShgBb2hr0ABRK0qbAEAqe273YrmeqBJzBYbjObqYCMhbLcNQbx1Q1TJUmwPB+XydXoKFmTime189Q+r0oepoHwIBDxin3Jt01S1EBq9Fu9kDHnc7QOlv3Yz1YMcLXS7S9739mfNocjoPohQ+fUY4CH+JTzADx3z4UmjjLg-6j3Owz3GFPYsonHo2Ao1RdrBvnCrY1C8ExGkqSz1PsIInvq7QQDWaCQcslzxpQgpJhg9ThE4TgZmBpafDAUHAsssHxPBiHIfsVzoBwpheL4ATQOwjIxAAYnA4bSHACgwAAMhAWSFJhzAIr6xbNG0XS9AY6j5GgGaKnMay-P8HBXDc6EVIBfojMpKCqfofw7NCjxAeJbb1AgQnihignCQSRJgKSz6Cpe9IwIyLJsgZ040tugoLjAYoSm6MpyiW7xKheW6DpSz4dl2CAwNAOreA4K7umuKpqpqBmpbAak7DA2SlWgVCmkg9FuTp5l+sy0yntASAAF4oBwkbRrGhS6fC5RacWqYAIySk1+ote1NUSRhyDJjAI34Vmqg5vMUEFkW9Q+ON8STR1dZ0Y2cVXgN5IullT5nS+lSJTANRICK8rFQC67XR5Q4wLucgoA+8THqe57vUFN5Lv6ANrrVlR9dZtloDAv7-pgfXAXpBHRZ8kIwaelG1pCmkzaUiZzVhMA4Xhoxo5yXxY3BCG4zR9b0Z43h+P4XgoOgMRxIk7Ocw5vhYKJKOSdI4b8eG7Tht0PRyaoCnDOROOFINgrQya2N0719X9bdNn2AL8MoISahYJDMjHZ5NkIQq2Dq7TiGxQF8XXqK4r3uD8iyvKiua6YqpBpqPuIaVAripVyD0UDFS3Z23avar2uujte2dVGKAxgpSPawlEkpk4o2NeRKdof18DE2AefDUt-KrXmYwbdAW3J1AbX7bRDaO7yzunVZYOPhDV3uRbH0ojMEA0Hb8TmIQeRxlHlTBaP4+GL9Hrz7df7bvHr6JzAm+DlnsLaT3IF7z4W8qxUonYbhMAtAA0ozDEswEKLLv42Dipq-FojAADiSoNBC0sqfBof8JbS3sEqBWGsHaXyhrvIO6BD7vmAkiGAyAcheUAYbY2OQNBm3ejDa2fhJ5K07rOAULsQpuz7mebQXsyG+1ygHJhwcyphyqpHYeOde6xx7GbNWhdmotyml1dOPUUEWRPn6EaY0i6iI6iXWaJQK4LXztXbMuZ1qFkbgqZurddjtyZvPKovdV4DzbEPJ2-IGRMnLClKBWYKGBWoaFZcpoUoALmB6Yw1iu62P-kqGUmCsCmNut4lAIS0TbwQUfYsoTsFZkNn+BAAFs4gNRk4tQeYGgwE6Pjagx8y6qJvuTcY2TVC5PycY5+TF-AcAAOxuCcCgJwMRwzBDgFxAAbPAUchhIkwCKOXYW9QpIdEgdAnaSsMzZIAHJKkKQmOJ75FywPQHMpUiy5hmXiWg86X10RJMMCiPBWBYlEIwRAEhtskGFFMcFdxdCIrew2YUFhGo2Gcw4RVLhLju4x2SrEh48Sk4KMMeIjOcZkbR1zuoguBiprKKJqUhFmiVraPzLo4s20IVTQOh3KOID0EWPkCC26RzDBoBQJseAU91BDMeEEnxliqB9ipMPeoORoDUtpSyqJa5HkgzvAKtePDCY3SugyJUABJaQ28d5gs+gMk5KTEawqlUU4sIxsnyuWcU6+pNb4PyfszeplgUDdggHS2ISAEhgEtdaulAApCA4oBUxGSKAdUIzVFjMaE0ZkMkejZJgfbTZoxsAIGAJaqAcAIA2SgGsPV0gDWlzVvcjM0bY2UATUmlNcq01SOKbdAAVu6uG2TcHOVMJDK5Vt8p3PeQCk6QpXYSjJcARh9y-Z5W+XDX54dqqtrnCfdB-CQVCKRR1KFkjNWDUrvIkRhiUWVCNYtCmy1a46M2vo-FbczXhOlS8tlHKrnPP1IYatkVU2jqoQvG8zzsmvJOfKutg8SWHKniATUZznIdRgDmuN+boB9tYf+k2gHgPoRAImtKZVqyc1NOaSwU7d4BlQ6GRCc7M6arMafVMwQ10lPmkRjFO7sV7pQ0GC0MAkNIVqRSk9fgtDHMiTKXU+ooqchBVclaE9WPfWXBxoVErH1bWwGxwZwScrrxPfvfk6HlWKfUCWnW8LVMEIJuUI1ZM76P0Oua1mXhY1c3tTzKAZnEBBlgMAbA0aZ49WGcArVoDRbi0ltLYwJG1YNA8xLKWvQfP4cpdwPANaTZ8a5Z9cLsAr170gG-IwYmbFjvbTADIY8J6eIutoNYXa1jMtE+S+TvctPKbWWfLeC7NPnwPvAvTJrDMNiAA
