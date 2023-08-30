package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    /*declare an abstract function that returns the ItemDao so that the database knows about the DAO.*/
    abstract fun itemDao(): ItemDao

    companion object {
        /*The value of a volatile variable is never cached, and all reads and writes are to and from the main memory.
         These features help ensure the value of Instance is always up to date and is the same for all execution threads.
        It means that changes made by one thread to Instance are immediately visible to all other threads.*/
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            /*Multiple threads can potentially ask for a database instance at the same time, which results in two databases
             instead of one. This issue is known as a race condition. Wrapping the code to get the database inside a
             synchronized block means that only one thread of execution at a time can enter this block of code,
             which makes sure the database only gets initialized once.*/
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it   /*to keep a reference to the recently created db instance.*/
                    }
            }
        }
    }
}
