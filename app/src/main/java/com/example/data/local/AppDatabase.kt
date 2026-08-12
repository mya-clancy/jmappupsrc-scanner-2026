package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.PartnerBusiness
import com.example.data.model.PartnerVoucher
import com.example.data.model.ScanLog
import com.example.data.model.Student
import com.example.data.model.VoucherRedemption

@Database(
    entities = [Student::class, ScanLog::class, VoucherRedemption::class, PartnerBusiness::class, PartnerVoucher::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun scanLogDao(): ScanLogDao
    abstract fun voucherDao(): VoucherDao
    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "org_membership_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
