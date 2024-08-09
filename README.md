# rain
A collection of automation functions for Ktor Server applications

- Automatic Routing
    1. Mark a class with the `@Service(path: String)` annotation
    2. Mark any routes with a `@RequestMethod` annotation such as `@Get` or `@Post`
    3. Mark all parameters with a `@RequestParameter` annotation such as `@Body` or `@Query`
    4. Return a type that implements `BaseResponse<T : BaseResponse>`

``` Kotlin
// Application.kt
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args) // Use whichever server you'd like
}

fun Application.module() {
    installServiceAnnotatedRoutes()
}
```

- Basic Global Error Handling
     1. Currently returns a Json response unless the request was formatted in a way which the service could not respond.
