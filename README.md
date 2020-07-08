NodeJs Microservice cartridge:
NodeJs cartridge is aimed at creating a reusable pipeline for all the nodejs projects, using the clove platform.

All the necessary plugins and settings for cartridge are automated. 

All using Kubernetes Deployments

Pre-requisites:

Sonar: Create a token from sonar server and add that token inside jenkins configuration.

Private registey: To push the image in private registry (Azure),add the user name and the password in jenkins credentials.

To pull the base image from private registry we have to create a secret in bastion and add that secret with that pod deployment.

The following tables list the configurable parameters of the Jenkinsfile and their default values.

JenkinsFile

| Parameter                         | Description                                                                            | Default                                   
| --------------------------------- | ---------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| `GIT_URL`                         | SCM URL. Change URL with respective SCM/innersource URL                                | `http://gitlab.ethan.svc.cluster.local:8084/gitlab/root/microservices_nodejs_cartridge.git`|                                 
| `GIT_CREDENTIAL_ID`               | To authenicate SCM repo for cloning the code                                           | `gitlab`                             |
| `GIT_BRANCH`                      | Cloning the code from different git branch                                             | `master`                                  |
| `COMPONENT_KEY`                   | Application name                                                                       | `Frontend`                                   |
| `SONAR_HOST_URL`                  | Sonar service URL.To connect jenkins with Sonar server                                 | `http://sonar.ethan.svc.cluster.local:9001/sonar`|
| `String metricKeys`               | To publish sonar data in jenkins console                                               | `coverage,code_smells,bugs,vulnerabilities,sqale_index,tests,ncloc,quality_gate_details,duplicated_lines_density`|
| `SONAR_UI`                        | API call from jenkins to sonar to publish the data                                     | `http://sonar.ethan.svc.cluster.local:9001/sonar/api/measures/component?metricKeys=`|
| `SONAR_PLUGIN`                    | version of sonar plugin                                                                | `sonar-maven-plugin:3.2`|
| `DOCKER_HUB_REPO_URL`             | Private registry URL to push the base docker image                                     | `aiindevops.azurecr.io`|
| `DOCKER_CREDENTIAL_ID`            | To authenticate private registry (Azure)                                               | `dockerhub`            |
| `DOCKER_IMAGE_NAME`               | Base image name as application name                                                    | `front-end`                | 
| `DOCKER_HUB_ACCOUNT`              | Private registry account                                                               | `aiindevops.azurecr.io`|
| `IMAGE_TAG`                       | Base application image version                                                         | `0.0.0.2`
| `K8S_DEPLOYMENT_NAME`             | Application name                                                                       | `front-end`|
 


Once the pipeline is success, the code of the application will be deployed on kuberentes.