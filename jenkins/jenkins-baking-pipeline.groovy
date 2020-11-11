def LABEL = "jenkins-baking-slave-${UUID.randomUUID().toString()}"
def TAG_NAME="${params.tagName}"
def BRANCH_VERSION="${params.branchName}"
def REPOSITORY_NAME="${params.repositoryName}"
def PROJECT_KEY = "cloud"
def DOCKER_REGISTORY_URL = 'dev-registry.11stcorp.com'

def CODE_BUILD_PROJECT_NAME = "sample-spring"
// def ECR_REGISTRY = '446804614856.dkr.ecr.ap-northeast-2.amazonaws.com'
// def ECR_REPO = '11st-registry'

podTemplate(label:LABEL,
        containers: [
                containerTemplate(name: 'java', image: 'java:8-jdk', ttyEnabled: true, command: 'cat',
                        resourceRequestMemory: '7000Mi',
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
            // git( url: "https://github.com/11stcorp/sample-spring.git", branch: "master")
            // git( url: "http://aegis@bitbucket.11stcorp.com/scm/cloud/sample-spring.git", branch: "master")
           checkout([$class: 'GitSCM',
                     branches: [[name: BRANCH_VERSION]],
                     doGenerateSubmoduleConfigurations: false, extensions: [],
                     submoduleCfg: [],
                     userRemoteConfigs: [[url: "http://aegis@bitbucket.11stcorp.com/scm/${PROJECT_KEY}/${REPOSITORY_NAME}.git", credentialsId: 'git-common-user']]])
        }

        stage('Build') {
            awsCodeBuild credentialsId: 'codebuild-user', credentialsType: 'jenkins', sourceControlType: 'jenkins',
                    projectName: "${CODE_BUILD_PROJECT_NAME}", region: 'ap-northeast-2',
                    envVariables: "[ { TAG_NAME, ${TAG_NAME} }]"
        }
    }
}
