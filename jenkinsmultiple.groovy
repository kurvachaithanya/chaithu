//declarative pipeline
pipeline{
    agent any
    parameters {
        string(name: 'SERVERIPS', defaultValue: '', description: 'type SERVER_IP nme here')
        string(name: 'BUILD_NUMBER', defaultValue: '', description: 'type BUILD_NUM name here')
    }
    stages{
        stage("multiple servers"){
            steps{
                sh '''
                aws s3 cp s3://chaituart/hello-${BUILD_NUMBER}.war .
                ls -l
                IFS=',' read -ra ADDR <<< "${SERVERIPS}"
                for ip in \"${ADDR[@]}\";
                do 
                echo $ip
                 scp -o strictHostkeychecking=no -i /tmp/mine.pem hello-${BUILD_NUMBER}.war ec2-user@$ip:/var/lib/tomcat/webapps
                 ssh -o strictHostkeychecking=no -i /tmp/mine.pem ec2-user@$ip "hostname"
                  # process "$i" 
                done          
                '''
            }
        }
    }
}
