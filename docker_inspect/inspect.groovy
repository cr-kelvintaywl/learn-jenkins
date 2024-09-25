node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run Docker commands on',
            ),
            string(
                name: 'image',
                description: 'docker image to pull',
            )
        ])
    ])
}

if (currentBuild.number == 1) {
    currentBuild.result = 'SUCCESS'
    currentBuild.displayName = 'Load Params'
    // exit early
    return
}

node(params.node) {
    stage('Docker inspect') {
        sh 'docker info'
        sh 'which jq'
        sh 'jq . /etc/docker/daemon.json'
    }
    stage('Docker pull') {
        sh "docker pull ${params.image}"
    }
}
