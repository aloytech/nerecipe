package ru.netology.nerecipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val userName: String,
    val favorites: List<Int>? = null,
    val hash: Int
) {
    fun toDto() =
        User(uid, userName, favorites, hash)

    companion object {
        fun fromDto(dto: User) =
            UserEntity(
                dto.uid, dto.userName, dto.favorites, dto.hash
            )

    }
}