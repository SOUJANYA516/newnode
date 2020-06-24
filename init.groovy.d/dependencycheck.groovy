import hudson.tools.ToolProperty
import org.jenkinsci.plugins.DependencyCheck.*
import jenkins.model.*
import hudson.model.*
import hudson.tools.*
import org.jenkinsci.plugins.DependencyCheck.tools.DependencyCheckInstaller


  def dependency_check_name = "DependencyCheck"
def dependency_check_version = '5.3.1'
 def DependencyCheckInstaller = new DependencyCheckInstaller(dependency_check_version)
def installSourceProperty = new InstallSourceProperty([DependencyCheckInstaller])

// this is to add the name 'dependency check' in the global tool
desc = Jenkins.instance.getDescriptor(org.jenkinsci.plugins.DependencyCheck.tools.DependencyCheckInstallation);
installation = new org.jenkinsci.plugins.DependencyCheck.tools.DependencyCheckInstallation(dependency_check_name, "",[installSourceProperty])

desc.setInstallations(installation);
desc.save()