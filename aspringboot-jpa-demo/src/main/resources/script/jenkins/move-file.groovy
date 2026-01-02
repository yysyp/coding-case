@Library("xxxx")

pipeline {

    agent { label "xxx-jenkins-slave" }

    options { timestamps () }

    parameters {
        choice(name: 'env', choices: ['dev', 'sit'])
        string(name: 'msg', defaultValue: 'hello', description: 'sayhi')
    }

    environment {
        jdk_home="/build/jdk"
    }

    stages {

        stage("xxx") {
            steps {
                script {
                    def AAA = "A"+UUID.randomUUID().toString()
                    sh(returnStdout: true, script: "gcloud --version")
                    try {

                        sh(xxx)

                        withCredentials([string(credentialsId: 'xxx', varible: 'userpassId')]) {
                            sh """
                                smbclient //xxxx/x -U \"${userpassId}\" -c \"get a.csv b.csv\"
                            """
                            //sh(xxx)
                        }

                    } catch (Exception e) {
                        error("Error occur: " + e)
                        currentBuild.result = 'FAILURE'
                    } finally {
                        //sh(returnStdout: true, script: "xxx")
                    }

                }

            }
        }

    }

    post {
        failure {
            echo("Failed")
        }
        success {
            mail to: 'abc@123.com',
                subject: 'Test xxx',
                body: """
                    Hi xxx
                """
        }
    }

}