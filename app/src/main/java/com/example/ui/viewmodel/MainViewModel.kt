package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.PartnerBusiness
import com.example.data.model.PartnerVoucher
import com.example.data.model.ScanLog
import com.example.data.model.Student
import com.example.data.model.VoucherRedemption
import com.example.data.remote.FirebaseSyncService
import com.example.data.repository.MembershipRepository
import com.example.data.repository.ScanValidationResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AuthMode {
    LOGGED_OUT,
    ADMIN,
    PARTNER_CASHIER
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val firebaseService = FirebaseSyncService()
    val repository = MembershipRepository(
        studentDao = db.studentDao(),
        scanLogDao = db.scanLogDao(),
        voucherDao = db.voucherDao(),
        businessDao = db.businessDao(),
        firebaseService = firebaseService
    )

    // Auth State
    private val _authMode = MutableStateFlow(AuthMode.LOGGED_OUT)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()

    private val _loggedInPartnerBusiness = MutableStateFlow<PartnerBusiness?>(null)
    val loggedInPartnerBusiness: StateFlow<PartnerBusiness?> = _loggedInPartnerBusiness.asStateFlow()

    val allBusinesses: StateFlow<List<PartnerBusiness>> = repository.allBusinesses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVouchers: StateFlow<List<PartnerVoucher>> = repository.allVouchers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val voucherRedemptions: StateFlow<List<VoucherRedemption>> = repository.allVoucherRedemptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current active organization semester
    private val _currentSemester = MutableStateFlow("2026-2027 | 1st Semester")
    val currentSemester: StateFlow<String> = _currentSemester.asStateFlow()

    // Available semester choices
    val availableSemesters = listOf(
        "2026-2027 | 1st Semester",
        "2026-2027 | 2nd Semester",
        "2025-2026 | 1st Semester",
        "2025-2026 | 2nd Semester"
    )

    // Daily/Event Reset Timestamp for Total Scans Metric
    private val _eventResetTimestamp = MutableStateFlow<Long>(0L)
    val eventResetTimestamp: StateFlow<Long> = _eventResetTimestamp.asStateFlow()

    fun resetDailyEventScans() {
        _eventResetTimestamp.value = System.currentTimeMillis()
    }

    // Roster search query & status filter
    private val _rosterSearchQuery = MutableStateFlow("")
    val rosterSearchQuery: StateFlow<String> = _rosterSearchQuery.asStateFlow()

    private val _rosterFilterPaidOnly = MutableStateFlow<Boolean?>(null) // null = all, true = active, false = unpaid
    val rosterFilterPaidOnly: StateFlow<Boolean?> = _rosterFilterPaidOnly.asStateFlow()

    // Filtered Student List
    val filteredStudents: StateFlow<List<Student>> = combine(
        repository.allStudents,
        _rosterSearchQuery,
        _rosterFilterPaidOnly
    ) { students, query, filterPaid ->
        students.filter { student ->
            val matchesQuery = query.isBlank() ||
                    student.fullName.contains(query, ignoreCase = true) ||
                    student.studentId.contains(query, ignoreCase = true) ||
                    student.department.contains(query, ignoreCase = true)

            val matchesFilter = when (filterPaid) {
                true -> student.isMembershipPaid
                false -> !student.isMembershipPaid
                null -> true
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Dashboard Logs search query & status filter
    private val _logSearchQuery = MutableStateFlow("")
    val logSearchQuery: StateFlow<String> = _logSearchQuery.asStateFlow()

    private val _selectedLogSemester = MutableStateFlow("2026-2027 | 1st Semester")
    val selectedLogSemester: StateFlow<String> = _selectedLogSemester.asStateFlow()

    val filteredLogs: StateFlow<List<ScanLog>> = combine(
        repository.allLogs,
        _logSearchQuery,
        _selectedLogSemester
    ) { logs, query, semester ->
        logs.filter { log ->
            val matchesSemester = semester == "All Semesters" || log.scannedSemester == semester
            val matchesQuery = query.isBlank() ||
                    log.studentName.contains(query, ignoreCase = true) ||
                    log.studentId.contains(query, ignoreCase = true) ||
                    log.status.contains(query, ignoreCase = true) ||
                    log.department.contains(query, ignoreCase = true)

            matchesSemester && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Scan result dialog state
    private val _latestScanResult = MutableStateFlow<ScanValidationResult?>(null)
    val latestScanResult: StateFlow<ScanValidationResult?> = _latestScanResult.asStateFlow()

    // Loading / scanning status
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun setSemester(semester: String) {
        _currentSemester.value = semester
        _selectedLogSemester.value = semester
    }

    fun setRosterSearchQuery(query: String) {
        _rosterSearchQuery.value = query
    }

    fun setRosterFilter(paidOnly: Boolean?) {
        _rosterFilterPaidOnly.value = paidOnly
    }

    fun setLogSearchQuery(query: String) {
        _logSearchQuery.value = query
    }

    fun setSelectedLogSemester(semester: String) {
        _selectedLogSemester.value = semester
    }

    fun processQrScan(qrPayload: String) {
        viewModelScope.launch {
            _isScanning.value = true
            val result = repository.validateScan(qrPayload, _currentSemester.value)
            _latestScanResult.value = result
            _isScanning.value = false
        }
    }

    fun clearScanResult() {
        _latestScanResult.value = null
    }

    fun renewStudentForCurrentSemester(studentId: String) {
        viewModelScope.launch {
            repository.renewMembershipForSemester(studentId, _currentSemester.value)
            // Re-evaluate scan result if currently open for this student
            val activeResult = _latestScanResult.value
            if (activeResult is ScanValidationResult.ExpiredSemester && activeResult.student.studentId == studentId) {
                _latestScanResult.value = ScanValidationResult.Valid(
                    activeResult.student.copy(
                        activeSemester = _currentSemester.value,
                        isMembershipPaid = true
                    ),
                    activeResult.log.copy(status = "VALID", notes = "Renewed for ${_currentSemester.value}")
                )
            } else if (activeResult is ScanValidationResult.PaymentPending && activeResult.student.studentId == studentId) {
                _latestScanResult.value = ScanValidationResult.Valid(
                    activeResult.student.copy(isMembershipPaid = true),
                    activeResult.log.copy(status = "VALID", notes = "Dues marked paid")
                )
            }
        }
    }

    fun saveStudent(student: Student) {
        viewModelScope.launch {
            repository.addOrUpdateStudent(student)
        }
    }

    fun deleteStudent(studentId: String) {
        viewModelScope.launch {
            repository.deleteStudent(studentId)
        }
    }

    fun currentMonthYearString(): String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        return sdf.format(Date())
    }

    fun redeemVoucher(
        student: Student,
        voucher: PartnerVoucher,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val monthYear = currentMonthYearString()
            val success = repository.redeemVoucherForStudent(student, voucher, monthYear)
            if (success) {
                onResult(true, "Successfully redeemed ${voucher.discountTitle} at ${voucher.businessName}!")
            } else {
                onResult(false, "Student already used a voucher at ${voucher.businessName} this month ($monthYear)!")
            }
        }
    }

    fun getStudentRedeemedBusinessesThisMonth(
        studentId: String,
        onResult: (Set<String>) -> Unit
    ) {
        viewModelScope.launch {
            val monthYear = currentMonthYearString()
            val list = repository.getRedemptionsForStudentThisMonth(studentId, monthYear)
            val set = list.map { it.businessId }.toSet()
            onResult(set)
        }
    }

    fun clearLogs() {
        viewModelScope.launch {
            repository.clearAllScanLogs()
        }
    }

    // Auth methods
    fun loginAdmin(user: String, pass: String): Boolean {
        if (user.trim() == "admin" && pass.trim() == "jmapup") {
            _authMode.value = AuthMode.ADMIN
            _loggedInPartnerBusiness.value = null
            return true
        }
        return false
    }

    fun loginPartner(business: PartnerBusiness, pin: String): Boolean {
        if (pin.trim() == business.pin.trim()) {
            _authMode.value = AuthMode.PARTNER_CASHIER
            _loggedInPartnerBusiness.value = business
            return true
        }
        return false
    }

    fun logout() {
        _authMode.value = AuthMode.LOGGED_OUT
        _loggedInPartnerBusiness.value = null
        _latestScanResult.value = null
    }

    // Business Management
    fun addOrUpdateBusiness(business: PartnerBusiness) {
        viewModelScope.launch {
            repository.addOrUpdateBusiness(business)
        }
    }

    fun deleteBusiness(businessId: String) {
        viewModelScope.launch {
            repository.deleteBusiness(businessId)
        }
    }

    // Voucher Management
    fun addOrUpdateVoucher(voucher: PartnerVoucher) {
        viewModelScope.launch {
            repository.addOrUpdateVoucher(voucher)
        }
    }

    fun deleteVoucher(voucherId: String) {
        viewModelScope.launch {
            repository.deleteVoucher(voucherId)
        }
    }
}
