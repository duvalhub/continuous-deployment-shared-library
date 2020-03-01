package com.duvalhub.initializeworkdir

import com.duvalhub.BaseObject
import com.duvalhub.git.GitRepo

class InitializeWorkdirIn extends BaseObject {
    String appWorkdir
    GitRepo appGitRepo
    
    String pipelineWorkdir
    GitRepo pipelineGitRepo

    InitializeWorkdirIn() {
        this.pipelineGitRepo = new GitRepo("duvalhub", "continuous-deployment-shared-library")
        this.pipelineWorkdir = "jenkins-workdir"
        this.appWorkdir = "app-workdir"
    }

    InitializeWorkdirIn(GitRepo appGitRepo) {
        this()
        this.appGitRepo = appGitRepo
    }
}