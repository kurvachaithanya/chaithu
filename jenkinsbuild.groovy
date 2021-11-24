pipeline{
    agent any
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'type branch name here')
    }
    stages{
        stage("clone the code"){
            steps{
                println "cloning the code from git hub"
            }
        }
        stage("build code"){
            steps{
                println "build the code using maven"
                sh "maven clean package"
                sh "ls -l"
            }
        }
        stage("save artifacts in s3"){
            steps{
                println "upload artifacts to s3"
                sh "echo $BUILD_NUMBER"
                sh "aws s3 cp target/hello-${BUILD_NUMBER}.war s3://chaituart/"
            }
        }
    }
}