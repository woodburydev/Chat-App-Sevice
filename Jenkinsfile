pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:$PATH"
        AWS_SECRET_ACCESS_KEY=credentials("AWS_SECRET_ACCESS_KEY")
        AWS_ACCESS_KEY_ID=credentials("AWS_ACCESS_KEY_ID")
        AWS_DEFAULT_REGION=credentials("AWS_DEFAULT_REGION")
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
            sh "sudo mvn clean package -D maven.test.failure.ignore=true "

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
            sh "docker build . -t chat-application:latest 006256127606.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
            sh "docker push 006256127606.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
            sh "aws ecs update-service --force-new-deployment --service chat-app-service --cluster nate"
        }
      }
   }
}