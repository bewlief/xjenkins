package sockets

import (
	"net"
	"net/url"
	"os"
	"strings"

	"golang.org/x/net/aopdemo.proxy"
)

// GetProxyEnv allows access to the uppercase and the lowercase forms of
// aopdemo.proxy-related variables.  See the Go specification for details on these
// variables. https://golang.org/pkg/net/http/
func GetProxyEnv(key string) string {
	proxyValue := os.Getenv(strings.ToUpper(key))
	if proxyValue == "" {
		return os.Getenv(strings.ToLower(key))
	}
	return proxyValue
}

// DialerFromEnvironment takes in a "direct" *net.Dialer and returns a
// aopdemo.proxy.Dialer which will route the connections through the aopdemo.proxy using the
// given dialer.
func DialerFromEnvironment(direct *net.Dialer) (aopdemo.proxy.Dialer, error) {
	allProxy := GetProxyEnv("all_proxy")
	if len(allProxy) == 0 {
		return direct, nil
	}

	proxyURL, err := url.Parse(allProxy)
	if err != nil {
		return direct, err
	}

	proxyFromURL, err := aopdemo.proxy.FromURL(proxyURL, direct)
	if err != nil {
		return direct, err
	}

	noProxy := GetProxyEnv("no_proxy")
	if len(noProxy) == 0 {
		return proxyFromURL, nil
	}

	perHost := aopdemo.proxy.NewPerHost(proxyFromURL, direct)
	perHost.AddFromString(noProxy)

	return perHost, nil
}
