node {
    def project = 'sciencebase'
    def appName = 'upload-status'
    def svcName = 'upload-status'
    def imageTag = "${project}/${appName}:${env.BRANCH_NAME}.${env.BUILD_NUMBER}"
    def namespace = getNamespace(env.BRANCH_NAME, appName)
    def environment = environmentFromBranchName(env.BRANCH_NAME)

//    hipchatSend(color: 'GREEN', message: "Starting build #${env.BUILD_NUMBER} for branch ${env.BRANCH_NAME} of ${appName}", notify: true, v2enabled: true)

    if (env.BRANCH_NAME != 'master') {
        stage "Deploy Config"
        echo "Deploy config with environment: ${environment} namespace: ${namespace}"
        build job: 'upload-status-config', parameters: [string(name: 'environment', value: environment), string(name: 'namespace', value: namespace)]
    }

    checkout scm

    try {
        stage("Build and Stage Docker directory") {
            sh("./gradlew build")
        }

        stage("SonarQube Analysis") {
            withSonarQubeEnv('default') {
                sh './gradlew sonarqube'
            }
        }

        stage("Build and push image") {
            sh("docker build -t ${imageTag} .")
        }

        stage("Push image") {
            withCredentials([[$class          : 'UsernamePasswordMultiBinding', credentialsId: 'docker-hub',
                              usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD']]) {
                sh('docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD')
            }
            sh("docker push ${imageTag}")
//            hipchatSend(color: 'GREEN', message: "pushed ${imageTag} to docker registry", notify: true, v2enabled: true)
        }

        stage("Deploy Application") {
            switch (env.BRANCH_NAME) {
            // Roll out to staging
            /*case "staging":
            // Change deployed image in staging to the one we just built
            sh("sed -i.bak 's#sciencebase/upload-status:0.1#${imageTag}#' ./k8s/staging/*.yaml")
            sh("kubectl --namespace=$namespace apply -f k8s/services/")
            sh("kubectl --namespace=$namespace apply -f k8s/staging/")
            def deployUrl =  sh(returnStdout: true, script: "echo http://`kubectl --namespace=$namespace get service/${svcName} --output=json | jq -r '.status.loadBalancer.ingress[0].hostname'` > ${svcName}").trim()
            hipchatSend(color: 'GREEN', message: "Deployed ${appName}. Access via: ${deployUrl}", notify: true, v2enabled: true)
            echo "${deployUrl}"
            break*/

            // Roll out to production
                case "master":
                    // Change deployed image in staging to the one we just built
                    sh("sed -i.bak 's#sciencebase/upload-status:0.1#${imageTag}#' ./k8s/production/*.yaml")
                    sh("kubectl --namespace=$namespace apply -f k8s/production/")
                    def deployUrl = sh(returnStdout: true, script: "echo http://`kubectl --namespace=$namespace get service/${svcName} --output=json | jq -r '.status.loadBalancer.ingress[0].hostname'` > ${svcName}").trim()
                    hipchatSend(color: 'GREEN', message: "Deployed ${appName}. Access via: ${deployUrl}", notify: true, v2enabled: true)
                    echo "${deployUrl}"
                    break

            // Roll out a dev environment
                default:
                    // Create namespace if it doesn't exist
                    sh("kubectl get ns ${namespace} || kubectl create ns ${namespace}")
                    // Don't use public load balancing for development branches
                    sh("sed -i.bak 's#sciencebase/upload-status:0.1#${imageTag}#' ./k8s/development/*.yaml")
                    sh("kubectl --namespace=$namespace apply -f k8s/development/")
                    echo 'To access your environment run `kubectl proxy`'
                    echo "Then access your service via http://localhost:8001/api/v1/proxy/namespaces/${namespace}/services/${svcName}:80/"
                    hipchatSend(color: 'GREEN', message: "Deployed ${appName}. Access via `kubectl proxy`. Then access your service via http://localhost:8001/api/v1/proxy/namespaces/${namespace}/services/${svcName}:80/", notify: true, v2enabled: true)
            }
        }
    } catch (error) {
        hipchatSend(color: 'RED', message: "Build #${env.BUILD_NUMBER} for branch ${env.BRANCH_NAME} of ${appName} failed. Error: ${error.toString()}")
    }
}

def environmentFromBranchName(String branchName) {
    if (branchName == 'master') {
        return 'production'
    }

    return 'development'
}

def getNamespace(String branchName, String appName) {
    if (branchName == 'master') {
        return "pipeline"
    }

    return "${appName}-${branchName}"
}