package com.example.data.repository

import com.example.data.local.BusinessDao
import com.example.data.local.ScanLogDao
import com.example.data.local.StudentDao
import com.example.data.local.VoucherDao
import com.example.data.model.PartnerBusiness
import com.example.data.model.PartnerVoucher
import com.example.data.model.ScanLog
import com.example.data.model.Student
import com.example.data.model.VoucherRedemption
import com.example.data.remote.FirebaseSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

sealed class ScanValidationResult {
    data class Valid(val student: Student, val log: ScanLog) : ScanValidationResult()
    data class ExpiredSemester(val student: Student, val log: ScanLog) : ScanValidationResult()
    data class NotFound(val scannedId: String, val log: ScanLog) : ScanValidationResult()
    data class PaymentPending(val student: Student, val log: ScanLog) : ScanValidationResult()
}

class MembershipRepository(
    private val studentDao: StudentDao,
    private val scanLogDao: ScanLogDao,
    private val voucherDao: VoucherDao,
    private val businessDao: BusinessDao,
    private val firebaseService: FirebaseSyncService
) {

    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()
    val allLogs: Flow<List<ScanLog>> = scanLogDao.getAllLogs()
    val allVoucherRedemptions: Flow<List<VoucherRedemption>> = voucherDao.getAllRedemptions()
    val allBusinesses: Flow<List<PartnerBusiness>> = businessDao.getAllBusinesses()
    val allVouchers: Flow<List<PartnerVoucher>> = voucherDao.getAllVouchers()
    val isFirebaseAvailable: Boolean = firebaseService.isFirebaseAvailable

    suspend fun seedInitialDataIfEmpty() {
        val defaultBusinesses = listOf(
            PartnerBusiness("bus_1", "PUP Campus Cafe", "Food & Beverage", "1234"),
            PartnerBusiness("bus_2", "Santa Rosa Print & Copy Center", "Academic Services", "1234"),
            PartnerBusiness("bus_3", "National Book Store - Santa Rosa", "Retail & Supplies", "1234"),
            PartnerBusiness("bus_4", "ChaTime PUP Branch", "Food & Beverage", "1234"),
            PartnerBusiness("bus_5", "Iskolar Diner & Grill", "Food & Beverage", "1234")
        )

        val defaultVouchers = listOf(
            PartnerVoucher("v_1", "bus_1", "PUP Campus Cafe", "15% Off Coffee & Pastries", "Valid for all JMAP active members once per month", "Food & Beverage"),
            PartnerVoucher("v_2", "bus_2", "Santa Rosa Print & Copy Center", "20% Off Printing & Thesis Binding", "Valid for all JMAP active members once per month", "Academic Services"),
            PartnerVoucher("v_3", "bus_3", "National Book Store - Santa Rosa", "10% Off School Supplies", "Valid for all JMAP active members once per month", "Retail & Supplies"),
            PartnerVoucher("v_4", "bus_4", "ChaTime PUP Branch", "Free Extra Topping with Any Milk Tea", "Valid for all JMAP active members once per month", "Food & Beverage"),
            PartnerVoucher("v_5", "bus_5", "Iskolar Diner & Grill", "10% Student Discount on Meals", "Valid for all JMAP active members once per month", "Food & Beverage")
        )

        val existingBusinesses = businessDao.getAllBusinesses().first()
        if (existingBusinesses.isEmpty()) {
            businessDao.insertBusinesses(defaultBusinesses)
            if (isFirebaseAvailable) {
                CoroutineScope(Dispatchers.IO).launch {
                    for (bus in defaultBusinesses) {
                        firebaseService.syncBusinessToCloud(bus)
                    }
                }
            }
        }

        val existingVouchers = voucherDao.getAllVouchers().first()
        if (existingVouchers.isEmpty()) {
            voucherDao.insertVouchers(defaultVouchers)
            if (isFirebaseAvailable) {
                CoroutineScope(Dispatchers.IO).launch {
                    for (vouch in defaultVouchers) {
                        firebaseService.syncVoucherToCloud(vouch)
                    }
                }
            }
        }

        val existingStudents = studentDao.getAllStudents().first()
        if (existingStudents.isEmpty()) {
            val sampleStudents = listOf(
                Student(
                    studentId = "2026-00101-SR-0",
                    fullName = "Alex Chen",
                    email = "alex.chen@iskolarngbayan.pup.edu.ph",
                    department = "BSBA - Marketing Management",
                    activeSemester = "2026-2027 | 1st Semester",
                    isMembershipPaid = true,
                    joinYear = "2024",
                    notes = "JMAP Org President"
                ),
                Student(
                    studentId = "2026-00102-SR-0",
                    fullName = "Sarah Jenkins",
                    email = "sarah.jenkins@iskolarngbayan.pup.edu.ph",
                    department = "BSBA - Marketing Management",
                    activeSemester = "2026-2027 | 1st Semester",
                    isMembershipPaid = true,
                    joinYear = "2025"
                ),
                Student(
                    studentId = "2026-00103-SR-0",
                    fullName = "Marcus Vance",
                    email = "marcus.vance@iskolarngbayan.pup.edu.ph",
                    department = "BSBA - Marketing Management",
                    activeSemester = "2025-2026 | 2nd Semester",
                    isMembershipPaid = false,
                    joinYear = "2024",
                    notes = "Needs renewal for 2026-2027 | 1st Semester"
                ),
                Student(
                    studentId = "2026-00104-SR-0",
                    fullName = "Elena Rostova",
                    email = "elena.rostova@iskolarngbayan.pup.edu.ph",
                    department = "BSBA - Marketing Management",
                    activeSemester = "2026-2027 | 1st Semester",
                    isMembershipPaid = true,
                    joinYear = "2023"
                ),
                Student(
                    studentId = "2026-00105-SR-0",
                    fullName = "David Kim",
                    email = "david.kim@iskolarngbayan.pup.edu.ph",
                    department = "BSBA - Marketing Management",
                    activeSemester = "2026-2027 | 1st Semester",
                    isMembershipPaid = false,
                    joinYear = "2023",
                    notes = "Membership dues payment pending"
                ),
                Student(
                    studentId = "2026-00106-SR-0",
                    fullName = "Maya Lin",
                    email = "maya.lin@iskolarngbayan.pup.edu.ph",
                    department = "BSBA - Marketing Management",
                    activeSemester = "2026-2027 | 1st Semester",
                    isMembershipPaid = true,
                    joinYear = "2024"
                )
            )
            studentDao.insertStudents(sampleStudents)

            // Seed initial sample scan logs
            val now = System.currentTimeMillis()
            val sampleLogs = listOf(
                ScanLog(
                    studentId = "2026-00101-SR-0",
                    studentName = "Alex Chen",
                    department = "BSBA - Marketing Management",
                    scannedSemester = "2026-2027 | 1st Semester",
                    status = "VALID",
                    scannedAt = now - 3600000,
                    notes = "JMAP Orientation Check-in"
                ),
                ScanLog(
                    studentId = "2026-00103-SR-0",
                    studentName = "Marcus Vance",
                    department = "BSBA - Marketing Management",
                    scannedSemester = "2026-2027 | 1st Semester",
                    status = "EXPIRED_SEMESTER",
                    scannedAt = now - 7200000,
                    notes = "Needs 2025-2026 | 2nd Semester -> 2026-2027 | 1st Semester renewal"
                ),
                ScanLog(
                    studentId = "UNKNOWN-99",
                    studentName = "Unregistered Student",
                    department = "N/A",
                    scannedSemester = "2026-2027 | 1st Semester",
                    status = "NOT_FOUND",
                    scannedAt = now - 10800000,
                    notes = "Card not registered in JMAP directory"
                )
            )
            for (log in sampleLogs) {
                scanLogDao.insertLog(log)
            }

            // Sync to Firebase if connected
            if (isFirebaseAvailable) {
                CoroutineScope(Dispatchers.IO).launch {
                    for (student in sampleStudents) {
                        firebaseService.syncStudentToCloud(student)
                    }
                    for (log in sampleLogs) {
                        firebaseService.syncScanLogToCloud(log)
                    }
                }
            }
        }
    }

    suspend fun validateScan(qrPayload: String, currentSemester: String): ScanValidationResult {
        // Extract studentId from QR code payload (supports raw ID or JSON {"studentId":"..."} format)
        val extractedId = parseStudentId(qrPayload)
        val student = studentDao.getStudentById(extractedId)
        val now = System.currentTimeMillis()

        if (student == null) {
            val log = ScanLog(
                studentId = extractedId,
                studentName = "Unknown Student",
                department = "N/A",
                scannedSemester = currentSemester,
                status = "NOT_FOUND",
                scannedAt = now,
                notes = "QR code scanned but student ID not found in database"
            )
            scanLogDao.insertLog(log)
            firebaseService.syncScanLogToCloud(log)
            return ScanValidationResult.NotFound(extractedId, log)
        }

        // Update last scanned timestamp
        val updatedStudent = student.copy(lastScannedAt = now, updatedAt = now)
        studentDao.updateStudent(updatedStudent)
        firebaseService.syncStudentToCloud(updatedStudent)

        // Check active semester and payment status
        return when {
            student.activeSemester == currentSemester && student.isMembershipPaid -> {
                val log = ScanLog(
                    studentId = student.studentId,
                    studentName = student.fullName,
                    department = student.department,
                    scannedSemester = currentSemester,
                    status = "VALID",
                    scannedAt = now,
                    notes = "Membership active for $currentSemester"
                )
                scanLogDao.insertLog(log)
                firebaseService.syncScanLogToCloud(log)
                ScanValidationResult.Valid(updatedStudent, log)
            }
            student.activeSemester == currentSemester && !student.isMembershipPaid -> {
                val log = ScanLog(
                    studentId = student.studentId,
                    studentName = student.fullName,
                    department = student.department,
                    scannedSemester = currentSemester,
                    status = "PAYMENT_PENDING",
                    scannedAt = now,
                    notes = "Dues pending for $currentSemester"
                )
                scanLogDao.insertLog(log)
                firebaseService.syncScanLogToCloud(log)
                ScanValidationResult.PaymentPending(updatedStudent, log)
            }
            else -> {
                val log = ScanLog(
                    studentId = student.studentId,
                    studentName = student.fullName,
                    department = student.department,
                    scannedSemester = currentSemester,
                    status = "EXPIRED_SEMESTER",
                    scannedAt = now,
                    notes = "Registered for ${student.activeSemester}, needs renewal for $currentSemester"
                )
                scanLogDao.insertLog(log)
                firebaseService.syncScanLogToCloud(log)
                ScanValidationResult.ExpiredSemester(updatedStudent, log)
            }
        }
    }

    suspend fun renewMembershipForSemester(studentId: String, semester: String): Boolean {
        val student = studentDao.getStudentById(studentId) ?: return false
        val updated = student.copy(
            activeSemester = semester,
            isMembershipPaid = true,
            updatedAt = System.currentTimeMillis()
        )
        studentDao.updateStudent(updated)
        firebaseService.syncStudentToCloud(updated)
        return true
    }

    suspend fun addOrUpdateStudent(student: Student) {
        studentDao.insertStudent(student)
        firebaseService.syncStudentToCloud(student)
    }

    suspend fun deleteStudent(studentId: String) {
        studentDao.deleteStudentById(studentId)
        firebaseService.deleteStudentFromCloud(studentId)
    }

    suspend fun clearAllScanLogs() {
        scanLogDao.clearLogs()
    }

    suspend fun getRedemptionsForStudentThisMonth(studentId: String, monthYear: String): List<VoucherRedemption> {
        return voucherDao.getRedemptionsForStudentThisMonth(studentId, monthYear)
    }

    suspend fun checkCanStudentRedeemVoucher(studentId: String, businessId: String, monthYear: String): Boolean {
        val usedCount = voucherDao.checkStudentBusinessUsageThisMonth(studentId, businessId, monthYear)
        return usedCount == 0
    }

    suspend fun redeemVoucherForStudent(
        student: Student,
        voucher: PartnerVoucher,
        monthYear: String
    ): Boolean {
        if (!checkCanStudentRedeemVoucher(student.studentId, voucher.id, monthYear)) {
            return false // Already redeemed this month!
        }

        val redemption = VoucherRedemption(
            studentId = student.studentId,
            studentName = student.fullName,
            businessId = voucher.id,
            businessName = voucher.businessName,
            discountTitle = voucher.discountTitle,
            monthYear = monthYear,
            redeemedAt = System.currentTimeMillis()
        )

        voucherDao.insertRedemption(redemption)
        firebaseService.syncVoucherRedemptionToCloud(redemption)

        // Also record a log entry in scan history for tracking!
        val scanLog = ScanLog(
            studentId = student.studentId,
            studentName = student.fullName,
            department = student.department,
            scannedSemester = student.activeSemester,
            status = "VALID",
            scannedAt = System.currentTimeMillis(),
            notes = "Voucher Redeemed: ${voucher.businessName} - ${voucher.discountTitle}"
        )
        scanLogDao.insertLog(scanLog)
        firebaseService.syncScanLogToCloud(scanLog)

        return true
    }

    suspend fun addOrUpdateBusiness(business: PartnerBusiness) {
        businessDao.insertBusiness(business)
        firebaseService.syncBusinessToCloud(business)
    }

    suspend fun deleteBusiness(businessId: String) {
        businessDao.deleteBusinessById(businessId)
        firebaseService.deleteBusinessFromCloud(businessId)
    }

    suspend fun addOrUpdateVoucher(voucher: PartnerVoucher) {
        voucherDao.insertVoucher(voucher)
        firebaseService.syncVoucherToCloud(voucher)
    }

    suspend fun deleteVoucher(voucherId: String) {
        voucherDao.deleteVoucherById(voucherId)
        firebaseService.deleteVoucherFromCloud(voucherId)
    }

    private fun parseStudentId(qrPayload: String): String {
        val trimmed = qrPayload.trim()
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            return try {
                val json = JSONObject(trimmed)
                json.optString("studentId", json.optString("id", trimmed))
            } catch (e: Exception) {
                trimmed
            }
        }
        return trimmed
    }
}
