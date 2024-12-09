node {
    checkout scm
    properties([
        parameters([
            string(
                name: 'node',
                description: 'which node to run Docker commands on',
            ),
            string(
                name: 'keyholder_node',
                description: 'which node to copy key from',
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
              description: 'path to SSH key in keyholder_node to copy from'
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

node(params.keyholder_node) {
    stage('Copy SSH Key from node') {
        stash(
            name: 'ssh_key',
            includes: "${params.keypath}",
        )
        // set key file name as env var
        ssh_key_filename = sh(
            script: "basename ${params.keypath}",
            returnStdout: true
        ).trim()
        env.ssh_key_filename = ssh_key_filename
    }
}

node(params.node) {
    // clean workspace after pipeline, so we remove the copied SSH key
    cleanWs()

    stage('Inspect') {
        unstash 'ssh_key'
        sh "ssh -i ./${env.ssh_key_filename} -o StrictHostKeyChecking=no ${params.user}@${params.ip} ${params.cmd}"
    }
}
