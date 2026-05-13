package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Tuition(
    @DocumentId
    val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val studentCode: String = "",
    val className: String = "",
    val schoolYear: String = "2024-2025",
    val semester: String = "HK1",
    
    // Các khoản phí
    val tuitionFee: Double = 0.0,      // Học phí
    val materialsFee: Double = 0.0,    // Tiền sách vở
    val activityFee: Double = 0.0,     // Hoạt động ngoại khóa
    val otherFee: Double = 0.0,        // Phí khác
    
    val totalFee: Double = 0.0,        // Tổng phải đóng
    val paidFee: Double = 0.0,         // Đã đóng
    val remainingFee: Double = 0.0,    // Còn lại
    
    val paymentStatus: String = "unpaid", // paid, partial, unpaid
    val paymentHistory: List<PaymentRecord> = emptyList(),
    
    val dueDate: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class PaymentRecord(
    val id: String = "",
    val amount: Double = 0.0,
    val paymentMethod: String = "", // cash, bank, momo
    val transactionId: String = "",
    val note: String = "",
    val paidAt: Long = System.currentTimeMillis(),
    val receivedBy: String = ""
)
