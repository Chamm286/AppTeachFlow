package com.example.teachflow.data.remote

import com.example.teachflow.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class Firebase {

    private val db = FirebaseFirestore.getInstance()

    // ============ USER ============
    suspend fun createUser(user: User): String {
        val docRef = db.collection("users").document(user.uid).set(user).await()
        return user.uid
    }

    suspend fun getUserById(uid: String): User? {
        return try {
            val doc = db.collection("users").document(uid).get().await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return try {
            val query = db.collection("users").whereEqualTo("email", email).get().await()
            query.documents.firstOrNull()?.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllUsers(): List<User> {
        return try {
            db.collection("users")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ TEACHER ============
    suspend fun createTeacher(teacher: Teacher): String {
        val docRef = db.collection("teachers").document(teacher.id).set(teacher).await()
        return teacher.id
    }

    suspend fun getTeacherById(teacherId: String): Teacher? {
        return try {
            val doc = db.collection("teachers").document(teacherId).get().await()
            doc.toObject(Teacher::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getClassesByTeacher(teacherId: String): List<Class> {
        return try {
            db.collection("classes")
                .whereEqualTo("teacherId", teacherId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Class::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllTeachers(): List<Teacher> {
        return try {
            db.collection("teachers")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Teacher::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ STUDENT ============
    suspend fun createStudent(student: Student): String {
        val docRef = db.collection("students").document(student.id).set(student).await()
        return student.id
    }

    suspend fun getStudentById(studentId: String): Student? {
        return try {
            val doc = db.collection("students").document(studentId).get().await()
            doc.toObject(Student::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllStudents(): List<Student> {
        return try {
            db.collection("students")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Student::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ CLASS ============
    suspend fun createClass(classItem: Class): String {
        val docRef = db.collection("classes").document(classItem.id).set(classItem).await()
        return classItem.id
    }

    suspend fun getClassById(classId: String): Class? {
        return try {
            val doc = db.collection("classes").document(classId).get().await()
            doc.toObject(Class::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllClasses(): List<Class> {
        return try {
            db.collection("classes")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Class::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ SUBJECT ============
    suspend fun createSubject(subject: Subject): String {
        val docRef = db.collection("subjects").document(subject.id).set(subject).await()
        return subject.id
    }

    suspend fun getAllSubjects(): List<Subject> {
        return try {
            db.collection("subjects")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Subject::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ SCHOOL ============
    suspend fun createSchool(school: School): String {
        val docRef = db.collection("schools").document(school.id).set(school).await()
        return school.id
    }

    suspend fun getAllSchools(): List<School> {
        return try {
            db.collection("schools")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(School::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ COURSE ============
    suspend fun createCourse(course: Course): String {
        val docRef = db.collection("courses").document(course.id).set(course).await()
        return course.id
    }

    suspend fun getCourseById(courseId: String): Course? {
        return try {
            val doc = db.collection("courses").document(courseId).get().await()
            doc.toObject(Course::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllCourses(): List<Course> {
        return try {
            db.collection("courses")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Course::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ LESSON ============
    suspend fun createLesson(lesson: Lesson): String {
        val docRef = db.collection("lessons").document(lesson.id).set(lesson).await()
        return lesson.id
    }

    suspend fun getLessonsByCourse(courseId: String): List<Lesson> {
        return try {
            db.collection("lessons")
                .whereEqualTo("courseId", courseId)
                .orderBy("orderIndex")
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Lesson::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ ASSIGNMENT ============
    suspend fun createAssignment(assignment: Assignment): String {
        val docRef = db.collection("assignments").document(assignment.id).set(assignment).await()
        return assignment.id
    }

    suspend fun getAssignmentsByCourse(courseId: String): List<Assignment> {
        return try {
            db.collection("assignments")
                .whereEqualTo("courseId", courseId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Assignment::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ EXAM ============
    suspend fun createExam(exam: Exam): String {
        val docRef = db.collection("exams").document(exam.id).set(exam).await()
        return exam.id
    }

    // ============ GRADE ============
    suspend fun createGrade(grade: Grade): String {
        val docRef = db.collection("grades").document(grade.id).set(grade).await()
        return grade.id
    }

    suspend fun getGradesByStudent(studentId: String): List<Grade> {
        return try {
            db.collection("grades")
                .whereEqualTo("studentId", studentId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Grade::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGradesByClass(classId: String): List<Grade> {
        return try {
            db.collection("grades")
                .whereEqualTo("classId", classId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Grade::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRecentGradesByStudent(studentId: String, limit: Int = 5): List<Grade> {
        return try {
            db.collection("grades")
                .whereEqualTo("studentId", studentId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Grade::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ ATTENDANCE ============
    suspend fun createAttendance(attendance: Attendance): String {
        val docRef = db.collection("attendances").document(attendance.id).set(attendance).await()
        return attendance.id
    }

    suspend fun getAttendanceByStudent(studentId: String): List<Attendance> {
        return try {
            db.collection("attendances")
                .whereEqualTo("studentId", studentId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Attendance::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAttendanceByStudent(studentId: String, limit: Int): List<Attendance> {
        return try {
            db.collection("attendances")
                .whereEqualTo("studentId", studentId)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Attendance::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ NOTIFICATION ============
    suspend fun createNotification(notification: Notification): String {
        val docRef = db.collection("notifications").document(notification.id).set(notification).await()
        return notification.id
    }

    suspend fun getNotificationsByUser(userId: String): List<Notification> {
        return try {
            db.collection("notifications")
                .whereEqualTo("targetUserId", userId) // Sửa lại field cho đúng logic thường gặp
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Notification::class.java) }
        } catch (e: Exception) {
            // Nếu không có field targetUserId thì thử senderId hoặc search theo role
            try {
                db.collection("notifications")
                    .whereArrayContains("targetRoles", "teacher")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Notification::class.java) }
            } catch (e2: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getNotificationsByRole(role: String): List<Notification> {
        return try {
            db.collection("notifications")
                .whereArrayContains("targetRoles", role)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Notification::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getNotificationsForParent(parentId: String): List<Notification> {
        return try {
            db.collection("notifications")
                .whereArrayContains("targetRoles", "parent")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Notification::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ PARENT FUNCTIONS ============
    suspend fun getChildrenByParent(parentId: String): List<Student> {
        return try {
            val parent = getUserById(parentId)
            if (parent != null) {
                val querySnapshot = db.collection("students")
                    .whereEqualTo("parentId", parentId)
                    .get()
                    .await()

                var students = querySnapshot.documents.mapNotNull { it.toObject(Student::class.java) }

                if (students.isEmpty() && parent.email.isNotEmpty()) {
                    val emailQuery = db.collection("students")
                        .whereEqualTo("parentEmail", parent.email)
                        .get()
                        .await()
                    students = emailQuery.documents.mapNotNull { it.toObject(Student::class.java) }
                }

                if (students.isEmpty() && parent.phone.isNotEmpty()) {
                    val phoneQuery = db.collection("students")
                        .whereEqualTo("parentPhone", parent.phone)
                        .get()
                        .await()
                    students = phoneQuery.documents.mapNotNull { it.toObject(Student::class.java) }
                }
                students
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getScheduleByStudent(studentId: String): List<Class> {
        return try {
            val student = getStudentById(studentId)
            if (student != null && student.classId.isNotEmpty()) {
                db.collection("classes")
                    .whereEqualTo("id", student.classId)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Class::class.java) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ============ STUDENT FUNCTIONS ============
    suspend fun getClassesByStudent(studentId: String): List<Class> {
        return try {
            val student = getStudentById(studentId)
            if (student != null) {
                if (student.classId.isNotEmpty()) {
                    val classDoc = db.collection("classes")
                        .whereEqualTo("id", student.classId)
                        .get()
                        .await()
                    val classes = classDoc.documents.mapNotNull { it.toObject(Class::class.java) }
                    if (classes.isNotEmpty()) return classes
                }
                if (student.className.isNotEmpty()) {
                    val classDoc = db.collection("classes")
                        .whereEqualTo("name", student.className)
                        .get()
                        .await()
                    val classes = classDoc.documents.mapNotNull { it.toObject(Class::class.java) }
                    if (classes.isNotEmpty()) return classes
                }
                db.collection("classes")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Class::class.java) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getStudentsByClass(classId: String): List<Student> {
        return try {
            val querySnapshot = db.collection("students")
                .whereEqualTo("classId", classId)
                .get()
                .await()
            var students = querySnapshot.documents.mapNotNull { it.toObject(Student::class.java) }
            if (students.isEmpty()) {
                val classInfo = getClassById(classId)
                if (classInfo != null && classInfo.name.isNotEmpty()) {
                    val nameQuery = db.collection("students")
                        .whereEqualTo("className", classInfo.name)
                        .get()
                        .await()
                    students = nameQuery.documents.mapNotNull { it.toObject(Student::class.java) }
                }
            }
            students
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getStudentsByTeacher(teacherId: String): List<Student> {
        return try {
            val classes = getClassesByTeacher(teacherId)
            val classIds = classes.map { it.id }
            if (classIds.isNotEmpty()) {
                db.collection("students")
                    .whereIn("classId", classIds)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(Student::class.java) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getStudentWithDetails(studentId: String): StudentWithScores? {
        return try {
            val student = getStudentById(studentId)
            if (student != null) {
                val grades = getGradesByStudent(studentId)
                val avgScore = if (grades.isNotEmpty()) {
                    grades.map { it.average ?: 0.0 }.average()
                } else 0.0
                StudentWithScores(
                    student = student,
                    scores = grades,
                    averageScore = avgScore.toFloat(),
                    rank = when {
                        avgScore >= 9.0 -> "Xuất sắc"
                        avgScore >= 8.0 -> "Giỏi"
                        avgScore >= 6.5 -> "Khá"
                        avgScore >= 5.0 -> "Trung bình"
                        else -> "Yếu"
                    }
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // ============ TASK FUNCTIONS ============
    suspend fun getTasks(studentId: String): List<Task> {
        return try {
            db.collection("tasks")
                .whereEqualTo("studentId", studentId)
                .orderBy("dueDate", Query.Direction.ASCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Task::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateTask(taskId: String, isCompleted: Boolean) {
        try {
            db.collection("tasks").document(taskId)
                .update("isCompleted", isCompleted)
                .await()
        } catch (e: Exception) {
            println("❌ Lỗi updateTask: ${e.message}")
        }
    }

    suspend fun createTask(task: Task): String {
        val docRef = db.collection("tasks").document(task.id).set(task).await()
        return task.id
    }

    suspend fun getTaskById(taskId: String): Task? {
        return try {
            val doc = db.collection("tasks").document(taskId).get().await()
            doc.toObject(Task::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateRank(average: Double): String {
        return when {
            average >= 9.0 -> "Xuất sắc"
            average >= 8.0 -> "Giỏi"
            average >= 6.5 -> "Khá"
            average >= 5.0 -> "Trung bình"
            else -> "Yếu"
        }
    }
}