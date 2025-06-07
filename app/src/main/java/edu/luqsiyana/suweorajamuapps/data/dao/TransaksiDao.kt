package edu.luqsiyana.suweorajamuapps.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import edu.luqsiyana.suweorajamuapps.data.model.Transaksi
import edu.luqsiyana.suweorajamuapps.data.model.TransaksiBarang

@Dao
interface TransaksiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: Transaksi)

    @Update
    suspend fun updateTransaksi(transaksi: Transaksi)

    @Delete
    suspend fun deleteTransaksi(transaksi: Transaksi)

    @Transaction
    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    suspend fun getAllTransaksiWithBarang(): List<TransaksiBarang>

}