import hudson.model.*
import hudson.plugins.sonar.SonarGlobalConfiguration
import hudson.plugins.sonar.*;
  
SonarGlobalConfiguration global = Hudson.instance.getDescriptorByType(SonarGlobalConfiguration.class)
def sonar_installations = global.getInstallations();
def custom_sonar_inst = new SonarInstallation("SonarQube", "http://sonar/sonar", null, null, null, null, null);


// Only add Sonar if it does not exist - do not overwrite existing config

def sonar_inst_exists = false
sonar_installations.each {
    installation = (SonarInstallation) it
    if (custom_sonar_inst.getName() == installation.getName()) {
        sonar_inst_exists = true
        println("Found existing installation: " + installation.getName())
    }
}

// setting the sonar name 
if (!sonar_inst_exists) {
global.setInstallations(custom_sonar_inst)
    global.save();
}
