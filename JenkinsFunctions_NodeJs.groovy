// Below methods are used to build and test the NodeJs code 
def buildMethod() {
    println('NodeJsBuildMethod enter');
	sh 'npm install';
    sh 'npm run';
    sh 'nohup npm start &';
	sh 'npm run-script coverage';
    println('nodeJsBuildMethod exit');
}

def testMethod() {
    println('NodeJsTestMethod enter');
    sh 'npm test' ;
    sh './node_modules/.bin/mocha test/*_test.js test/api/*_test.js  --reporter mocha-junit-reporter'
    println('NodeJsTestMethod exit');
}

def sonarMethod() {
	println('Sonar Method enter');
    def scannerHome = tool 'Sonar Scanner';
    sh "${scannerHome}/bin/sonar-scanner -Dsonar.login=$USERNAME -Dsonar.password=$PASSWORD";
	println('Sonar Method exit');
	
}

return this // this is important to return