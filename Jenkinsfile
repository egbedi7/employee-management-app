pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t employee-management-app:1.0 .'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose down'
                sh 'docker compose up -d'
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    echo "Waiting for Employee Management API to become ready..."

                    for i in {1..12}
                    do
                        if curl -f http://employee-management-app:8095/api/employees
                        then
                            echo "Employee Management API is healthy!"
                            exit 0
                        fi

                        echo "API not ready yet. Waiting 5 seconds..."
                        sleep 5
                    done

                    echo "Health check failed after 60 seconds."
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            echo 'Employee Management Pipeline completed successfully!'
        }

        failure {
            echo 'Employee Management Pipeline failed.'
        }
    }
}

