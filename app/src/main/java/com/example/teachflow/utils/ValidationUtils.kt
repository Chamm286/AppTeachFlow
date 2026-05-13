package com.example.teachflow.utils

import java.util.regex.Pattern

object ValidationUtils {
    
    // Kiểm tra email hợp lệ
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)\$"
        )
        return emailPattern.matcher(email).matches()
    }
    
    // Kiểm tra mật khẩu (ít nhất 6 ký tự)
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
    
    // Kiểm tra mật khẩu mạnh (ít nhất 8 ký tự, 1 chữ hoa, 1 số, 1 ký tự đặc biệt)
    fun isStrongPassword(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return password.length >= 8 && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
    }
    
    // Kiểm tra tên (không được để trống)
    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
    
    // Kiểm tra số điện thoại
    fun isValidPhone(phone: String): Boolean {
        val phonePattern = Pattern.compile("^(0|\\+84)[0-9]{9,10}\$")
        return phonePattern.matcher(phone).matches()
    }
    
    // Kiểm tra mật khẩu khớp
    fun isPasswordMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
    
    // Lấy thông báo lỗi email
    fun getEmailError(email: String): String? {
        return when {
            email.isBlank() -> "Email không được để trống"
            !isValidEmail(email) -> "Email không đúng định dạng"
            else -> null
        }
    }
    
    // Lấy thông báo lỗi mật khẩu
    fun getPasswordError(password: String): String? {
        return when {
            password.isBlank() -> "Mật khẩu không được để trống"
            !isValidPassword(password) -> "Mật khẩu phải có ít nhất 6 ký tự"
            else -> null
        }
    }
    
    // Lấy thông báo lỗi tên
    fun getNameError(name: String): String? {
        return when {
            name.isBlank() -> "Tên không được để trống"
            name.length < 2 -> "Tên phải có ít nhất 2 ký tự"
            else -> null
        }
    }
}
