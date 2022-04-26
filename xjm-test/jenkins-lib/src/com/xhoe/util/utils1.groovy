package pipeline.module.utils1

import groovy.json.JsonOutput

def find_files(filetype) {

    def files = findFiles(glob: filetype)
    for (file in files) {
        println file.name
    }
}

def read_json_file(file_path) {
    def propMap = readJSON file: file_path
    propMap.each {
        println(it.key + " = " + it.value)
    }
}

def read_json_file2(json_string) {
    def propMap = readJSON text: json_string
    propMap.each {
        println(it.key + " = " + it.value)
    }
}

def write_json_to_file(input_json, tofile_path) {
    def input = ''
    if (input_json.toString().endsWith(".json")) {
        input = readJSON file: input_json
    } else {
        def jsonOutput = new JsonOutput()
        def new_json_object = jsonOutput.toJson(input_json)
        input = new_json_object
    }
    writeJSON file: tofile_path, json: input
}

def read_properties(properties_file) {
    def props = readProperties interpolate: true, file: properties_file
    props.each {
        println(it.key + " = " + it.value)
    }
}

def read_yaml_file(yaml_file) {
    def datas = ""
    if (yaml_file.toString().endsWith(".yml")) {
        datas = readYaml file: yaml_file

    } else {
        datas = readYaml text: yaml_file
    }
    datas.each {
        println(it.key + " = " + it.value)
    }
}

def write_to_yaml(map_data, yaml_path) {
    writeYaml file: yaml_path, data: map_data
}


return this;