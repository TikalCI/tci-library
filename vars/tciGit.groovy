#!/usr/bin/groovy

def checkout(Map config) {
    try {
        echo "config.repository=${config.repository}"
        echo "config.dir=${config.dir}"
        if (config == null) {
            echo "config.repository=${config.repository}"
            echo "config.dir=${config.dir}"
            config = [:]
        }
        if (config.repository == null || config.repository == "") {
            echo "[ERROR] repository URL must be provided!"
            currentBuild.result = "FAILURE"
            return
        }
        if (config.branch == null) {
            config.branch = "master"
        }
        if (config.credentialsId == null) {
            if (env.TCI_MASTER_DEFAULT_GIT_CREDENTIAL) {
                config.credentialsId = env.TCI_MASTER_DEFAULT_GIT_CREDENTIAL
            }
            else {
                config.credentialsId = "gitsshkey"
            }
        }
        if (config.dir == null) {
            config.dir = "."
        }

        dir(config.dir) {
            timeout(time: 5, unit: 'MINUTES') {
                checkout([
                        $class: 'GitSCM', branches: [[name: config.branch]],
                        userRemoteConfigs: [[url: config.repository ,credentialsId:config.credentialsId]]
                ])
            }
        }
    }
    catch (error) {
        echo "[ERROR] ${error}"
    }
}

