def downstreamJobResult

stage('Trigger Downstream Job') {
    // Trigger the downstream job and wait for its completion
    downstreamJobResult = build job: '../separate_params/main', wait: true, parameters: [string(name: 'fizzbuzz', value: '2')]

    // Access the downstream job's result
    echo "Downstream job result: ${downstreamJobResult.result}"

    // Handle the result
    if (downstreamJobResult.result != 'SUCCESS') {
        error "Downstream job failed with result: ${downstreamJobResult.result}"
    }
}

node {
    stage('Additional Processing') {
        echo "Accessing downstream job result in node: ${downstreamJobResult.result}"
    }
}
