package com.jspersonal.projectcha

import android.content.Context
import androidx.room.*

@Database(entities = [Clients::class, AcptInfos::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun queryDao(): QueryDao

    companion object{
        private var instance: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase?{
            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                    AppDataBase::class.java,
                    "database-projectCha").build()

            }
            return instance
        }
    }
}