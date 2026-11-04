
pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    triggers {
        pollSCM('H/2 * * * *')
    }

    environment {
        IMAGE_NAME = 'springboot-cicd-pipeline'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/vikas-patel11/springboot-cicd-pipeline.git'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvnw.cmd clean package'
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_PASSWORD'
                    )
                ]) {
                    bat '''
                        @echo off

                        echo Logging in to Docker Hub...

                        echo %DOCKERHUB_PASSWORD% | docker login -u %DOCKERHUB_USERNAME% --password-stdin

                        if errorlevel 1 exit /b 1

                        echo Building Docker image...

                        docker build -t %DOCKERHUB_USERNAME%/%IMAGE_NAME%:%BUILD_NUMBER% .
                        if errorlevel 1 exit /b 1

                        docker tag %DOCKERHUB_USERNAME%/%IMAGE_NAME%:%BUILD_NUMBER% %DOCKERHUB_USERNAME%/%IMAGE_NAME%:latest

                        echo Pushing versioned image...

                        docker push %DOCKERHUB_USERNAME%/%IMAGE_NAME%:%BUILD_NUMBER%
                        if errorlevel 1 exit /b 1

                        echo Pushing latest image...

                        docker push %DOCKERHUB_USERNAME%/%IMAGE_NAME%:latest
                        if errorlevel 1 exit /b 1

                        docker logout

                        echo Docker image pushed successfully!
                    '''
                }
            }
        }

        stage('Deploy to Render') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'render-deploy-hook',
                        variable: 'RENDER_DEPLOY_HOOK'
                    )
                ]) {
                    bat '''
                        @echo off

                        echo Triggering Render deployment...

                        curl.exe -f -X POST "%RENDER_DEPLOY_HOOK%"

                        if errorlevel 1 exit /b 1

                        echo Render deployment triggered successfully!
                    '''
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true,
                testResults: '**/target/surefire-reports/TEST-*.xml'
        }

        success {
            archiveArtifacts artifacts: 'target/*.jar',
                fingerprint: true

            echo 'CI/CD Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
