package com.example.sneakerspot.ui.theme

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sneakerspot.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Trainer::class], version = 1, exportSchema = false)
abstract class TrainerDatabase : RoomDatabase() {
    abstract fun trainerDao(): TrainerDao

    private class TrainerDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.trainerDao())
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TrainerDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TrainerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrainerDatabase::class.java,
                    "trainer_database"
                )
                    .addCallback(TrainerDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDatabase(trainerDao: TrainerDao) {
            // Delete all content here.
            trainerDao.deleteAll()

            // Add sample trainers.
            val trainer1 = Trainer(name = "New Balance 2002r Navy", imageRes = R.drawable.new_balance_2002r_navy, price = "£110")
            val trainer2 = Trainer(name = "Jordan 4 Bred", imageRes = R.drawable.jordan_4_bred, price = "£250")
            val trainer3 = Trainer(name = "Jordan 4 Black Cat", imageRes = R.drawable.jordan_4_black_cat, price = "£210")
            trainerDao.insertTrainer(trainer1)
            trainerDao.insertTrainer(trainer2)
            trainerDao.insertTrainer(trainer3)
        }
    }
}
