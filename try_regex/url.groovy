import java.net.URI


node {
    stage('Inspect') {
        jenkinsURL = new URI(env.JENKINS_URL)
        host = jenkinsURL.host

        isProdution = host.contains('prod')
        if (isProduction) {
            echo 'This Jenkins is for PRODUCTION!'
        } else {
            echo "This Jenkins is @ ${jenkinsURL}"
        }
    }
}
