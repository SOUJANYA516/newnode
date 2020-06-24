import java.io.File
import jenkins.model.Jenkins
pm = Jenkins.instance.pluginManager
uc = Jenkins.instance.updateCenter

// Get the list of plugins already installed 
i = 0

def plugins = jenkins.model.Jenkins.instance.getPluginManager().getPlugins()
plugins.each {
    i++
    println "${i}: ${it.getDisplayName()} --> ${it.getShortName()} (${it.getVersion()}) - ${it.getDependencies()}"

}


// Install the plugin 

deployed = false

def activatePlugin(plugin) {
              if (! plugin.isEnabled()) {
              plugin.enable()
              deployed = true
              }
              plugin.getDependencies().each {
                             activatePlugin(pm.getPlugin(it.shortName))
              }
}


filename = '/var/jenkins_home/jobs/Load_Plugin/workspace' + '/New plugin.txt'
println filename
targetFile = new File(filename);
String givenPlugins = targetFile.text;
println givenPlugins;
pluginList = new ArrayList<String>();
pluginList.addAll(Arrays.asList(givenPlugins.split(",")));

for(String myPlugin : pluginList){
              if (! pm.getPlugin(myPlugin)) {
                            println ('Plugin to be installed : '+myPlugin)
							try {
                            deployment = uc.getPlugin(myPlugin).deploy(true)
                            
                            }catch(Exception e){
                            }	
                try {
                deployment.get()
              }catch(Exception e){
                            }
              }
              activatePlugin(pm.getPlugin(myPlugin))
}


if (deployed) {
              println ('deployed is true')
           //   Jenkins.instance.restart()
}