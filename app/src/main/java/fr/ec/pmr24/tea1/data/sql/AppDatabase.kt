package fr.ec.pmr24.tea1.data.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.ec.pmr24.tea1.data.classes.ItemToDo
import fr.ec.pmr24.tea1.data.classes.ItemsSync
import fr.ec.pmr24.tea1.data.classes.ListeToDo

@Database(entities = [ListeToDo::class, ItemToDo::class, ItemsSync::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemToDoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context : Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}