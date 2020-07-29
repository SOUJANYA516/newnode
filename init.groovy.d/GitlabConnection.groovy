import com.dabsquared.gitlabjenkins.connection.*
import com.dabsquared.gitlabjenkins.connection.GitLabConnection
import jenkins.model.Jenkins

GitLabConnectionConfig descriptor = (GitLabConnectionConfig) Jenkins.getInstance().getDescriptor(GitLabConnectionConfig.class)

GitLabConnection gitLabConnection = new GitLabConnection('MyGitLab',
                                        'http://gitlab.ethan.svc.cluster.local:8084/gitlab',
                                        'gitlab_api_token',
                                        false,
                                        10,
                                        10)
descriptor.getConnections().clear()
descriptor.addConnection(gitLabConnection)
descriptor.save()