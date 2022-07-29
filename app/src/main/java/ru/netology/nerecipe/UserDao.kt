package ru.netology.nerecipe

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface UserDao {
    @Query("SELECT * FROM UserEntity ORDER BY uid DESC")
    fun getAll(): LiveData<List<UserEntity>>

    @Insert
    fun insert(userEntity: UserEntity)

    @Query(
        """
            UPDATE UserEntity SET
            favorites= :favorites
            WHERE uid= :uid;
           """
    )
    fun updateFavorites(uid: Int, favorites: List<Int>)

    @Query(
        """
            SELECT favorites FROM UserEntity
            WHERE uid= :uid;
           """
    )
    fun getFavorites(uid:Int): List<Int>

    fun likeDislike(uid: Int, recipeId: Int): Boolean
    {
        val favorites = getFavorites(uid)
        return if (favorites.contains(recipeId)){
            updateFavorites(uid,favorites.filterNot { it==recipeId })
            false
        } else{
            updateFavorites(uid, favorites.plus(recipeId))
            true
        }
    }

    @Query("DELETE FROM UserEntity WHERE uid= :uid")
    fun removeById(uid: Int)


    fun save(user: UserEntity) =
        if (user.uid == 0) insert(user) else user.favorites?.let { updateFavorites(user.uid, it) }

    @Query("SELECT * FROM UserEntity WHERE uid= :uid")
    fun showUser(uid: Int): User
}