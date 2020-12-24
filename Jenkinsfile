pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:$PATH"
    }


   stages {
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
            sh "docker build . -t chat-application"
            sh "docker tag chat-application:latest 006256127606.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
            sh "docker push 006256127606.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
        }
      }
   }
}