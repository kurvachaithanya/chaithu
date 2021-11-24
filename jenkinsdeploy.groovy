//declarative pipeline
pipeline{
    agent any
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'type branch name here')
        string(name: 'SERVER_IP', defaultValue: '', description: 'type SERVER_IP name here')
        string(name: 'BUILD_NUM', defaultValue: '', description: 'type BUILD_NUM name here')
    }
    stages{
        stage("download artifacts"){
            steps{
                sh """
                aws s3 ls s3://chaituart
                aws s3 cp s3://chaituart/hello-${BUILD_NUM}.war .
                """
            }
        }
        stage("copy artifact to remote server"){
            steps{
                sh """
                ssh -i /tmp/mine.pem ec2-user@${SERVER_IP} "systemctl status tomcat"
                scp -i /tmp/mine.pem hello-${BUILD_NUM}.war ec2-user@${SERVER_IP}:/tmp
                """

            }
        }
    }
}