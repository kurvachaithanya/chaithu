//declarative pipeline
pipeline{
    agent any
    parameters {
        string(name: 'SERVERIPS', defaultValue: '', description: 'type SERVER_IP nme here')
        string(name: 'BUILD_NUM', defaultValue: '', description: 'type BUILD_NUM name here')
    }
    stages{
        stage("multiple servers"){
            steps{
                sh '''
                aws s3 cp s3://chaituart/hello-${BUILD_NUM}.war .
                ls -l
                IFS=',' read -ra ADDR <<< "${SERVERIPS}"
                for ip in \"${ADDR[@]}\";
                do 
                echo $ip
                 scp -o StrictHostkeychecking=no -i /tmp/mine.pem hello-${BUILD_NUM}.war ec2-user@$ip:/var/lib/tomcat/webapps
                 ssh -o StrictHostkeychecking=no -i /tmp/mine.pem ec2-user@$ip "hostname"
                  # process "$i" 
                done          
                '''
            }
        }
    }
}
