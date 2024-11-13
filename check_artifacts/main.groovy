node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run Python commands on',
                defaultValue: 'master',
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
    stage('Env inspect') {
        sh 'echo $JENKINS_HOME'
        sh 'echo $WORKSPACE'
        sh 'echo $BUILD_NUMBER'
        sh 'echo $BUILD_URL'
        sh 'echo $JOB_NAME'
    }

    stage('List builds') {
        job_path = "${JOB_NAME}".replaceAll('/', '/jobs/')
        sh "ls -lah ${job_path}/builds"
    }
}
