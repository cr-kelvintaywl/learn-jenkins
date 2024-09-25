node {
    stage('Inspect') {
        echo "Jenkins URL is ${env.JENKINS_URL}"
        echo "This build URL is ${env.BUILD_URL}"
    }
}
