node {
    checkout scm
    def params = load pwd() + '/separate_params/params.groovy'

    properties([
        parameters(params.all())
    ])

    stage('verify') {
        sh 'echo foobar value is $params.foobar'
    }
}

