pipeline {
   agent any

   stages {
      stage('Build') {
         steps {
            // Get some code from a GitHub repository
            git 'https://github.com/woodburydev/Chat-App-Sevice.git'

            // Run Maven
            sh export "M2_HOME=/Users/natew/Development/apache-maven-3.6.3"
            sh export "PATH=$PATH:$M2_HOME/bin"
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