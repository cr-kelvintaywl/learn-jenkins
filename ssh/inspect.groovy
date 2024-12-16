node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run Docker commands on',
            ),
            string(
              name: 'keypath',
              description: 'path to SSH key in keyholder_node to copy from'
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
    stage('Inspect SSH Key from node') {
        sh "head -c 50 ${params.keypath}"
        sh "md5 ${params.keypath} || echo 'no md5'"
    }
}
