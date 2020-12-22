Spring Security JWT and Opaque Token Validation
===============================================


This example app shows how to create a Spring Boot application that uses both JWT and opaque access tokens.

Please read [JWT vs Opaque Access Tokens: Use Both With Spring Boot](https://developer.okta.com/blog/2020/08/07/spring-boot-remote-vs-local-tokens) to see how this app was created.

**Prerequisites:** [Java 8+](https://adoptopenjdk.net/).

> [Okta](https://developer.okta.com/) has Authentication and User Management APIs that reduce development time with instant-on, scalable user infrastructure. Okta's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

* [Getting Started](#getting-started)
* [Links](#links)
* [Help](#help)
* [License](#license)

## Getting Started

To install this example application, run the following commands:

```bash
git clone https://github.com/oktadeveloper/okta-spring-jwt-and-opaque-token-example.git
cd okta-spring-jwt-and-opaque-token-example
```

This will get a copy of the project installed locally. To install all of its dependencies and start the app, follow the instructions below.

To run the application:

```bash
./mvnw spring-boot:run
```

### Create an Application in Okta

You will need to create an OpenID Connect Application in Okta to get your values to perform authentication.

Log in to your Okta Developer account (or [sign up](https://developer.okta.com/signup/) if you don’t have an account) and navigate to **Applications** > **Add Application**. Click **Web**, click **Next**, and give the app a name you’ll remember.

* Change the **Login redirect URIs** to `https://oidcdebugger.com/debug`
* Check **Implicit (Hybrid)**
* Click **Done**.

#### Server Configuration

Set the `issuer` and copy the `client-id` and `client-secret` into `server/src/main/resources/application.properties`.

**NOTE:** The value of `{yourOktaDomain}` should be something like `dev-123456.oktapreview.com`. Make sure you don't include `-admin` in the value!

```properties
spring.security.oauth2.client.provider.okta.issuer-uri = https://{yourOktaDomain}/oauth2/default
spring.security.oauth2.client.registration.okta.client-id = {clientId}
spring.security.oauth2.client.registration.okta.client-secret = {clientSecret}
```

## Links

This example uses the following libraries provided by Okta:

* [Okta Spring Boot Starter](https://github.com/okta/okta-spring-boot)

## Help

Please post any questions as comments on [this repo's blog post](https://developer.okta.com/blog/2020/08/07/spring-boot-remote-vs-local-tokens), or visit our [Okta Developer Forums](https://devforum.okta.com/). You can also email developers@okta.com if you would like to create a support ticket.

## License

Apache 2.0, see [LICENSE](LICENSE).
