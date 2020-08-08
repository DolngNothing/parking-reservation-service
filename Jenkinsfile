pipeline {
  agent { label 'master' }
  environment {
    JENKINS_NODE_COOKIE = "donotkillme"
  }
  stages {
    stage('test') {
      steps {
        echo 'test'
        bat 'gradle test'
      }
    }
    stage('build') {
      steps {
        echo 'build'
        bat 'gradle build'
      }
    }
    stage('deploy') {
      steps {
        echo 'deploy'
        bat "copy build\\libs\\*.jar d:\\deploy\\"
        bat "run.bat"
      }
    }
  }
}