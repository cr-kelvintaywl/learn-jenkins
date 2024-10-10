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
}
