import jenkins.model.*
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.dabsquared.gitlabjenkins.connection.*
import hudson.util.Secret

domain = Domain.global()
def credentials_store = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
def token = new Secret("5HxnAdK6yDA6BU5jo5hD")

def credentials = new GitLabApiTokenImpl(
  CredentialsScope.GLOBAL,
  "gitlab_api_token",
  "token for gitlab",
   token
)

credentials_store.addCredentials(domain, credentials)
