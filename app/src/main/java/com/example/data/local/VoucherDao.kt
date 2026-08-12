package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PartnerVoucher
import com.example.data.model.VoucherRedemption
import kotlinx.coroutines.flow.Flow

@Dao
interface VoucherDao {
    @Query("SELECT * FROM partner_vouchers ORDER BY businessName ASC")
    fun getAllVouchers(): Flow<List<PartnerVoucher>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoucher(voucher: PartnerVoucher)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVouchers(vouchers: List<PartnerVoucher>)

    @Query("DELETE FROM partner_vouchers WHERE id = :id")
    suspend fun deleteVoucherById(id: String)

    @Query("SELECT * FROM voucher_redemptions ORDER BY redeemedAt DESC")
    fun getAllRedemptions(): Flow<List<VoucherRedemption>>

    @Query("SELECT * FROM voucher_redemptions WHERE studentId = :studentId AND monthYear = :monthYear")
    suspend fun getRedemptionsForStudentThisMonth(studentId: String, monthYear: String): List<VoucherRedemption>

    @Query("SELECT COUNT(*) FROM voucher_redemptions WHERE studentId = :studentId AND businessId = :businessId AND monthYear = :monthYear")
    suspend fun checkStudentBusinessUsageThisMonth(studentId: String, businessId: String, monthYear: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRedemption(redemption: VoucherRedemption): Long

    @Query("DELETE FROM voucher_redemptions")
    suspend fun clearAllRedemptions()
}
