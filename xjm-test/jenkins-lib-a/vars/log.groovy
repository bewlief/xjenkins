def info(String msg) {
    echo "INFO: $msg"
}

def notice(String msg) {
    echo "NOTICE: $msg"
}

def warn(String msg) {
    echo "WARNING: $msg"
}

def error(String msg) {
    error " $msg"
}

def call() {
    echo "in log, no return this"
}


// return this和call()必须要至少有一个
return this