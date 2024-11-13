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
    cleanWs()

    stage('Env inspect') {
        sh 'echo $JENKINS_HOME'
        sh 'echo $WORKSPACE'
        sh 'echo $BUILD_NUMBER'
        sh 'echo $BUILD_URL'
        sh 'echo $JOB_NAME'
    }

    stage('List builds') {
        job_path = "${JOB_NAME}".replaceAll('/', '/jobs/')
        sh "ls -lah ${JENKINS_HOME}/jobs/${job_path}/builds"
    }

    stage('Generate artifact') {
        sh 'head -c 2097152 </dev/urandom >test2MB.txt'
        archiveArtifacts artifacts: '*.txt', fingerprint: true
    }
    stage('Verify artifact path') {
        job_path = "${JOB_NAME}".replaceAll('/', '/jobs/')
        sh "ls -lah ${JENKINS_HOME}/jobs/${job_path}/builds/${BUILD_NUMBER}/archive/*"
    }
}
