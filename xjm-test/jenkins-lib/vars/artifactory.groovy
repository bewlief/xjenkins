/**
 * Executing Artifactory Jenkins Plugin to upload artifacts.
 * https://www.jfrog.com/confluence/display/JFROG/Scripted+Pipeline+Syntax
 * https://www.jfrog.com/confluence/display/JFROG/Using+File+Specs
 */
def publish(targetPath, pattern, opt = [:]) {
  def conf = [repository: "XOPS-static"] << opt
  def server = Artifactory.server "gcp-artifactory"
  def uploadSpec = """{
    "files": [{
      "pattern": "${pattern}",
      "target": "artifactory/${conf.repository}/${targetPath}",
      "recursive": false,
      "props": "branch=${env.BRANCH_NAME}"
    }]
  }"""
  server.upload(uploadSpec)
}


def dockerPush(String tag) {
  def server = Artifactory.server "gcp-artifactory"
  def rtDocker = Artifactory.docker server: server, host: env.DOCKER_HOST

  rtDocker.push tag, "xops-images"
}
