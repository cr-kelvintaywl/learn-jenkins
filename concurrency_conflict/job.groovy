node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run on',
            ),
            string(
                name: 'seconds',
                description: 'how long to wait (in seconds) before running task',
            ),
            string(
                name: 'filepath',
                description: 'name of file to store file',
                defaultValue: 'concurrency_conflict'
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
    cleanWs()
    stage('Create File') {
        sh "echo '${env.BUILD_NUMBER}' >> /tmp/${filepath}"
        sh "cat /tmp/${params.folder}"
    }
    stage('Sleep') {
        int sleepSecs = params.seconds as Integer
        sleep(sleepSecs)
    }
    stage('Use file') {
        sh "stat /tmp/${filepath}"
        sh "cat /tmp/${filepath}"
    }
}
