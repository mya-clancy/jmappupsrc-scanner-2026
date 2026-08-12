package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PartnerBusiness
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDao {
    @Query("SELECT * FROM partner_businesses ORDER BY name ASC")
    fun getAllBusinesses(): Flow<List<PartnerBusiness>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: PartnerBusiness)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusinesses(businesses: List<PartnerBusiness>)

    @Query("DELETE FROM partner_businesses WHERE id = :id")
    suspend fun deleteBusinessById(id: String)
}
