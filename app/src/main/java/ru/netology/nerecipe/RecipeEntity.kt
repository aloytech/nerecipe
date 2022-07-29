package ru.netology.nerecipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val name: String,
    val categoryId: Int,
    val likesCount: Int = 0,
    val servingLink: String,
    val stages: List<String>,
    val stagesLink: List<String>
) {
    fun toDto() =
        Recipe(id, authorId, name, categoryId, likesCount, servingLink,stages,stagesLink)

    companion object {
        fun fromDto(dto: Recipe) =
            RecipeEntity(
                dto.id, dto.authorId, dto.name, dto.categoryId, dto.likesCount,
                dto.servingLink, dto.stages, dto.stagesLink
            )

    }
}