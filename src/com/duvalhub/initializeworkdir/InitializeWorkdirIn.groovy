package com.duvalhub.initializeworkdir

import com.duvalhub.BaseObject
import com.duvalhub.git.GitRepo

class InitializeWorkdirIn extends BaseObject {
    String appWorkdir
    GitRepo appGitRepo
    Boolean cloneAppRepo = true
    
    String pipelineWorkdir
    GitRepo pipelineGitRepo
    Boolean clonePipelineRepo = true

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