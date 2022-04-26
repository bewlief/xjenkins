package com.xhoe.ops


import java.time.LocalDate;

def find_files(filetype) {
    def files = findFiles(glob: filetype)
    for (file in files) {
        println file.name
    }
}

def java_func(str1) {
    LocalDate ld = LocalDate.now()
    println ld.toString() + "," + str1
}

return this;