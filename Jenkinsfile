pipeline {
    agent any

    environment {
        M2_HOME="/usr/local/apache-maven"
        M2="$M2_HOME/bin"
        PATH="$M2:$PATH"
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
            sh "whoami"
            sh "mvn clean package -D maven.test.failure.ignore=true "

         }

         post {
            success {
               junit '**/target/surefire-reports/TEST-*.xml'
               archiveArtifacts 'target/*.jar'
            }
         }
      }

      stage("Build Container") {
        steps {
            sh "docker build . -t 303109974979.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
            sh '''#!/bin/bash \n docker login -u AWS -p $(aws ecr get-login-password --region us-west-1) 303109974979.dkr.ecr.us-west-1.amazonaws.com'''
            sh "docker push 303109974979.dkr.ecr.us-west-1.amazonaws.com/chat-application:latest"
            sh "aws ecs update-service --force-new-deployment --service chat-application-service --cluster chat-application"
        }
      }
   }
}