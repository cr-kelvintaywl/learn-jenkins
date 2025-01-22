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
            ),
            booleanParam(
                name: 'save_image',
                description: 'Save image as tar (artifact)',
                defaultValue: true,
            ),
            booleanParam(
                name: 'reload_parameters',
                description: 'Reload job parameters from git and exit.',
                defaultValue: true,
            ),
        ])
    ])
}

if (params.reload_parameters) {
    currentBuild.result = 'SUCCESS'
    currentBuild.displayName = 'Load Params'
    // exit early
    return
}

node(params.node) {
    stage('Docker inspect') {
        sh 'docker info'
        sh 'docker system df'
        sh 'which jq'
        sh 'jq . /etc/docker/daemon.json || echo "cannot find daemon config JSON"'
        sh 'jq . ~/.docker/config.json || echo "cannot find CLI config JSON"'
    }
    stage('Docker pull') {
        sh "docker pull ${params.image}"
        sh 'docker image ls'
        if (params.save_image) {
            sh "docker save --output docker_image.tar ${params.image}"
            archiveArtifacts(artifacts: 'docker_image.tar')
        }
    }
}
