node {
    stage('Stash') {
        sh('touch dummy.txt')
        sh('echo hello > dummy.txt')
        stash(
            name: 'dummy',
            includes: 'dummy.txt'
        )
        sh('cat dummy.txt')
    }
    stage('Modify') {
        sh('echo goodbye > dummy.txt')
        sh('cat dummy.txt')
    }
    stage('Unstash') {
        unstash 'dummy'
        sh('cat dummy.txt')
    }
}
