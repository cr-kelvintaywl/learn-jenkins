node {
    checkout scm
    def params = load pwd() + '/params.groovy'

    properties([
        parameters(params.all())
    ])

    stage('verify') {
        sh 'echo foobar value is $params.foobar'
    }
}

