def LABEL = "jenkins-baking-slave-${UUID.randomUUID().toString()}"
def TAG_NAME="${params.tagName}"
def BRANCH_VERSION="${params.branchName}"
def REPOSITORY_NAME="aegis-dashboard"
def PROJECT_KEY = "STINFRA"
def DOCKER_REGISTORY_URL = 'dev-registry.11stcorp.com'

def ECR_REGISTRY = '446804614856.dkr.ecr.ap-northeast-2.amazonaws.com'
def ECR_REPO = '11st-registry'

podTemplate(label:LABEL,
        containers: [
                containerTemplate(name: 'java', image: 'java:8-jdk', ttyEnabled: true, command: 'cat',
                        resourceRequestMemory: '6000Mi',
                        envVars: [
                                envVar(key: 'GRADLE_OPTS', value: '-Xmx512m'),
                                envVar(key: 'JVM_HEAP_MIN', value: '-Xmx512m'),
                                envVar(key: 'JVM_HEAP_MAX', value: '-Xmx512m')
                        ]),
                containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat'),
                containerTemplate(name: 'maven', image: 'maven:3.5.4-jdk-8-alpine', command: 'cat', ttyEnabled: true)
        ],
        volumes: [
                hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
        ]) {

    node(LABEL) {

        stage('checkout') {
            git( url: "https://github.com/11stcorp/sample-spring.git", branch: "master")
//            checkout([$class: 'GitSCM',
//                      branches: [[name: BRANCH_VERSION]],
//                      doGenerateSubmoduleConfigurations: false, extensions: [],
//                      submoduleCfg: [],
//                      userRemoteConfigs: [[url: "http://aegis@bitbucket.11stcorp.com/scm/${PROJECT_KEY}/${REPOSITORY_NAME}.git", credentialsId: 'git-common-user']]])
        }

        stage('build') {
            container('maven') {
                sh 'pwd'
                sh 'mvn clean install'
            }
        }

        stage('create image') {
            container('docker') {
                //cleanup current user docker credentials
                sh 'rm  ~/.dockercfg || true'
                sh 'rm ~/.docker/config.json || true'

                docker.withRegistry("https://" + ECR_REGISTRY, "ecr:ap-northeast-2:ecr-credential") {
                    def image = docker.build(ECR_REPO + ':sample-spring-v0.0.2')
                    image.push()
                }
//                withCredentials([[$class: 'UsernamePasswordMultiBinding',
//                                  credentialsId: 'dev-docker-registry',
//                                  usernameVariable: 'DOCKER_HUB_USER',
//                                  passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
//                    sh """
//                        pwd
//                        whoami
//                        docker login ${DOCKER_REGISTORY_URL} -u ${DOCKER_HUB_USER} -p '${DOCKER_HUB_PASSWORD}'
//                        docker build -t '${DOCKER_REGISTORY_URL}/11st/${REPOSITORY_NAME}:${TAG_NAME}' .
//                        docker push '${DOCKER_REGISTORY_URL}/11st/${REPOSITORY_NAME}:${TAG_NAME}'
//                    """
//                }
            }
        }
    }
}
