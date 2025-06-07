package edu.luqsiyana.suweorajamuapps.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.luqsiyana.suweorajamuapps.data.dao.BarangDao
import edu.luqsiyana.suweorajamuapps.data.dao.TransaksiDao
import edu.luqsiyana.suweorajamuapps.data.db.AppDatabase
import edu.luqsiyana.suweorajamuapps.repository.SuweOraJamuRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "suweorajamu_db"
        ).build()
    }

    @Provides
    fun provideBarangDao(db: AppDatabase): BarangDao = db.barangDao()

    @Provides
    fun provideTransaksiDao(db: AppDatabase): TransaksiDao = db.transaksiDao()

    fun provideRepository(
        barangDao: BarangDao,
        transaksiDao: TransaksiDao
    ): SuweOraJamuRepository {
        return SuweOraJamuRepository(barangDao, transaksiDao)
    }
}