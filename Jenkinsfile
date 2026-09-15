pipeline {

    agent any

    stages {

        stage {
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
                sh 'docker build -t employee-management-app:1 .'
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

                    APP_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' employee-management-app)

                    echo "Employee Management App IP: $APP_IP"

                    i=1

                    while [ $i -le 12 ]
                    do
                        echo "Health check attempt $i..."

                        if curl --connect-timeout 5 --max-time 10 -f http://$APP_IP:8095/api/employees
                        then
                            echo "Employee Management API is healthy!"
                            exit 0
                        fi

                        echo "API not ready yet. Waiting 5 seconds..."
                        sleep 5

                        i=$((i + 1))
                    done

                    echo "Health check failed after 12 attempts."
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
