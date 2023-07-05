#!/usr/bin/env groovy

node {
    def MVN_CONFIG = "maven-settings"
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw clean"
    }

    stage('build application') {
        configFileProvider([configFile(fileId: "${MVN_CONFIG}", targetLocation: "settings.xml")]) {
            sh "./mvnw verify deploy -Pprod -DskipTests -s settings.xml"
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }

    stage('sonar analysis') {
        script {
            def scannerHome = tool 'My SonarQube Server';
            withSonarQubeEnv() {
                sh """${scannerHome}/bin/sonar-scanner \
                -Dproject.settings=sonar-project.properties """
            }
        }
    }

    stage('install') {
        sh "./mvnw install -DskipTests"
    }
}
