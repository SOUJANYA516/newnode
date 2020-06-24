#!groovy
import jenkins.model.*
import com.cloudbees.syslog.sender.UdpSyslogMessageSender
import jenkins.plugins.logstash.*
import jenkins.plugins.logstash.persistence.*
import jenkins.plugins.logstash.configuration.*
import com.cloudbees.syslog.MessageFormat;
import jenkins.plugins.logstash.persistence.LogstashIndexerDao.*;
import hudson.tools.ToolProperty

config = GlobalConfiguration.all().get(LogstashConfiguration.class);
config.setEnabled(true);
config.setEnableGlobally(true);
//println('-----> logstash start')
Logstash logstashtcp= new Logstash();
logstashtcp.setHost('52.53.125.114');
logstashtcp.setPort(6379);
config.setLogstashIndexer(logstashtcp);
config.save();
//println('-----> logstash end')

