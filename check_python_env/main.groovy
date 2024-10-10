node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run Python commands on',
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
    stage('Python3 env inspect') {
        sh 'which python3'
        sh 'python3 --version'
        sh 'pip3 list'
    }
    stage('Save Python env settings') {
        sh 'python3 --version | awk \'{print $2}\' > python-version.txt'
        sh 'pip3 freeze > requirements.txt'

        archiveArtifacts artifacts: '*.txt', fingerprint: true
    }
}
