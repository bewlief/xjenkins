package com.xhoe.ops

def getHostIp(steps) {
    steps.sh(
            returnStdout: true,
            script: '''ip route|awk '/default/ { print $3 }' '''
    )
            .trim()
}