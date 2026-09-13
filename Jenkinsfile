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
        
	stage('Debug Network') {
          steps {
        sh '''
            echo "=== Jenkins hostname ==="
            hostname

            echo "=== Jenkins user ==="
            whoami

            echo "=== DNS resolution ==="
            getent hosts employee-management-app

            echo "=== Jenkins network interfaces ==="
            hostname -I

            echo "=== DNS configuration ==="
            cat /etc/resolv.conf
        '''
    }
}

        stage('Health Check') {
            steps {
                sh '''
                    echo "Waiting for Employee Management API to become ready..."

                    i=1

                    while [ $i -le 12 ]
                    do
                        echo "Health check attempt $i..."

                        if curl --connect-timeout 5 --max-time 10 -f http://employee-management-app:8095/api/employees
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

