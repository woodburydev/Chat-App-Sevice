pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:$PATH"
    }


   stages {
      stage('Build') {
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
      stage("Test") {
        steps {

        sh "mvn test"

        }
      }
   }
}