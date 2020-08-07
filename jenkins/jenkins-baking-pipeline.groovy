def LABEL = "jenkins-baking-slave-${UUID.randomUUID().toString()}"
def TAG_NAME="${params.tagName}"
def BRANCH_VERSION="${params.branchName}"
def REPOSITORY_NAME="aegis-dashboard"
def PROJECT_KEY = "STINFRA"
def DOCKER_REGISTORY_URL = 'dev-registry.11stcorp.com'

podTemplate(label:LABEL,
        containers: [
                containerTemplate(name: 'java', image: 'java:8-jdk', ttyEnabled: true, command: 'cat',
                        envVars: [
                                envVar(key: 'GRADLE_OPTS', value: '-Xmx512m'),
                                envVar(key: 'JVM_HEAP_MIN', value: '-Xmx512m'),
                                envVar(key: 'JVM_HEAP_MAX', value: '-Xmx512m')
                        ]),
                containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat')
        ],
        volumes: [
                hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
        ]) {

    node(LABEL) {

        stage('checkout') {
            checkout([$class: 'GitSCM',
                      branches: [[name: BRANCH_VERSION]],
                      doGenerateSubmoduleConfigurations: false, extensions: [],
                      submoduleCfg: [],
                      userRemoteConfigs: [[url: "http://aegis@bitbucket.11stcorp.com/scm/${PROJECT_KEY}/${REPOSITORY_NAME}.git", credentialsId: 'git-common-user']]])
        }

        stage('build') {
            container('java') {
                sh 'pwd'
                sh './gradlew -x test clean build'
            }
        }

        stage('create image') {
            container('docker') {
                withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                  credentialsId: 'dev-docker-registry',
                                  usernameVariable: 'DOCKER_HUB_USER',
                                  passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
                    sh """
                        pwd
                        whoami
                        docker login ${DOCKER_REGISTORY_URL} -u ${DOCKER_HUB_USER} -p '${DOCKER_HUB_PASSWORD}'
                        docker build -t '${DOCKER_REGISTORY_URL}/11st/${REPOSITORY_NAME}:${TAG_NAME}' .
                        docker push '${DOCKER_REGISTORY_URL}/11st/${REPOSITORY_NAME}:${TAG_NAME}'
                    """
                }
            }
        }
    }
}
