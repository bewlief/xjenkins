#!/groovy

def getRandom(int num) {
  return new java.util.Random().nextInt(10 * num)
}

def checkoutGit(repoUrl) {
  //git url: "${repoUrl}"
  return "new common ${repoUrl}"
}

def buildWar(javaHome,buildCmd) {
  sh "export JAVA_HOME=${javaHome} && ${buildCmd}"
}

return this;
