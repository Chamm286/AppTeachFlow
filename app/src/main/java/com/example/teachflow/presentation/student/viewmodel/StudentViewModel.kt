package com.example.teachflow.presentation.student.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.data.model.Class
import com.example.teachflow.data.model.Task
import com.example.teachflow.data.repo.AppRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudentUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val averageScore: Float = 0f,
    val classCount: Int = 0,
    val taskCompleted: Int = 0,
    val classes: List<Class> = emptyList(),
    val tasks: List<Task> = emptyList()
)

class StudentViewModel : ViewModel() {

    private var repo: AppRepo? = null

    fun initRepo(repo: AppRepo) {
        this.repo = repo
    }

    private val _state = MutableStateFlow(StudentUiState())
    val state = _state.asStateFlow()

        fun loadData(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val user = repo?.getUserById(userId)
                _state.update { it.copy(userName = user?.name ?: "") }
                
                val classes = repo?.getClassesByStudent(userId) ?: emptyList()
                _state.update { it.copy(classes = classes, classCount = classes.size) }
                
                val tasks = repo?.getTasks(userId) ?: emptyList()
                val completed = tasks.count { it.isCompleted }
                _state.update { it.copy(tasks = tasks, taskCompleted = completed) }
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun toggleTask(taskId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            repo?.updateTask(taskId, isCompleted)
        }
    }
}

