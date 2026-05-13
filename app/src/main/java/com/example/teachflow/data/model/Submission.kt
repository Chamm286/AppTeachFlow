package com.example.teachflow.data.model

data class Submission(
    val id: String = "",
    val assignmentId: String = "",       // Mã bài tập
    val studentId: String = "",          // Học sinh nộp
    val studentName: String = "",        // Tên học sinh
    val answers: List<Answer> = emptyList(), // Câu trả lời
    val score: Double? = null,           // Điểm (null nếu chưa chấm)
    val feedback: String = "",           // Nhận xét của giáo viên
    val submittedAt: Long = 0L,          // Thời gian nộp
    val gradedAt: Long = 0L,             // Thời gian chấm
    val status: String = "submitted"     // submitted, graded, late
)

data class Answer(
    val questionId: String = "",
    val answer: String = ""
)
