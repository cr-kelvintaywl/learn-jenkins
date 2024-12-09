node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run Docker commands on',
            ),
            string(
                name: 'ip',
                description: 'ip or address to telnet',
            ),
            string(
              name: 'user',
              description: 'user to log in as'
            ),
            string(
              name: 'keypath',
              description: 'path to SSH key'
            ),
            string(
              name: 'cmd',
              description: 'command to run remotely'
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
    stage('Inspect') {
        sh "ssh -i ${params.keypath} -o StrictHostKeyChecking=no ${params.user}@${params.ip} ${params.cmd}"
    }
}
