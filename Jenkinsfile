pipeline {
    agent any

    environment {
        M2_HOME="/usr/local/apache-maven"
        M2="$M2_HOME/bin"
        PATH="$M2:$PATH"
        AWS_SECRET_ACCESS_KEY="4rDREaKtvfG0OTOnuesYuD+1Kgvhw5G4sYmQWv4k"
        AWS_ACCESS_KEY_ID="AKIAUNEWDFPB4E5VMLWT"
        AWS_DEFAULT_REGION="us-west-1"
    }


   stages {
   // Examine Environment
   // Build
   // Remove Build in case fail
   // Run Tests
   // build & tag
   // push to ecr
      stage('Build Jar') {
         steps {
            // Get some code from a GitHub repository
            git 'https://github.com/woodburydev/Chat-App-Sevice.git'
            // Run Maven
            sh "mvn clean package -D maven.test.failure.ignore=true "

         }

         post {
            // If Maven was able to run the tests, even if some of the test
            // failed, record the test results and archive the jar file.
            success {
               junit '**/target/surefire-reports/TEST-*.xml'
               archiveArtifacts 'target/*.jar'
            }
         }
      }

      stage("Build Container") {
        steps {
            sh "sudo -su jenkins"
            sh "aws configure"
            sh "docker build . -t 303109974979.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
            sh "/usr/local/bin/aws ecr get-login-password --region us-west-1 | docker login --username AWS --password-stdin 006256127606.dkr.ecr.us-west-1.amazonaws.com"
            sh "docker push 303109974979.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
            sh "/usr/local/bin/aws ecs update-service --force-new-deployment --service chat-application-service --cluster chat-application"
        }
      }
   }
}