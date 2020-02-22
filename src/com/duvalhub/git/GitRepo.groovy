package com.duvalhub.git

import com.duvalhub.BaseObject

class GitRepo extends BaseObject {
    String org
    String repo
    String branch

    GitRepo(String org, String repo) {
        this.org = org
        this.repo = repo
    }

    GitRepo(String org, String repo, String branch) {
        this(org, repo)
        this.branch = branch
    }

    String getUrl() {
        return String.format("git@github.com:%s/%s.git", this.org, this.repo)
    }

    String getHttpUrl() {
        return String.format("https://github.com/%s/%s.git", this.org, this.repo)
    }

}