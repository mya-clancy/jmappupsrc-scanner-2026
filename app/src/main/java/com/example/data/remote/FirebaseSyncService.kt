package com.example.data.remote

import android.util.Log
import com.example.data.model.PartnerBusiness
import com.example.data.model.PartnerVoucher
import com.example.data.model.ScanLog
import com.example.data.model.Student
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseSyncService {

    private var firestore: FirebaseFirestore? = null

    init {
        try {
            firestore = FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w("FirebaseSyncService", "Firebase Firestore not initialized: ${e.message}")
        }
    }

    val isFirebaseAvailable: Boolean
        get() = firestore != null

    suspend fun syncStudentToCloud(student: Student): Boolean {
        val db = firestore ?: return false
        return try {
            val map = mapOf(
                "studentId" to student.studentId,
                "fullName" to student.fullName,
                "email" to student.email,
                "department" to student.department,
                "activeSemester" to student.activeSemester,
                "isMembershipPaid" to student.isMembershipPaid,
                "joinYear" to student.joinYear,
                "notes" to student.notes,
                "lastScannedAt" to student.lastScannedAt,
                "updatedAt" to student.updatedAt
            )
            db.collection("students").document(student.studentId).set(map).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error uploading student: ${e.message}")
            false
        }
    }

    suspend fun syncScanLogToCloud(log: ScanLog): Boolean {
        val db = firestore ?: return false
        return try {
            val map = mapOf(
                "studentId" to log.studentId,
                "studentName" to log.studentName,
                "department" to log.department,
                "scannedSemester" to log.scannedSemester,
                "status" to log.status,
                "scannedAt" to log.scannedAt,
                "scannedBy" to log.scannedBy,
                "notes" to log.notes
            )
            db.collection("scan_logs").add(map).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error syncing log: ${e.message}")
            false
        }
    }

    suspend fun syncVoucherRedemptionToCloud(redemption: com.example.data.model.VoucherRedemption): Boolean {
        val db = firestore ?: return false
        return try {
            val map = mapOf(
                "studentId" to redemption.studentId,
                "studentName" to redemption.studentName,
                "businessId" to redemption.businessId,
                "businessName" to redemption.businessName,
                "discountTitle" to redemption.discountTitle,
                "monthYear" to redemption.monthYear,
                "redeemedAt" to redemption.redeemedAt
            )
            db.collection("voucher_redemptions").add(map).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error syncing voucher: ${e.message}")
            false
        }
    }

    suspend fun syncBusinessToCloud(business: PartnerBusiness): Boolean {
        val db = firestore ?: return false
        return try {
            val map = mapOf(
                "id" to business.id,
                "name" to business.name,
                "category" to business.category,
                "pin" to business.pin
            )
            db.collection("businesses").document(business.id).set(map).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error syncing business: ${e.message}")
            false
        }
    }

    suspend fun deleteBusinessFromCloud(businessId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("businesses").document(businessId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error deleting business: ${e.message}")
            false
        }
    }

    suspend fun syncVoucherToCloud(voucher: PartnerVoucher): Boolean {
        val db = firestore ?: return false
        return try {
            val map = mapOf(
                "id" to voucher.id,
                "businessId" to voucher.businessId,
                "businessName" to voucher.businessName,
                "discountTitle" to voucher.discountTitle,
                "description" to voucher.description,
                "category" to voucher.category
            )
            db.collection("vouchers").document(voucher.id).set(map).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error syncing voucher: ${e.message}")
            false
        }
    }

    suspend fun deleteVoucherFromCloud(voucherId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("vouchers").document(voucherId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error deleting voucher: ${e.message}")
            false
        }
    }

    suspend fun deleteStudentFromCloud(studentId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("students").document(studentId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseSyncService", "Error deleting student: ${e.message}")
            false
        }
    }

    fun observeRealtimeStudents(): Flow<List<Student>> = callbackFlow {
        val db = firestore
        if (db == null) {
            close()
            return@callbackFlow
        }

        val listener = db.collection("students")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirebaseSyncService", "Realtime listener error: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val students = snapshot.documents.mapNotNull { doc ->
                        try {
                            Student(
                                studentId = doc.getString("studentId") ?: doc.id,
                                fullName = doc.getString("fullName") ?: "",
                                email = doc.getString("email") ?: "",
                                department = doc.getString("department") ?: "",
                                activeSemester = doc.getString("activeSemester") ?: "Fall 2026",
                                isMembershipPaid = doc.getBoolean("isMembershipPaid") ?: false,
                                joinYear = doc.getString("joinYear") ?: "2024",
                                notes = doc.getString("notes") ?: "",
                                lastScannedAt = doc.getLong("lastScannedAt"),
                                updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(students)
                }
            }

        awaitClose { listener.remove() }
    }
}
