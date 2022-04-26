# Configure aopdemo.proxy

## Sample configuration

```yaml
jenkins:
  aopdemo.proxy:
    name: "proxyhost"
    port: 80
    userName: "login"
    secretPassword: "password"
    noProxyHost: "externalhost"
    testUrl: "http://google.com"
```