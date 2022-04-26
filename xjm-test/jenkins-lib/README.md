# DSE Jenkins Lib

The DSE Jenkins Lib is a collection of functions and patterns that can be used
in `Jenkinsfile`s across all our projects in DSE. It leverages the Jenkins
[Shared Library][jenkins-shared-lib] feature which allows the loading of
arbitrary Groovy scripts and classes during the runtime of a Jenkins pipeline.

[jenkins-shared-lib]: https://www.jenkins.io/doc/book/pipeline/shared-libraries/

## To do

The project is still in it's infancy. See below for the work we still need to
do.

### Utilities

- [x] Checkmarx
  - [x] Scan Repository
- [ ] Artifactory
  - [x] Publish NPM Package
  - [ ] Publish Docker image
  - [x] Publish static file or archive
  - [ ] Download static file or archive
  - [ ] Publish test results
- [ ] Slack
  - [x] Failed build notification
  - [ ] Successful build notification
- [ ] Deployments
  - [ ] AWS SPD Deployment (non-prod)
  - [ ] OpenShift Deployment (non-prod)
  - [ ] GCP Deployment (non-prod)

### Pipelines

#### Project Builds

- [x] NPM Package Pipeline
- [x] Frontend SPA Pipeline
- [ ] API/BFF Docker Pipeline
- [ ] Jenkins Agent Docker Pipeline
- [ ] Process Orchestrator Plugin Pipeline
