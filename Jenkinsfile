def label = "mypod-${UUID.randomUUID().toString()}"
def serviceaccount = "jenkins-admin"

podTemplate(label: label, serviceAccount: serviceaccount, containers: [
	containerTemplate(name: 'nodejs', image: 'localhost:32121/root/docker_registry/node:10-alpine', ttyEnabled: true, command: 'cat'),
	containerTemplate(name: 'git-secrets', image: 'localhost:32121/root/docker_registry/aiindevops.azurecr.io/git-secrets:0.1', ttyEnabled: true, alwaysPullImage: true, command: 'cat'),
	containerTemplate(name: 'clair-scanner', image: 'localhost:32121/root/docker_registry/aiindevops.azurecr.io/clair-scanner:0.1', ttyEnabled: true, alwaysPullImage: true, command: 'cat', ports: [portMapping(name: 'clair-scanner', containerPort: '9279')],
		volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]),
    containerTemplate(name: 'py-python', image: 'localhost:32121/root/docker_registry/aiindevops.azurecr.io/py_pandas:latest', ttyEnabled: true, command: 'cat',
		volumes: [hostPathVolume(hostPath: '/*.py', mountPath: '/*.py')]),		
	containerTemplate(name: 'kubectl', image: 'localhost:32121/root/docker_registry/aiindevops.azurecr.io/docker-kubectl:19.03-alpine', ttyEnabled: true, command: 'cat',
		volumes: [secretVolume(secretName: 'kube-config', mountPath: '/root/.kube')]),
    containerTemplate(name: 'docker', image: 'localhost:32121/root/docker_registry/docker:1.13', ttyEnabled: true, command: 'cat')],
		volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')],
	imagePullSecrets: [ 'gcrcred' ]
) {
    node(label) {
        
        def GIT_URL= 'http://gitlab.ethan.svc.cluster.local:8084/gitlab/root/microservices_nodejs_cartridge.git'
		def GIT_CREDENTIAL_ID ='gitlab'
		def GIT_BRANCH='master'
	
		/*** For Java - COMPONENT_KEY value should be same as what is given in pom file groupId:artifactID 
			 For NodeJs - COMPONENT_KEY value should be same as what is given as ProjectName in in sonar.properies file ***/		
		def COMPONENT_KEY='Frontend';
		def rootDir = pwd()	
		
		/*** Below variables used in the sonar maven configuration ***/
		def SONAR_SCANNER='org.sonarsource.scanner.maven'
		def SONAR_PLUGIN='sonar-maven-plugin:3.2'
		def SONAR_HOST_URL='http://sonar.ethan.svc.cluster.local:9001/sonar'
		
		/***  DOCKER_HUB_REPO_URL is the URL of docker hub ***/
		def REGISTRY_NAME = 'aiindevops.azurecr.io' 
		def REPO_NAME = 'front-end'
		def IMAGE_TAG = "demo-dev-${env.BUILD_NUMBER}"
		def IMAGE_NAME = "${REGISTRY_NAME}/${REPO_NAME}:${IMAGE_TAG}"
		def CLAIR_TAG = "clair-${env.BUILD_NUMBER}.html"
		def DEPENDENCY_PATH= '/home/jenkins/agent/workspace/demo_microservices/sockshop-frontend-nodejs'
	 
		stage('Git Checkout') {
			git branch: GIT_BRANCH, url: GIT_URL,credentialsId: GIT_CREDENTIAL_ID
			def function = load "${WORKSPACE}/JenkinsFunctions_NodeJs.groovy"
			def Nap = load "${WORKSPACE}/git_scan_nonallowed.groovy"
			def Ap = load "${WORKSPACE}/git_scan_allowed.groovy"
						
			
			stage('Git-Secrets') {
				container('git-secrets') {
					Nap.nonAllowedPattern()
					Ap.AllowedPattern()	
					sh 'git secrets --scan'
				}
			}
           
	        stage('Build  Application') {
                container('nodejs') {
                  function.buildMethod()
                }
            }
	        stage('Unit Test') {
				container('nodejs') {
					function.testMethod()
				}
			} 
            
			stage('Publish Test Result'){
                junit 'test-results.xml'
            }
            stage('SonarQube Analysis') {
				withCredentials([usernamePassword(credentialsId: 'SONAR', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]){
				    withSonarQubeEnv('SonarQube') {
					  function.sonarMethod()                         
					}  
				}
			}
		}
			
         stage('Dependency Check') {
				sh 'mkdir -p build/owasp'
                dependencyCheck additionalArguments: '--project sockshop-frontend --scan ./ --out build/owasp/dependency-check-report.xml --format XML', odcInstallation: 'dependency'
				dependencyCheckPublisher pattern: 'build/owasp/dependency-check-report.xml'
				withCredentials([usernamePassword(credentialsId: 'NEXUS', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh ("curl --upload-file build/owasp/dependency-check-report.xml -u $USERNAME:$PASSWORD -v http://nexus.ethan.svc.cluster.local:8083/nexus/repository/maven-releases/frontend/")
            
				}   
			}
 
		stage('Docker Image Build') {
			container('docker'){
				sh ("docker build -t ${IMAGE_NAME} .")
			}
		}
			
		
		stage('Docker Image Scan') {
			container('kubectl') {
			  sh("cat ${WORKSPACE}/clair-scanner.yaml | sed 's/{{parm}}/${label}/g' | kubectl apply -f - ") //create jenkins-slave service 
			}
			container('clair-scanner') {
		      try {
		        sh "echo clair-test"
				sh "clair-scanner -w 'mywhitelist.yaml' -c 'http://clair:6060' -r 'clair.json' --ip='${label}' -t 'Low' ${IMAGE_NAME}"
				container('py-python'){
				  sh ("python report.py ${CLAIR_TAG}")
				  archiveArtifacts allowEmptyArchive: true, artifacts: "${CLAIR_TAG}", onlyIfSuccessful: true
                }
			  } catch(Exception e){
				  container('py-python'){
                    sh ("python report.py ${CLAIR_TAG}")
				    archiveArtifacts allowEmptyArchive: true, artifacts: "${CLAIR_TAG}", onlyIfSuccessful: true
				  }
				}
		    }
			container('kubectl') {
			  sh("kubectl delete svc ${label}") //delete jenkins-slave service  
		    }
		}
	        
	    stage('Publish Docker Image') {
            container('docker') {
			  withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub', usernameVariable: 'USER', passwordVariable: 'PASSWORD']]) {
                    sh ('docker login -u ${USER} -p ${PASSWORD} https://aiindevops.azurecr.io')
                    sh ("docker push ${IMAGE_NAME}")
			  }
			}
		}
	    stage('Upload Result to Nexus'){
			container('py-python'){
		      withCredentials([usernamePassword(credentialsId: 'NEXUS', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
				sh ("curl --upload-file ${CLAIR_TAG} -u $USERNAME:$PASSWORD -v http://nexus.ethan.svc.cluster.local:8083/nexus/repository/maven-releases/frontend/")
			  }
			}
        }
        
	}
}
