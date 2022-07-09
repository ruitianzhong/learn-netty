## learn-netty

### Introduction

To learn the concept and the usage of `netty`.

### Author

`ruitianzhong`

### To-do List

+ **Authentication** with cookie
+ More **algorithms** for load balance
+ `README` for the quick start of the server
+ Fix bug for configuration reload
+ Try to integrate the `Redis` into the server with **easy** configuration

### Features

- [x] Detect the configuration changes in configuration file like
  `application.properties` automatically and reload it without restarting the
  server.
- [x] Load Balance
- [x] Support `GET` and `REDIRECT` in Server itself, and support
  **all** HTTP requests for load balancer.
