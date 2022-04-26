pipeline {
    agent { label 'dind' }
    environment {
        JENKINS_VAULT_K8S_SERVICE_ACCOUNT_TOKEN=credentials('jenkins-vault-k8s-service-account-token')
        JENKINS_VAULT_DATAPOWER_CERT=credentials('jenkins-vault-datapower-cert')
        JENKINS_VAULT_DATAPOWER_KEY=credentials('jenkins-vault-datapower-key')
    }
    stages{
        stage('Environment'){
            steps {
                sh 'curl -k -X POST ${JENKINS_VAULT_ADDRESS}/v1/auth/kubernetes/cpaas_omni/dev/login -H "X-Vault-Namespace: ${JENKINS_VAULT_NAMESPACE}" -d \'{"role": \'\"${JENKINS_VAULT_ROLE}\"\',"jwt": \'\"${JENKINS_VAULT_K8S_SERVICE_ACCOUNT_TOKEN}\"\'}\''
            }    
        }
    }    
}
