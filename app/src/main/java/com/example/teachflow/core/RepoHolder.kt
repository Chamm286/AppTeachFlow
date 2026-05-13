package com.example.teachflow.core

import com.example.teachflow.data.repo.AppRepo

object RepoHolder {
    lateinit var repo: AppRepo
    
    fun init(repo: AppRepo) {
        this.repo = repo
    }
}
