package ru.netology.nerecipe

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class, UserEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDb? = null
        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app_room_recipe.db")
                .allowMainThreadQueries()
                .build()
    }
}