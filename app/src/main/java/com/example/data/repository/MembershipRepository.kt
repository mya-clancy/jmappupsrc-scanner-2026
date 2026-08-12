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
        // No initial data seeding. The database starts completely empty.
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
