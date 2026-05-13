package com.example.teachflow.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val userId: String? = null,
    val userRole: String? = null,
    val userName: String? = null
)

class AuthViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            Log.d("AUTH_LOGIN", "🔐 Đang đăng nhập: $email")
            
            try {
                val usersSnapshot = db.collection("users")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .await()
                
                Log.d("AUTH_LOGIN", "📊 Tìm thấy ${usersSnapshot.size()} user")
                
                if (usersSnapshot.isEmpty) {
                    Log.e("AUTH_LOGIN", "❌ Sai email hoặc mật khẩu")
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = "Email hoặc mật khẩu không đúng"
                    )
                    return@launch
                }
                
                val document = usersSnapshot.documents[0]
                val userId = document.id
                val userRole = document.getString("role") ?: ""
                val userName = document.getString("name") ?: document.getString("fullName") ?: ""
                
                Log.d("AUTH_LOGIN", "✅ Đăng nhập thành công!")
                Log.d("AUTH_LOGIN", "   - ID: $userId")
                Log.d("AUTH_LOGIN", "   - Name: $userName")
                Log.d("AUTH_LOGIN", "   - Role: $userRole")
                
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userId = userId,
                    userRole = userRole,
                    userName = userName
                )
                
            } catch (e: Exception) {
                Log.e("AUTH_LOGIN", "❌ Lỗi: ${e.message}")
                e.printStackTrace()
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = "Lỗi kết nối: ${e.message}"
                )
            }
        }
    }
    
    fun logout() {
        _authState.value = AuthState()
    }    
    // THÊM HÀM REGISTER
    fun register(email: String, password: String, fullName: String, role: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            Log.d("AUTH_REGISTER", "📝 Đang đăng ký: $email - $fullName - $role")
            
            try {
                val existingUser = db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .await()
                
                if (!existingUser.isEmpty) {
                    Log.e("AUTH_REGISTER", "❌ Email đã tồn tại: $email")
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = "Email đã được đăng ký"
                    )
                    return@launch
                }
                
                val newUser = hashMapOf(
                    "email" to email,
                    "password" to password,
                    "name" to fullName,
                    "fullName" to fullName,
                    "role" to role,
                    "isActive" to true,
                    "createdAt" to System.currentTimeMillis()
                )
                
                val docRef = db.collection("users").add(newUser).await()
                val userId = docRef.id
                
                Log.d("AUTH_REGISTER", "✅ Đăng ký thành công! ID: $userId")
                
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userId = userId,
                    userRole = role,
                    userName = fullName
                )
                
            } catch (e: Exception) {
                Log.e("AUTH_REGISTER", "❌ Lỗi: ${e.message}")
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = "Đăng ký thất bại: ${e.message}"
                )
            }
        }
    }

}
