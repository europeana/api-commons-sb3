# API Commons for Spring Boot v3 - Error module

This module can be configured with the following options

  - `server.error.see-also`           optional, default is empty and field won't be displayed
  - `server.error.include-message`    optional, include message field or not, values are from Spring Boot; `never`, `always` or `on_param`, default is `always`
  - `server.error.include-stacktrace` optional, include stacktrace field or not, values are from Spring Boot; `never`, `always` or `on_param`, default is `on_param`
  - `server.error.include-exception`  optional, true or false, default is true
