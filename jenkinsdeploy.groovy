//declarative pipeline
pipeline{
    agent any
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'type branch name here')
        string(name: 'SERVER_IP', defaultValue: '', description: 'type SERVER_IP nme here')
        string(name: 'BUILD_NUM', defaultValue: '', description: 'type BUILD_NUM name here')
    }
    stages{
        stage("download artifcts"){
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
                
               scp -o StrictHostKeyChecking=no -i /tmp/mine.pem hello-${BUILD_NUM}.war ec2-user@${SERVER_IP}:/tmp
               ssh -i /tmp/mine.pem ec2-user@${SERVER_IP} "sudo cp /tmp/hello-${BUILD_NUM}.war /var/lib/tomcat/webapps"
                 
                """

            }
        }
    }
}