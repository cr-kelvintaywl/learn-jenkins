node {
    checkout scm
    // NOTE: `load` is a Jenkinsfile-only construct;
    // This is not a Groovy-specific function
    def params = load pwd() + '/separate_params/params.groovy'

    properties([
        parameters(params)
    ])

    stage('verify') {
        sh "echo foobar value is ${params.foobar}"
    }
}

