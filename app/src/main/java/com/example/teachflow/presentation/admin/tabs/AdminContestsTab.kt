package com.example.teachflow.presentation.admin.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.presentation.admin.PurpleAccent
import com.example.teachflow.presentation.admin.components.*
import kotlinx.coroutines.launch

data class ContestData(
    val id: String,
    val name: String,
    val icon: String,
    val startDate: String,
    val endDate: String,
    val participants: Int,
    val prize: String,
    val status: String,
    val description: String = "",
    val location: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContestsTab(padding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedContest by remember { mutableStateOf<ContestData?>(null) }
    var showDetailDialog by remember { mutableStateOf(false) }

    var contests by remember { mutableStateOf(listOf(
        ContestData("1", "Olympic Tin học 2025", "🏆", "15/05/2025", "30/05/2025", 245, "100.000.000đ", "active", "Cuộc thi Olympic Tin học dành cho sinh viên", "Đại học Bách Khoa Đà Nẵng"),
        ContestData("2", "Hackathon Sáng tạo", "💻", "20/05/2025", "25/05/2025", 89, "50.000.000đ", "active", "Hackathon tìm kiếm giải pháp công nghệ", "Online"),
        ContestData("3", "Code Challenge", "🚀", "25/05/2025", "28/05/2025", 156, "30.000.000đ", "upcoming", "Thử thách lập trình 24h", "Online"),
        ContestData("4", "AI Competition", "🤖", "10/06/2025", "20/06/2025", 67, "80.000.000đ", "upcoming", "Cuộc thi trí tuệ nhân tạo", "Đại học Bách Khoa Đà Nẵng"),
        ContestData("5", "Game Dev Contest", "🎮", "05/07/2025", "15/07/2025", 34, "60.000.000đ", "draft", "Cuộc thi phát triển game", "Online")
    )) }

    val filteredContests = if (searchQuery.isEmpty()) contests else contests.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.status.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("🏆 Quản lý cuộc thi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Quản lý các cuộc thi, sự kiện", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                }
                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Thêm", color = NavyPrimary)
                }
            }
        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Tìm kiếm...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NavyPrimary,
                unfocusedBorderColor = TextGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter chips
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val statuses = listOf("Tất cả", "Đang diễn ra", "Sắp diễn ra", "Nháp")
            items(statuses) { status ->
                val isSelected = when(status) {
                    "Tất cả" -> searchQuery.isEmpty()
                    "Đang diễn ra" -> searchQuery == "active"
                    "Sắp diễn ra" -> searchQuery == "upcoming"
                    "Nháp" -> searchQuery == "draft"
                    else -> false
                }
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        searchQuery = when(status) {
                            "Tất cả" -> ""
                            "Đang diễn ra" -> "active"
                            "Sắp diễn ra" -> "upcoming"
                            "Nháp" -> "draft"
                            else -> ""
                        }
                    },
                    label = { Text(status, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NavyPrimary.copy(alpha = 0.1f),
                        selectedLabelColor = NavyPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Thống kê nhanh
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(contests.count { it.status == "active" }.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    Text("Đang diễn ra", fontSize = 11.sp, color = TextGray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(contests.count { it.status == "upcoming" }.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WarningOrange)
                    Text("Sắp diễn ra", fontSize = 11.sp, color = TextGray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(contests.sumOf { it.participants }.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                    Text("Thí sinh", fontSize = 11.sp, color = TextGray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(contests.size.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PurpleAccent)
                    Text("Tổng", fontSize = 11.sp, color = TextGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Danh sách cuộc thi
        if (filteredContests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏆", fontSize = 64.sp)
                    Text("Không tìm thấy cuộc thi", fontSize = 16.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                    ) {
                        Text("Thêm cuộc thi mới")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredContests) { contest ->
                    ContestCardAdmin(
                        contest = contest,
                        onEdit = {
                            selectedContest = contest
                            showAddDialog = true
                        },
                        onDelete = { contests = contests.filter { it.id != contest.id } },
                        onViewDetails = {
                            selectedContest = contest
                            showDetailDialog = true
                        }
                    )
                }
            }
        }
    }

    // Dialog thêm/sửa
    if (showAddDialog) {
        AddEditContestDialog(
            contest = selectedContest,
            onDismiss = {
                showAddDialog = false
                selectedContest = null
            },
            onSuccess = { newContest ->
                if (selectedContest != null) {
                    contests = contests.map { if (it.id == newContest.id) newContest else it }
                } else {
                    contests = contests + newContest
                }
                showAddDialog = false
                selectedContest = null
            }
        )
    }

    // Dialog chi tiết
    if (showDetailDialog && selectedContest != null) {
        ContestDetailDialog(
            contest = selectedContest!!,
            onDismiss = {
                showDetailDialog = false
                selectedContest = null
            }
        )
    }
}

@Composable
fun ContestCardAdmin(
    contest: ContestData,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewDetails: () -> Unit
) {
    val statusColor = when (contest.status) {
        "active" -> SuccessGreen
        "upcoming" -> WarningOrange
        else -> TextGray
    }
    val statusText = when (contest.status) {
        "active" -> "Đang diễn ra"
        "upcoming" -> "Sắp diễn ra"
        else -> "Nháp"
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onViewDetails() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NavyPrimary.copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(contest.icon, fontSize = 28.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(contest.name, fontWeight = FontWeight.SemiBold, color = TextDark, fontSize = 16.sp, maxLines = 1)
                        Text("📅 ${contest.startDate} - ${contest.endDate}", fontSize = 12.sp, color = TextGray)
                    }
                }
                Surface(shape = RoundedCornerShape(12.dp), color = statusColor.copy(alpha = 0.1f)) {
                    Text(statusText, fontSize = 10.sp, color = statusColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(contest.description, fontSize = 12.sp, color = TextGray, maxLines = 2, modifier = Modifier.padding(bottom = 8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.People, null, modifier = Modifier.size(16.dp), tint = TextGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${contest.participants}", fontSize = 12.sp, color = TextGray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.MonetizationOn, null, modifier = Modifier.size(16.dp), tint = SuccessGreen)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(contest.prize, fontSize = 12.sp, color = SuccessGreen)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(onClick = onViewDetails, modifier = Modifier.size(width = 70.dp, height = 32.dp), shape = RoundedCornerShape(8.dp)) {
                    Text("Chi tiết", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onEdit, modifier = Modifier.size(width = 60.dp, height = 32.dp), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningOrange)) {
                    Text("Sửa", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = ErrorRed), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(width = 60.dp, height = 32.dp)) {
                    Text("Xóa", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContestDialog(
    contest: ContestData?,
    onDismiss: () -> Unit,
    onSuccess: (ContestData) -> Unit
) {
    var name by remember { mutableStateOf(contest?.name ?: "") }
    var icon by remember { mutableStateOf(contest?.icon ?: "🏆") }
    var startDate by remember { mutableStateOf(contest?.startDate ?: "") }
    var endDate by remember { mutableStateOf(contest?.endDate ?: "") }
    var prize by remember { mutableStateOf(contest?.prize ?: "") }
    var status by remember { mutableStateOf(contest?.status ?: "upcoming") }
    var description by remember { mutableStateOf(contest?.description ?: "") }
    var location by remember { mutableStateOf(contest?.location ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

    val icons = listOf("🏆", "💻", "🚀", "🤖", "🎮", "📱", "⚡", "🎯")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (contest == null) "Thêm cuộc thi" else "Sửa cuộc thi", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Chọn biểu tượng:", fontSize = 12.sp, color = TextGray)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(icons) { itemIcon ->
                        Surface(
                            shape = CircleShape,
                            color = if (icon == itemIcon) NavyPrimary.copy(alpha = 0.1f) else Color.Transparent,
                            modifier = Modifier.size(40.dp).clickable { icon = itemIcon }
                        ) {
                            Box(contentAlignment = Alignment.Center) { Text(itemIcon, fontSize = 24.sp) }
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên cuộc thi") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Ngày bắt đầu") },
                        placeholder = { Text("DD/MM/YYYY") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("Ngày kết thúc") },
                        placeholder = { Text("DD/MM/YYYY") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Địa điểm") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = prize,
                    onValueChange = { prize = it },
                    label = { Text("Giải thưởng") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 3
                )

                Row {
                    Text("Trạng thái:", modifier = Modifier.weight(1f), fontSize = 12.sp, color = TextGray)
                    Row {
                        FilterChip(
                            selected = status == "active",
                            onClick = { status = "active" },
                            label = { Text("Đang diễn ra", fontSize = 11.sp) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        FilterChip(
                            selected = status == "upcoming",
                            onClick = { status = "upcoming" },
                            label = { Text("Sắp diễn ra", fontSize = 11.sp) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        FilterChip(
                            selected = status == "draft",
                            onClick = { status = "draft" },
                            label = { Text("Nháp", fontSize = 11.sp) }
                        )
                    }
                }

                if (error != null) {
                    Text(error!!, color = ErrorRed, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        error = "Vui lòng nhập tên cuộc thi"
                        return@Button
                    }
                    val newContest = ContestData(
                        id = contest?.id ?: System.currentTimeMillis().toString(),
                        name = name,
                        icon = icon,
                        startDate = startDate,
                        endDate = endDate,
                        participants = contest?.participants ?: 0,
                        prize = prize,
                        status = status,
                        description = description,
                        location = location
                    )
                    onSuccess(newContest)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text(if (contest == null) "Thêm" else "Cập nhật")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        }
    )
}

@Composable
fun ContestDetailDialog(
    contest: ContestData,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(contest.icon, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(contest.name, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailRow("📅 Thời gian", "${contest.startDate} - ${contest.endDate}")
                DetailRow("📍 Địa điểm", contest.location.ifEmpty { "Chưa cập nhật" })
                DetailRow("💰 Giải thưởng", contest.prize)
                DetailRow("👥 Thí sinh", "${contest.participants}")
                DetailRow("📝 Mô tả", contest.description.ifEmpty { "Chưa có mô tả" })
                DetailRow("📌 Trạng thái", when(contest.status) {
                    "active" -> "Đang diễn ra"
                    "upcoming" -> "Sắp diễn ra"
                    else -> "Nháp"
                })
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)) {
                Text("Đóng")
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextGray)
        Text(value, fontSize = 13.sp, color = TextDark, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}