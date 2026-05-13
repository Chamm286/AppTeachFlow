package com.example.teachflow.data.repo

import com.example.teachflow.data.model.*
import com.example.teachflow.data.remote.Firebase

/**
 * Repository class that acts as a single source of truth for data.
 * It abstracts the data source (Firebase) from the rest of the app.
 */
class AppRepo(private val firebase: Firebase) {

    // ============ USER ============
    suspend fun createUser(user: User) = firebase.createUser(user)
    suspend fun getUserById(uid: String) = firebase.getUserById(uid)
    suspend fun getUserByEmail(email: String) = firebase.getUserByEmail(email)
    suspend fun getAllUsers() = firebase.getAllUsers()
    suspend fun updateUser(user: User) = firebase.updateUser(user)
    suspend fun deleteUser(uid: String) = firebase.deleteUser(uid)

    // ============ TEACHER ============
    suspend fun createTeacher(teacher: Teacher) = firebase.createTeacher(teacher)
    suspend fun getTeacherById(teacherId: String) = firebase.getTeacherById(teacherId)
    suspend fun getClassesByTeacher(teacherId: String) = firebase.getClassesByTeacher(teacherId)
    suspend fun getAllTeachers() = firebase.getAllTeachers()

    // ============ STUDENT ============
    suspend fun createStudent(student: Student) = firebase.createStudent(student)
    suspend fun getStudentById(studentId: String) = firebase.getStudentById(studentId)
    suspend fun getAllStudents() = firebase.getAllStudents()

    // ============ CLASS ============
    suspend fun createClass(classItem: Class) = firebase.createClass(classItem)
    suspend fun getClassById(classId: String) = firebase.getClassById(classId)
    suspend fun getAllClasses() = firebase.getAllClasses()

    // ============ SUBJECT ============
    suspend fun createSubject(subject: Subject) = firebase.createSubject(subject)
    suspend fun getAllSubjects() = firebase.getAllSubjects()

    // ============ SCHOOL ============
    suspend fun createSchool(school: School) = firebase.createSchool(school)
    suspend fun getAllSchools() = firebase.getAllSchools()

    // ============ COURSE ============
    suspend fun createCourse(course: Course) = firebase.createCourse(course)
    suspend fun getCourseById(courseId: String) = firebase.getCourseById(courseId)
    suspend fun getAllCourses() = firebase.getAllCourses()
    suspend fun updateCourse(course: Course) = firebase.updateCourse(course)
    suspend fun deleteCourse(id: String) = firebase.deleteCourse(id)

    // ============ LESSON ============
    suspend fun createLesson(lesson: Lesson) = firebase.createLesson(lesson)
    suspend fun getLessonsByCourse(courseId: String) = firebase.getLessonsByCourse(courseId)

    // ============ ASSIGNMENT ============
    suspend fun createAssignment(assignment: Assignment) = firebase.createAssignment(assignment)
    suspend fun getAssignmentsByCourse(courseId: String) = firebase.getAssignmentsByCourse(courseId)

    // ============ EXAM ============
    suspend fun createExam(exam: Exam) = firebase.createExam(exam)

    // ============ GRADE ============
    suspend fun createGrade(grade: Grade) = firebase.createGrade(grade)
    suspend fun getGradesByStudent(studentId: String) = firebase.getGradesByStudent(studentId)
    suspend fun getRecentGradesByStudent(studentId: String, limit: Int = 5) = firebase.getRecentGradesByStudent(studentId, limit)

    // ============ ATTENDANCE ============
    suspend fun createAttendance(attendance: Attendance) = firebase.createAttendance(attendance)
    suspend fun getAttendanceByStudent(studentId: String) = firebase.getAttendanceByStudent(studentId)
    suspend fun getAttendanceByStudent(studentId: String, limit: Int) = firebase.getAttendanceByStudent(studentId, limit)

    // ============ NOTIFICATION ============
    suspend fun createNotification(notification: Notification) = firebase.createNotification(notification)
    suspend fun getNotificationsByUser(userId: String) = firebase.getNotificationsByUser(userId)
    suspend fun getNotificationsForParent(parentId: String) = firebase.getNotificationsForParent(parentId)

    // ============ PARENT ============
    suspend fun getChildrenByParent(parentId: String) = firebase.getChildrenByParent(parentId)
    suspend fun getScheduleByStudent(studentId: String) = firebase.getScheduleByStudent(studentId)

    // ============ STUDENT EXTRA ============
    suspend fun getClassesByStudent(studentId: String): List<Class> = firebase.getClassesByStudent(studentId)
    suspend fun getStudentsByClass(classId: String): List<Student> = firebase.getStudentsByClass(classId)
    suspend fun getStudentsByTeacher(teacherId: String): List<Student> = firebase.getStudentsByTeacher(teacherId)
    suspend fun getStudentWithDetails(studentId: String): StudentWithScores? = firebase.getStudentWithDetails(studentId)

    // ============ TASKS ============
    suspend fun getTasks(studentId: String): List<Task> = firebase.getTasks(studentId)
    suspend fun updateTask(taskId: String, isCompleted: Boolean) = firebase.updateTask(taskId, isCompleted)
    suspend fun createTask(task: Task): String = firebase.createTask(task)
    suspend fun getTaskById(taskId: String): Task? = firebase.getTaskById(taskId)
}
