node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run on',
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
    customWorkspace = "workspace/${env.JOB_NAME}/${env.BUILD_NUMBER}"
    ws(customWorkspace) {
        cleanWs()
        stage('Debug - Check workspace') {
            sh 'pwd'

            sh 'echo hello > hello.txt'
            sh 'stat hello.txt'
        }
    }
}
