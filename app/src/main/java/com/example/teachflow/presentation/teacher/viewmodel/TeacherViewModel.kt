package com.example.teachflow.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.model.Class
import com.example.teachflow.data.model.Teacher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeacherUiState(
    val isLoading: Boolean = false,
    val teacherName: String = "",
    val teacherCode: String = "",
    val classCount: Int = 0,
    val studentCount: Int = 0,
    val avgScore: Float = 0f,
    val classes: List<Class> = emptyList(),
    val error: String? = null
)

class TeacherViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(TeacherUiState())
    val state = _state.asStateFlow()
    
    fun loadData(teacherId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                // Lấy thông tin giáo viên từ Firestore
                val teacher = RepoHolder.repo.getTeacherById(teacherId)
                
                if (teacher != null) {
                    _state.update { 
                        it.copy(
                            teacherName = teacher.name,
                            teacherCode = teacher.teacherCode
                        )
                    }
                    
                    // Lấy danh sách lớp của giáo viên
                    val classes = RepoHolder.repo.getClassesByTeacher(teacherId)
                    var totalStudents = 0
                    
                    for (classItem in classes) {
                        val students = RepoHolder.repo.getStudentsByClass(classItem.id)
                        totalStudents += students.size
                    }
                    
                    _state.update { 
                        it.copy(
                            classes = classes,
                            classCount = classes.size,
                            studentCount = totalStudents,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = "Không tìm thấy giáo viên"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Có lỗi xảy ra"
                    )
                }
            }
        }
    }
}
