package com.example.finaldb.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.finaldb.dao.ModesDao
import com.example.finaldb.dao.ReadByDeviceDao
import com.example.finaldb.dao.ResultDao
import com.example.finaldb.dao.UserDao
import com.example.finaldb.entities.Modes
import com.example.finaldb.entities.ReadByDevice
import com.example.finaldb.entities.UserInfo
import com.example.finaldb.entities.UserResult
import com.example.finaldb.entities.relations.ModesAndResultsRef
import com.example.finaldb.entities.relations.ReadByDeviceWithResultRef
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [
        UserInfo::class, UserResult::class,
        ReadByDevice::class,Modes::class,
        ModesAndResultsRef::class,ReadByDeviceWithResultRef::class],
    version = 1,
    exportSchema = false)

abstract class AppDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getResultDao(): ResultDao

    abstract fun getModesDao(): ModesDao

    abstract fun getReadyByDeviceDao(): ReadByDeviceDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? =null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}