package edu.luqsiyana.suweorajamuapps.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "barang")
@Parcelize
data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val harga: Double,
    val imageUri: String
): Parcelable
