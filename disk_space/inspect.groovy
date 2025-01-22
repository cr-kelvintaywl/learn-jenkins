node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to inspect',
            ),
            string(
                name: 'paths',
                description: 'which paths to inspect (comma-separated)',
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
    stage('Inspect disk space') {
        sh('df -ha')
        paths = params.paths.split(',').join(' ')
        sh("du -sh ${paths}")
    }
}
