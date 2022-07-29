package ru.netology.nerecipe

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Insert
    fun insert(recipeEntity: RecipeEntity)

    @Query(
        """
            UPDATE RecipeEntity SET
            stages= :stages, stagesLink= :stagesLink
            WHERE id= :id;
           """
    )
    fun updateStagesById(id: Int, stages: List<String>, stagesLink: List<String>)

    @Query(
        """
            UPDATE RecipeEntity SET
            likesCount = likesCount + CASE WHEN :likedByMe THEN -1 ELSE 1 END
            WHERE id = :id;
            """
    )
    fun likeDislike(id: Int, likedByMe: Boolean)

    @Query("DELETE FROM RecipeEntity WHERE id= :id")
    fun removeById(id: Int)


    fun save(recipe: RecipeEntity) =
        if (recipe.id == 0) insert(recipe) else updateStagesById(recipe.id, recipe.stages, recipe.stagesLink)

    @Query("SELECT * FROM RecipeEntity WHERE id= :id")
    fun showRecipe(id: Int): Recipe
}