//def signature = 'new groovy.json.JsonSlurperClassic'
//org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval.get().approveSignature(signature)
println 'Hello from the DSL script!'; 
def existFolder = new File( "${WORKSPACE}" )
def existSubFolder = new File( "${WORKSPACE}/${APPLICATION_NAME}" )

// to check whether Workspace exist or not 
if( !existFolder.exists() ) {
println existFolder;
println 'Workspace does not exist'; 
  // create the folder with provided workspace name 
 folder(WORKSPACE){ } 
}

if( !existSubFolder.exists() ) {
  println existSubFolder;
println 'Sub folder does not exist';
   String subFolder="${WORKSPACE}/${APPLICATION_NAME}"
   folder (subFolder)
}

def concatVar = "${WORKSPACE}/${APPLICATION_NAME}/${JOB_NAME}"
// create pipeline job
pipelineJob(concatVar) 
{ definition 
	{
	cpsScm{
		scm{ 
			git 
				{ remote { 
					url(GIT_REPO)
					credentials(GIT_USER) }
					branch(BRANCH) 
					configure = null } 
			} 
		} 
	triggers {
	scm('H/1 * * * *') } 
	} 
}

