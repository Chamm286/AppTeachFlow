// core/SeedData.kt - Sửa tất cả avatarResId thành avatarUrl
package com.example.teachflow.core

import com.example.teachflow.data.model.*
import com.example.teachflow.data.remote.Firebase

object SeedData {

    private val firebase = Firebase()

    suspend fun seedAllData() {
        try {
            println("🎓 BẮT ĐẦU SEED DỮ LIỆU HỆ THỐNG GIÁO DỤC")
            println("============================================================")
            println()

            var total = 0

            // ============ 1. TẠO USER (ĐĂNG NHẬP) ============
            println("📥 [1/8] Đang tạo Users...")

            // Admin
            val admin = User(
                id = "AD001", uid = "AD001",
                email = "chamm4930@gmail.com",
                password = "12345678",
                name = "Châu Minh Mẫn",
                fullName = "Châu Minh Mẫn",
                role = "admin",
                loginType = "email",
                phone = "0909123456",
                avatarUrl = "https://randomuser.me/api/portraits/women/68.jpg",
                isActive = true,
                schoolId = "SCH_VKU",
                schoolName = "ĐH Công nghệ Thông tin & Truyền thông Việt - Hàn",
                createdAt = System.currentTimeMillis()
            )
            firebase.createUser(admin)
            total++
            println("   ✅ AD001 | chamm4930@gmail.com | Admin")

            // Giáo viên - ĐH CNTT & TT Việt - Hàn (VKU)
            val vkuTeachers = listOf(
                TeacherUser("GV001", "Trần Nguyễn Bảo Trâm", "tramntb.24it@vku.udn.vn", "0909123457", "https://randomuser.me/api/portraits/women/1.jpg", "Nữ", "02/03/1995", "Thạc sĩ CNTT", 5, "Trưởng bộ môn CNTT", "SCH_VKU"),
                TeacherUser("GV002", "Lê Minh Tuấn", "tuan.le@vku.edu.vn", "0909123458", "https://randomuser.me/api/portraits/men/2.jpg", "Nam", "15/07/1988", "Tiến sĩ KHMT", 10, "Giảng viên chính", "SCH_VKU"),
                TeacherUser("GV003", "Phạm Thị Hồng", "hong.pham@vku.edu.vn", "0909123459", "https://randomuser.me/api/portraits/women/3.jpg", "Nữ", "20/10/1990", "Thạc sĩ CNTT", 8, "Giảng viên", "SCH_VKU"),
                TeacherUser("GV004", "Nguyễn Văn An", "an.nguyen@vku.edu.vn", "0909123460", "https://randomuser.me/api/portraits/men/4.jpg", "Nam", "10/12/1985", "Tiến sĩ ATTT", 12, "Phó trưởng khoa", "SCH_VKU")
            )

            // Giáo viên - ĐH Bách Khoa Đà Nẵng (DUT)
            val dutTeachers = listOf(
                TeacherUser("GV005", "Hoàng Thị Lan", "lan.hoang@dut.udn.vn", "0909123461", "https://randomuser.me/api/portraits/women/5.jpg", "Nữ", "25/05/1992", "Thạc sĩ Toán", 6, "Giảng viên", "SCH_DUT"),
                TeacherUser("GV006", "Vũ Minh Đức", "duc.vu@dut.udn.vn", "0909123462", "https://randomuser.me/api/portraits/men/6.jpg", "Nam", "30/08/1987", "Tiến sĩ Cơ khí", 11, "Trưởng phòng Đào tạo", "SCH_DUT"),
                TeacherUser("GV007", "Trần Thị Mai", "mai.tran@dut.udn.vn", "0909123463", "https://randomuser.me/api/portraits/women/7.jpg", "Nữ", "18/04/1993", "Thạc sĩ Điện tử", 7, "Giảng viên", "SCH_DUT")
            )

            // Giáo viên - ĐH Kinh tế Đà Nẵng (DUE)
            val dueTeachers = listOf(
                TeacherUser("GV008", "Đỗ Văn Hải", "hai.do@due.edu.vn", "0909123464", "https://randomuser.me/api/portraits/men/8.jpg", "Nam", "05/11/1984", "Tiến sĩ Kinh tế", 14, "Trưởng khoa Kinh tế", "SCH_DUE"),
                TeacherUser("GV009", "Bùi Thị Thu", "thu.bui@due.edu.vn", "0909123465", "https://randomuser.me/api/portraits/women/9.jpg", "Nữ", "22/02/1994", "Thạc sĩ QTKD", 6, "Giảng viên", "SCH_DUE"),
                TeacherUser("GV010", "Lý Hoàng Nam", "nam.ly@due.edu.vn", "0909123466", "https://randomuser.me/api/portraits/men/10.jpg", "Nam", "12/09/1989", "Tiến sĩ Marketing", 9, "Phó trưởng khoa", "SCH_DUE")
            )

            val allTeachers = vkuTeachers + dutTeachers + dueTeachers

            for (t in allTeachers) {
                val user = User(
                    id = t.id, uid = t.id,
                    email = t.email, password = "12345678",
                    name = t.name, fullName = t.name,
                    role = "teacher", loginType = "email",
                    phone = t.phone, avatarUrl = t.avatar,
                    profileId = t.id, gender = t.gender,
                    birthday = t.birthday, isActive = true,
                    schoolId = t.schoolId, schoolName = getSchoolName(t.schoolId),
                    createdAt = System.currentTimeMillis()
                )
                firebase.createUser(user)
                total++
                println("   ✅ ${t.id} | ${t.email} | ${t.name} | ${getSchoolName(t.schoolId)}")
            }

            // Học sinh
            val students = listOf(
                // VKU students
                StudentUser("HS001", "Nguyễn Minh Quân", "minhquan.nguyen@student.vku.edu.vn", "0987654101", "https://randomuser.me/api/portraits/men/11.jpg", "Nam", "15/05/2006", "CL_VKU_01", "KTPM", "SCH_VKU"),
                StudentUser("HS002", "Trần Phương Thảo", "phuongthao.tran@student.vku.edu.vn", "0987654102", "https://randomuser.me/api/portraits/women/12.jpg", "Nữ", "20/08/2006", "CL_VKU_01", "KTPM", "SCH_VKU"),
                StudentUser("HS003", "Lê Hoàng Nam", "hoangnam.le@student.vku.edu.vn", "0987654103", "https://randomuser.me/api/portraits/men/13.jpg", "Nam", "10/11/2006", "CL_VKU_02", "KHMT", "SCH_VKU"),
                StudentUser("HS004", "Phạm Minh Đức", "minhduc.pham@student.vku.edu.vn", "0987654104", "https://randomuser.me/api/portraits/men/14.jpg", "Nam", "25/12/2006", "CL_VKU_02", "KHMT", "SCH_VKU"),
                StudentUser("HS005", "Nguyễn Thùy Trang", "thuytrang.nguyen@student.vku.edu.vn", "0987654105", "https://randomuser.me/api/portraits/women/15.jpg", "Nữ", "05/03/2007", "CL_VKU_03", "ATTT", "SCH_VKU"),
                // DUT students
                StudentUser("HS006", "Đỗ Thị Kim Anh", "kimanh.do@student.dut.udn.vn", "0987654106", "https://randomuser.me/api/portraits/women/16.jpg", "Nữ", "12/06/2006", "CL_DUT_01", "CNTT", "SCH_DUT"),
                StudentUser("HS007", "Vũ Hoàng Long", "hoanglong.vu@student.dut.udn.vn", "0987654107", "https://randomuser.me/api/portraits/men/17.jpg", "Nam", "18/09/2006", "CL_DUT_01", "CNTT", "SCH_DUT"),
                StudentUser("HS008", "Bùi Thanh Tùng", "thanhtung.bui@student.dut.udn.vn", "0987654108", "https://randomuser.me/api/portraits/men/18.jpg", "Nam", "22/01/2006", "CL_DUT_02", "Cơ khí", "SCH_DUT"),
                // DUE students
                StudentUser("HS009", "Hoàng Thị Ngọc", "thingoc.hoang@student.due.edu.vn", "0987654109", "https://randomuser.me/api/portraits/women/19.jpg", "Nữ", "30/04/2006", "CL_DUE_01", "Kinh tế", "SCH_DUE"),
                StudentUser("HS010", "Lê Văn Thành", "vanthanh.le@student.due.edu.vn", "0987654110", "https://randomuser.me/api/portraits/men/20.jpg", "Nam", "08/07/2006", "CL_DUE_01", "QTKD", "SCH_DUE")
            )

            for (s in students) {
                val user = User(
                    id = s.id, uid = s.id,
                    email = s.email, password = "12345678",
                    name = s.name, fullName = s.name,
                    role = "student", loginType = "email",
                    phone = s.phone, avatarUrl = s.avatar,
                    profileId = s.id, gender = s.gender,
                    birthday = s.birthday, isActive = true,
                    schoolId = s.schoolId, schoolName = getSchoolName(s.schoolId),
                    createdAt = System.currentTimeMillis()
                )
                firebase.createUser(user)
                total++
                println("   ✅ ${s.id} | ${s.email} | ${s.name} | ${getSchoolName(s.schoolId)}")
            }

            // ============ 2. TẠO TRƯỜNG HỌC VỚI VỊ TRÍ MAP ============
            println("\n📥 [2/8] Đang tạo Schools với vị trí...")

            val schools = listOf(
                School(
                    id = "SCH_VKU", schoolCode = "VKU",
                    name = "ĐH Công nghệ Thông tin & Truyền thông Việt - Hàn",
                    shortName = "ĐH Việt - Hàn",
                    address = "Số 01, đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, Đà Nẵng",
                    phone = "0236.3798.888",
                    email = "info@vku.udn.vn",
                    website = "https://vku.udn.vn",
                    logo = "🎓",
                    principal = "PGS.TS. Nguyễn Văn A",
                    foundedYear = 2015,
                    studentCount = 3500,
                    teacherCount = 120,
                    classCount = 45,
                    lat = 16.0742, lng = 108.1494,
                    status = "active",
                    createdAt = System.currentTimeMillis()
                ),
                School(
                    id = "SCH_DUT", schoolCode = "DUT",
                    name = "Đại học Bách Khoa - Đại học Đà Nẵng",
                    shortName = "ĐH Bách Khoa ĐN",
                    address = "Số 01, đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, Đà Nẵng",
                    phone = "0236.3672.222",
                    email = "info@dut.udn.vn",
                    website = "https://dut.udn.vn",
                    logo = "⚙️",
                    principal = "GS.TS. Nguyễn Văn B",
                    foundedYear = 1975,
                    studentCount = 12000,
                    teacherCount = 450,
                    classCount = 150,
                    lat = 16.0725, lng = 108.1512,
                    status = "active",
                    createdAt = System.currentTimeMillis()
                ),
                School(
                    id = "SCH_DUE", schoolCode = "DUE",
                    name = "Đại học Kinh tế - Đại học Đà Nẵng",
                    shortName = "ĐH Kinh tế ĐN",
                    address = "Số 01, đường Nguyễn Lương Bằng, phường Hòa Khánh Bắc, quận Liên Chiểu, Đà Nẵng",
                    phone = "0236.3672.333",
                    email = "info@due.udn.vn",
                    website = "https://due.udn.vn",
                    logo = "📈",
                    principal = "PGS.TS. Nguyễn Văn C",
                    foundedYear = 1992,
                    studentCount = 8000,
                    teacherCount = 300,
                    classCount = 100,
                    lat = 16.0738, lng = 108.1505,
                    status = "active",
                    createdAt = System.currentTimeMillis()
                )
            )

            for (school in schools) {
                firebase.createSchool(school)
                total++
                println("   ✅ ${school.id} | ${school.name}")
            }

            // ============ 3. TẠO LỚP HỌC ============
            println("\n📥 [3/8] Đang tạo Classes...")

            val classes = listOf(
                Class(
                    id = "CL_VKU_01", name = "KTPM - Lớp Công nghệ phần mềm K24", grade = "Đại học năm 1",
                    subject = "CNPM", teacherId = "GV001", teacherName = "Trần Nguyễn Bảo Trâm",
                    studentCount = 35, room = "P301", schedule = "Thứ 2,4,6 - 13h30",
                    schoolId = "SCH_VKU", schoolName = "ĐH Công nghệ Thông tin & Truyền thông Việt - Hàn",
                    academicYear = "2024-2028", status = "active"
                ),
                Class(
                    id = "CL_VKU_02", name = "KHMT - Lớp Khoa học máy tính K24", grade = "Đại học năm 1",
                    subject = "KHMT", teacherId = "GV002", teacherName = "Lê Minh Tuấn",
                    studentCount = 32, room = "P302", schedule = "Thứ 3,5,7 - 13h30",
                    schoolId = "SCH_VKU", schoolName = "ĐH Công nghệ Thông tin & Truyền thông Việt - Hàn",
                    academicYear = "2024-2028", status = "active"
                ),
                Class(
                    id = "CL_VKU_03", name = "ATTT - Lớp An toàn thông tin K24", grade = "Đại học năm 1",
                    subject = "ATTT", teacherId = "GV004", teacherName = "Nguyễn Văn An",
                    studentCount = 28, room = "P303", schedule = "Thứ 2,4 - 8h00",
                    schoolId = "SCH_VKU", schoolName = "ĐH Công nghệ Thông tin & Truyền thông Việt - Hàn",
                    academicYear = "2024-2028", status = "active"
                ),
                Class(
                    id = "CL_DUT_01", name = "CNTT - Lớp Công nghệ thông tin K24", grade = "Đại học năm 1",
                    subject = "CNTT", teacherId = "GV005", teacherName = "Hoàng Thị Lan",
                    studentCount = 40, room = "P201", schedule = "Thứ 2,4,6 - 7h00",
                    schoolId = "SCH_DUT", schoolName = "Đại học Bách Khoa - ĐH Đà Nẵng",
                    academicYear = "2024-2028", status = "active"
                ),
                Class(
                    id = "CL_DUT_02", name = "CK - Lớp Cơ khí K24", grade = "Đại học năm 1",
                    subject = "Cơ khí", teacherId = "GV006", teacherName = "Vũ Minh Đức",
                    studentCount = 38, room = "P202", schedule = "Thứ 3,5,7 - 7h00",
                    schoolId = "SCH_DUT", schoolName = "Đại học Bách Khoa - ĐH Đà Nẵng",
                    academicYear = "2024-2028", status = "active"
                ),
                Class(
                    id = "CL_DUE_01", name = "KT - Lớp Kinh tế K24", grade = "Đại học năm 1",
                    subject = "Kinh tế", teacherId = "GV008", teacherName = "Đỗ Văn Hải",
                    studentCount = 45, room = "P101", schedule = "Thứ 2,4 - 13h00",
                    schoolId = "SCH_DUE", schoolName = "Đại học Kinh tế - ĐH Đà Nẵng",
                    academicYear = "2024-2028", status = "active"
                ),
                Class(
                    id = "CL_DUE_02", name = "QTKD - Lớp Quản trị kinh doanh K24", grade = "Đại học năm 1",
                    subject = "QTKD", teacherId = "GV010", teacherName = "Lý Hoàng Nam",
                    studentCount = 42, room = "P102", schedule = "Thứ 3,5 - 13h00",
                    schoolId = "SCH_DUE", schoolName = "Đại học Kinh tế - ĐH Đà Nẵng",
                    academicYear = "2024-2028", status = "active"
                )
            )

            for (c in classes) {
                firebase.createClass(c)
                total++
                println("   ✅ ${c.id} | ${c.name} | ${c.schoolName}")
            }

            // ============ 4. TẠO GIÁO VIÊN CHI TIẾT ============
            println("\n📥 [4/8] Đang tạo Teachers chi tiết...")

            val teacherDetails = listOf(
                TeacherDetail("GV001", "Trần Nguyễn Bảo Trâm", "tramntb.24it@vku.udn.vn", "0909123457", "https://randomuser.me/api/portraits/women/1.jpg", "Nữ", "02/03/1995", "Đà Nẵng", 5, listOf("Thạc sĩ CNTT - ĐH Bách Khoa ĐN", "Kỹ sư Phần mềm - VKU"), listOf("Giảng viên trẻ xuất sắc 2023", "Sáng kiến đổi mới giáo dục"), listOf("Android", "Kotlin", "Firebase"), "Trưởng bộ môn CNTT", "SCH_VKU"),
                TeacherDetail("GV002", "Lê Minh Tuấn", "tuan.le@vku.edu.vn", "0909123458", "https://randomuser.me/api/portraits/men/2.jpg", "Nam", "15/07/1988", "Hà Nội", 10, listOf("Tiến sĩ KHMT - ĐH Bách Khoa HN"), listOf("Nhà giáo ưu tú 2020", "Nghiên cứu KH xuất sắc"), listOf("CTDL", "Giải thuật", "Python"), "Giảng viên chính", "SCH_VKU"),
                TeacherDetail("GV003", "Phạm Thị Hồng", "hong.pham@vku.edu.vn", "0909123459", "https://randomuser.me/api/portraits/women/3.jpg", "Nữ", "20/10/1990", "TP.HCM", 8, listOf("Thạc sĩ CNTT - ĐH Bách Khoa TP.HCM"), listOf("Sáng kiến đổi mới GD 2022"), listOf("Web", "React", "Node.js"), "Giảng viên", "SCH_VKU"),
                TeacherDetail("GV004", "Nguyễn Văn An", "an.nguyen@vku.edu.vn", "0909123460", "https://randomuser.me/api/portraits/men/4.jpg", "Nam", "10/12/1985", "Đà Nẵng", 12, listOf("Tiến sĩ ATTT - ĐH Bách Khoa ĐN"), listOf("Công trình NCKH 2019"), listOf("Bảo mật", "Mạng máy tính"), "Phó trưởng khoa", "SCH_VKU"),
                TeacherDetail("GV005", "Hoàng Thị Lan", "lan.hoang@dut.udn.vn", "0909123461", "https://randomuser.me/api/portraits/women/5.jpg", "Nữ", "25/05/1992", "Huế", 6, listOf("Thạc sĩ Toán - ĐH Sư phạm ĐN"), listOf("GV hướng dẫn giỏi 2024"), listOf("Toán cao cấp", "Xác suất thống kê"), "Giảng viên", "SCH_DUT"),
                TeacherDetail("GV006", "Vũ Minh Đức", "duc.vu@dut.udn.vn", "0909123462", "https://randomuser.me/api/portraits/men/6.jpg", "Nam", "30/08/1987", "Hải Phòng", 11, listOf("Tiến sĩ Cơ khí - ĐH Bách Khoa HN"), listOf("Công trình cơ khí xuất sắc"), listOf("Cơ khí", "CAD/CAM", "SolidWorks"), "Trưởng phòng Đào tạo", "SCH_DUT"),
                TeacherDetail("GV007", "Trần Thị Mai", "mai.tran@dut.udn.vn", "0909123463", "https://randomuser.me/api/portraits/women/7.jpg", "Nữ", "18/04/1993", "Nha Trang", 7, listOf("Thạc sĩ Điện tử - ĐH Bách Khoa ĐN"), listOf("GV trẻ triển vọng 2021"), listOf("Điện tử", "Vi điều khiển", "Arduino"), "Giảng viên", "SCH_DUT"),
                TeacherDetail("GV008", "Đỗ Văn Hải", "hai.do@due.edu.vn", "0909123464", "https://randomuser.me/api/portraits/men/8.jpg", "Nam", "05/11/1984", "Cần Thơ", 14, listOf("Tiến sĩ Kinh tế - ĐH Kinh tế TP.HCM"), listOf("Giải thưởng NCKH 2018"), listOf("Kinh tế vi mô", "Kinh tế vĩ mô"), "Trưởng khoa Kinh tế", "SCH_DUE"),
                TeacherDetail("GV009", "Bùi Thị Thu", "thu.bui@due.edu.vn", "0909123465", "https://randomuser.me/api/portraits/women/9.jpg", "Nữ", "22/02/1994", "Vinh", 6, listOf("Thạc sĩ QTKD - ĐH Kinh tế ĐN"), listOf("GV dạy giỏi cấp trường 2023"), listOf("Quản trị học", "Hành vi tổ chức"), "Giảng viên", "SCH_DUE"),
                TeacherDetail("GV010", "Lý Hoàng Nam", "nam.ly@due.edu.vn", "0909123466", "https://randomuser.me/api/portraits/men/10.jpg", "Nam", "12/09/1989", "Đà Lạt", 9, listOf("Tiến sĩ Marketing - ĐH Kinh tế ĐN"), listOf("Sáng kiến đổi mới PP giảng dạy"), listOf("Marketing", "Thương hiệu", "Digital Marketing"), "Phó trưởng khoa", "SCH_DUE")
            )

            for (t in teacherDetails) {
                val teacher = Teacher(
                    id = t.id, teacherCode = t.id,
                    name = t.name, email = t.email,
                    phone = t.phone, avatar = t.avatar,
                    gender = t.gender, birthday = t.birthday,
                    address = t.address, yearsOfExperience = t.yearsExp,
                    qualifications = t.qualifications,
                    achievements = t.achievements,
                    subjects = t.subjects, position = t.position,
                    schoolId = t.schoolId, status = "active"
                )
                firebase.createTeacher(teacher)
                total++
                println("   ✅ ${t.id} | ${t.name} | ${t.yearsExp} năm | ${t.position}")
            }

            // ============ 5. TẠO HỌC SINH CHI TIẾT ============
            println("\n📥 [5/8] Đang tạo Students chi tiết...")

            val studentDetails = listOf(
                StudentDetail("HS001", "Nguyễn Minh Quân", "minhquan.nguyen@student.vku.edu.vn", "0987654101", "https://randomuser.me/api/portraits/men/11.jpg", "Nam", "15/05/2006", "CL_VKU_01", "24IT1 - KTPM", "CNPM", 8.5, "Khá", "Tốt", "CLB Lập trình", "🥈 Giải Nhì Tin học trẻ 2024", "SCH_VKU"),
                StudentDetail("HS002", "Trần Phương Thảo", "phuongthao.tran@student.vku.edu.vn", "0987654102", "https://randomuser.me/api/portraits/women/12.jpg", "Nữ", "20/08/2006", "CL_VKU_01", "24IT1 - KTPM", "CNPM", 9.2, "Giỏi", "Tốt", "CLB Robotic", "🥇 Giải Nhất Robotic 2024", "SCH_VKU"),
                StudentDetail("HS003", "Lê Hoàng Nam", "hoangnam.le@student.vku.edu.vn", "0987654103", "https://randomuser.me/api/portraits/men/13.jpg", "Nam", "10/11/2006", "CL_VKU_02", "24IT2 - KHMT", "KHMT", 7.8, "Trung bình", "Khá", "CLB Bóng đá", "", "SCH_VKU"),
                StudentDetail("HS004", "Phạm Minh Đức", "minhduc.pham@student.vku.edu.vn", "0987654104", "https://randomuser.me/api/portraits/men/14.jpg", "Nam", "25/12/2006", "CL_VKU_02", "24IT2 - KHMT", "KHMT", 8.9, "Giỏi", "Tốt", "CLB AI", "🥈 Giải Nhì Olympic Tin học 2024", "SCH_VKU"),
                StudentDetail("HS005", "Nguyễn Thùy Trang", "thuytrang.nguyen@student.vku.edu.vn", "0987654105", "https://randomuser.me/api/portraits/women/15.jpg", "Nữ", "05/03/2007", "CL_VKU_03", "24IT3 - ATTT", "ATTT", 9.5, "Xuất sắc", "Tốt", "CLB An ninh mạng", "🥇 Học sinh xuất sắc nhất khối", "SCH_VKU"),
                StudentDetail("HS006", "Đỗ Thị Kim Anh", "kimanh.do@student.dut.udn.vn", "0987654106", "https://randomuser.me/api/portraits/women/16.jpg", "Nữ", "12/06/2006", "CL_DUT_01", "CNTT K24", "CNTT", 8.2, "Khá", "Tốt", "CLB Tin học", "", "SCH_DUT"),
                StudentDetail("HS007", "Vũ Hoàng Long", "hoanglong.vu@student.dut.udn.vn", "0987654107", "https://randomuser.me/api/portraits/men/17.jpg", "Nam", "18/09/2006", "CL_DUT_01", "CNTT K24", "CNTT", 7.5, "Trung bình", "Khá", "CLB Game", "", "SCH_DUT"),
                StudentDetail("HS008", "Bùi Thanh Tùng", "thanhtung.bui@student.dut.udn.vn", "0987654108", "https://randomuser.me/api/portraits/men/18.jpg", "Nam", "22/01/2006", "CL_DUT_02", "Cơ khí K24", "Cơ khí", 8.7, "Giỏi", "Tốt", "CLB Cơ khí", "🥉 Giải Ba Sáng tạo Kỹ thuật", "SCH_DUT"),
                StudentDetail("HS009", "Hoàng Thị Ngọc", "thingoc.hoang@student.due.edu.vn", "0987654109", "https://randomuser.me/api/portraits/women/19.jpg", "Nữ", "30/04/2006", "CL_DUE_01", "Kinh tế K24", "Kinh tế", 8.0, "Khá", "Tốt", "CLB Kinh tế", "", "SCH_DUE"),
                StudentDetail("HS010", "Lê Văn Thành", "vanthanh.le@student.due.edu.vn", "0987654110", "https://randomuser.me/api/portraits/men/20.jpg", "Nam", "08/07/2006", "CL_DUE_01", "QTKD K24", "QTKD", 9.0, "Giỏi", "Tốt", "CLB Khởi nghiệp", "🏆 Giải Nhất Ý tưởng Kinh doanh", "SCH_DUE")
            )

            for (s in studentDetails) {
                val student = Student(
                    id = s.id, studentCode = s.id,
                    name = s.name, email = s.email,
                    phone = s.phone, avatar = s.avatar,
                    gender = s.gender, birthday = s.birthday,
                    address = "Đà Nẵng",
                    schoolId = s.schoolId, schoolName = getSchoolName(s.schoolId),
                    classId = s.classId, className = s.className, major = s.major,
                    homeroomTeacherId = getHomeroomTeacherId(s.schoolId),
                    homeroomTeacherName = getHomeroomTeacherName(s.schoolId),
                    parentName = "Phụ huynh", parentPhone = "0901234567",
                    enrollmentYear = 2024, status = "active",
                    averageScore = s.avgScore, ranking = s.ranking,
                    conduct = s.conduct,
                    extracurricular = listOf(s.extracurricular),
                    awards = if (s.awards.isNotEmpty()) listOf(s.awards) else emptyList()
                )
                firebase.createStudent(student)
                total++
                println("   ✅ ${s.id} | ${s.name} | ${s.className} | ${s.avgScore} điểm")
            }

            // ============ 6. TẠO MÔN HỌC ============
            println("\n📥 [6/8] Đang tạo Subjects...")

            val subjects = listOf(
                Subject("SUB01", "Lập trình Android cơ bản", "AND101", 3, 45, listOf("Android", "Kotlin", "XML", "Firebase"), listOf("1", "2")),
                Subject("SUB02", "Kotlin & Coroutines", "AND201", 2, 30, listOf("Kotlin", "Coroutines", "Flow"), listOf("2", "3")),
                Subject("SUB03", "Firebase & Cloud", "CLD101", 3, 45, listOf("Firebase", "Firestore", "Storage"), listOf("2", "3")),
                Subject("SUB04", "Cấu trúc dữ liệu & Giải thuật", "CS201", 4, 60, listOf("DS", "Algorithms", "Sorting", "Graph"), listOf("1", "2")),
                Subject("SUB05", "Machine Learning cơ bản", "AI101", 3, 45, listOf("Python", "ML", "TensorFlow"), listOf("3", "4")),
                Subject("SUB06", "React & Modern Web", "WEB201", 3, 45, listOf("React", "JavaScript", "HTML/CSS"), listOf("2", "3")),
                Subject("SUB07", "Cơ sở dữ liệu", "DB101", 3, 45, listOf("SQL", "MySQL", "MongoDB"), listOf("1", "2")),
                Subject("SUB08", "Bảo mật & An toàn TT", "SEC201", 3, 45, listOf("Security", "Cryptography"), listOf("3", "4")),
                Subject("SUB09", "DevOps & Docker", "CLD201", 2, 30, listOf("Docker", "Kubernetes", "CI/CD"), listOf("3", "4")),
                Subject("SUB10", "Unity & Game Dev", "GAME101", 3, 45, listOf("Unity", "C#", "2D/3D"), listOf("2", "3")),
                Subject("SUB11", "Toán cao cấp", "MATH101", 4, 60, listOf("Calculus", "Linear Algebra"), listOf("1")),
                Subject("SUB12", "Kinh tế vi mô", "ECO101", 3, 45, listOf("Microeconomics", "Supply/Demand"), listOf("1")),
                Subject("SUB13", "Quản trị học", "MGT101", 3, 45, listOf("Management", "Leadership"), listOf("2")),
                Subject("SUB14", "Marketing căn bản", "MKT101", 3, 45, listOf("Marketing", "Branding"), listOf("2")),
                Subject("SUB15", "Tiếng Anh chuyên ngành", "ENG201", 2, 30, listOf("English", "Technical Writing"), listOf("2"))
            )

            for (s in subjects) {
                firebase.createSubject(s)
                total++
                println("   ✅ ${s.code} | ${s.name} | ${s.credits} tín chỉ")
            }

            // ============ 7. TẠO BẢNG ĐIỂM ============
            println("\n📥 [7/8] Đang tạo Grades...")

            val grades = listOf(
                Grade(
                    id = "GRD001",
                    studentId = "HS001",
                    studentName = "Nguyễn Minh Quân",
                    classId = "CL_VKU_01",
                    className = "24IT1 - KTPM",
                    subjectId = "SUB01",
                    subjectName = "Lập trình Android",
                    score15min = 8.5,
                    score45min = 9.0,
                    scoreMidterm = 8.5,
                    scoreFinal = 9.0,
                    average = 8.8,
                    semester = "HK1",
                    year = 2024,
                    rank = 2
                ),
                Grade(
                    id = "GRD002",
                    studentId = "HS001",
                    studentName = "Nguyễn Minh Quân",
                    classId = "CL_VKU_01",
                    className = "24IT1 - KTPM",
                    subjectId = "SUB04",
                    subjectName = "Cấu trúc dữ liệu",
                    score15min = 7.5,
                    score45min = 8.0,
                    scoreMidterm = 8.0,
                    scoreFinal = 8.5,
                    average = 8.1,
                    semester = "HK1",
                    year = 2024,
                    rank = 5
                ),
                Grade(
                    id = "GRD003",
                    studentId = "HS001",
                    studentName = "Nguyễn Minh Quân",
                    classId = "CL_VKU_01",
                    className = "24IT1 - KTPM",
                    subjectId = "SUB07",
                    subjectName = "Cơ sở dữ liệu",
                    score15min = 9.0,
                    score45min = 8.5,
                    scoreMidterm = 8.5,
                    scoreFinal = 9.0,
                    average = 8.7,
                    semester = "HK1",
                    year = 2024,
                    rank = 3
                ),
                Grade(
                    id = "GRD004",
                    studentId = "HS002",
                    studentName = "Trần Phương Thảo",
                    classId = "CL_VKU_01",
                    className = "24IT1 - KTPM",
                    subjectId = "SUB01",
                    subjectName = "Lập trình Android",
                    score15min = 9.5,
                    score45min = 9.5,
                    scoreMidterm = 9.0,
                    scoreFinal = 9.5,
                    average = 9.3,
                    semester = "HK1",
                    year = 2024,
                    rank = 1
                ),
                Grade(
                    id = "GRD005",
                    studentId = "HS002",
                    studentName = "Trần Phương Thảo",
                    classId = "CL_VKU_01",
                    className = "24IT1 - KTPM",
                    subjectId = "SUB04",
                    subjectName = "Cấu trúc dữ liệu",
                    score15min = 9.0,
                    score45min = 9.0,
                    scoreMidterm = 8.5,
                    scoreFinal = 9.0,
                    average = 8.9,
                    semester = "HK1",
                    year = 2024,
                    rank = 2
                ),
                Grade(
                    id = "GRD006",
                    studentId = "HS005",
                    studentName = "Nguyễn Thùy Trang",
                    classId = "CL_VKU_03",
                    className = "24IT3 - ATTT",
                    subjectId = "SUB08",
                    subjectName = "Bảo mật & ATTT",
                    score15min = 9.5,
                    score45min = 10.0,
                    scoreMidterm = 9.5,
                    scoreFinal = 9.5,
                    average = 9.6,
                    semester = "HK1",
                    year = 2024,
                    rank = 1
                ),
                Grade(
                    id = "GRD007",
                    studentId = "HS005",
                    studentName = "Nguyễn Thùy Trang",
                    classId = "CL_VKU_03",
                    className = "24IT3 - ATTT",
                    subjectId = "SUB03",
                    subjectName = "Firebase & Cloud",
                    score15min = 8.5,
                    score45min = 9.0,
                    scoreMidterm = 9.0,
                    scoreFinal = 9.0,
                    average = 8.9,
                    semester = "HK1",
                    year = 2024,
                    rank = 3
                ),
                Grade(
                    id = "GRD008",
                    studentId = "HS003",
                    studentName = "Lê Hoàng Nam",
                    classId = "CL_VKU_02",
                    className = "24IT2 - KHMT",
                    subjectId = "SUB04",
                    subjectName = "Cấu trúc dữ liệu",
                    score15min = 7.0,
                    score45min = 7.5,
                    scoreMidterm = 7.0,
                    scoreFinal = 8.0,
                    average = 7.4,
                    semester = "HK1",
                    year = 2024,
                    rank = 8
                ),
                Grade(
                    id = "GRD009",
                    studentId = "HS004",
                    studentName = "Phạm Minh Đức",
                    classId = "CL_VKU_02",
                    className = "24IT2 - KHMT",
                    subjectId = "SUB05",
                    subjectName = "Machine Learning",
                    score15min = 8.5,
                    score45min = 9.0,
                    scoreMidterm = 8.5,
                    scoreFinal = 9.0,
                    average = 8.8,
                    semester = "HK1",
                    year = 2024,
                    rank = 3
                ),
                Grade(
                    id = "GRD010",
                    studentId = "HS006",
                    studentName = "Đỗ Thị Kim Anh",
                    classId = "CL_DUT_01",
                    className = "CNTT K24",
                    subjectId = "SUB06",
                    subjectName = "React & Web",
                    score15min = 8.0,
                    score45min = 8.5,
                    scoreMidterm = 8.0,
                    scoreFinal = 8.5,
                    average = 8.3,
                    semester = "HK1",
                    year = 2024,
                    rank = 4
                )
            )

            for (g in grades) {
                firebase.createGrade(g)
                total++
                println("   ✅ ${g.id} | ${g.studentName} | ${g.subjectName} | ${g.average} điểm")
            }

            // ============ 8. TẠO ĐIỂM DANH ============
            println("\n📥 [8/8] Đang tạo Attendances...")

            val attendances = listOf(
                Attendance("ATD001", "HS001", "Nguyễn Minh Quân", "CL_VKU_01", "24IT1 - KTPM", "2025-01-15", "present", "", "HK2", 2),
                Attendance("ATD002", "HS001", "Nguyễn Minh Quân", "CL_VKU_01", "24IT1 - KTPM", "2025-01-16", "present", "", "HK2", 2),
                Attendance("ATD003", "HS001", "Nguyễn Minh Quân", "CL_VKU_01", "24IT1 - KTPM", "2025-01-17", "absent", "Có phép", "HK2", 2),
                Attendance("ATD004", "HS002", "Trần Phương Thảo", "CL_VKU_01", "24IT1 - KTPM", "2025-01-15", "present", "", "HK2", 2),
                Attendance("ATD005", "HS002", "Trần Phương Thảo", "CL_VKU_01", "24IT1 - KTPM", "2025-01-16", "present", "", "HK2", 2),
                Attendance("ATD006", "HS002", "Trần Phương Thảo", "CL_VKU_01", "24IT1 - KTPM", "2025-01-17", "present", "", "HK2", 2)
            )

            for (a in attendances) {
                firebase.createAttendance(a)
                total++
                println("   ✅ ${a.id} | ${a.studentName} | ${a.date} | ${a.status}")
            }

            // ============ 9. TẠO THÔNG BÁO ============
            println("\n📥 [9/9] Đang tạo Notifications...")

            val notifications = listOf(
                Notification(
                    id = "NOTI_001",
                    title = "Họp hội đồng sư phạm",
                    content = "Tất cả giáo viên tập trung tại hội trường lúc 8h sáng thứ 2 tuần tới.",
                    type = "announcement",
                    targetRoles = listOf("teacher", "admin"),
                    senderName = "BGH Nhà trường",
                    createdAt = System.currentTimeMillis() - 86400000 // 1 ngày trước
                ),
                Notification(
                    id = "NOTI_002",
                    title = "Nhắc nhở nhập điểm HK1",
                    content = "Hạn cuối nhập điểm học kỳ 1 là ngày 20/05. Đề nghị các thầy cô hoàn tất đúng hạn.",
                    type = "announcement",
                    targetRoles = listOf("teacher"),
                    senderName = "Phòng Đào tạo",
                    createdAt = System.currentTimeMillis() - 43200000 // 12h trước
                ),
                Notification(
                    id = "NOTI_003",
                    title = "Lịch thi giữa kỳ dự kiến",
                    content = "Lịch thi giữa kỳ sẽ bắt đầu từ ngày 25/05. Học sinh và giáo viên lưu ý xem lịch chi tiết.",
                    type = "exam",
                    targetRoles = listOf("teacher", "student"),
                    senderName = "Phòng Khảo thí",
                    createdAt = System.currentTimeMillis() - 172800000 // 2 ngày trước
                )
            )

            for (n in notifications) {
                firebase.createNotification(n)
                total++
                println("   ✅ ${n.id} | ${n.title}")
            }

            println()
            println("============================================================")
            println("✅ SEED DỮ LIỆU HOÀN TẤT!")
            println("📊 Tổng số bản ghi đã tạo: $total")
            println("============================================================")
            println()
            println("🔐 THÔNG TIN ĐĂNG NHẬP:")
            println("   👨‍💼 Admin: chamm4930@gmail.com / 12345678")
            println("   👩‍🏫 Giáo viên VKU: tramntb.24it@vku.udn.vn / 12345678")
            println("   👩‍🏫 Giáo viên Bách Khoa: lan.hoang@dut.udn.vn / 12345678")
            println("   👩‍🏫 Giáo viên Kinh tế: hai.do@due.edu.vn / 12345678")
            println("   👨‍🎓 Học sinh: minhquan.nguyen@student.vku.edu.vn / 12345678")
            println("============================================================")
            println()
            println("🗺️ CÁC TRƯỜNG ĐÃ THÊM VỚI VỊ TRÍ MAP:")
            println("   📍 ĐH Việt - Hàn: 16.0742, 108.1494")
            println("   📍 ĐH Bách Khoa ĐN: 16.0725, 108.1512")
            println("   📍 ĐH Kinh tế ĐN: 16.0738, 108.1505")
            println("============================================================")

        } catch (e: Exception) {
            println("❌ LỖI: ${e.message}")
            e.printStackTrace()
        }
    }

    // Helper functions
    private fun getSchoolName(schoolId: String): String = when (schoolId) {
        "SCH_VKU" -> "ĐH Công nghệ Thông tin & Truyền thông Việt - Hàn"
        "SCH_DUT" -> "Đại học Bách Khoa - ĐH Đà Nẵng"
        "SCH_DUE" -> "Đại học Kinh tế - ĐH Đà Nẵng"
        else -> ""
    }

    private fun getHomeroomTeacherId(schoolId: String): String = when (schoolId) {
        "SCH_VKU" -> "GV001"
        "SCH_DUT" -> "GV005"
        "SCH_DUE" -> "GV008"
        else -> "GV001"
    }

    private fun getHomeroomTeacherName(schoolId: String): String = when (schoolId) {
        "SCH_VKU" -> "Trần Nguyễn Bảo Trâm"
        "SCH_DUT" -> "Hoàng Thị Lan"
        "SCH_DUE" -> "Đỗ Văn Hải"
        else -> ""
    }

    // Data classes
    data class TeacherUser(val id: String, val name: String, val email: String, val phone: String, val avatar: String, val gender: String, val birthday: String, val degree: String, val exp: Int, val position: String, val schoolId: String)
    data class StudentUser(val id: String, val name: String, val email: String, val phone: String, val avatar: String, val gender: String, val birthday: String, val classId: String, val major: String, val schoolId: String)
    data class TeacherDetail(val id: String, val name: String, val email: String, val phone: String, val avatar: String, val gender: String, val birthday: String, val address: String, val yearsExp: Int, val qualifications: List<String>, val achievements: List<String>, val subjects: List<String>, val position: String, val schoolId: String)
    data class StudentDetail(val id: String, val name: String, val email: String, val phone: String, val avatar: String, val gender: String, val birthday: String, val classId: String, val className: String, val major: String, val avgScore: Double, val ranking: String, val conduct: String, val extracurricular: String, val awards: String, val schoolId: String)
}