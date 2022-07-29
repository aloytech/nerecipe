package ru.netology.nerecipe

import androidx.lifecycle.Transformations

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository{

    override fun getAll() = Transformations.map(userDao.getAll()) { list ->
        list.map {
            User(
                it.uid,
                it.userName,
                it.favorites,
                it.hash
            )
        }
    }

    override fun likeDislike(uid: Int, recipeId: Int): Boolean {
        return userDao.likeDislike(uid,recipeId)
    }


    override fun removeById(uid: Int) {
        userDao.removeById(uid)
    }

    override fun save(user: User) {
        userDao.save(UserEntity.fromDto(user))
    }

    override fun showUser(uid: Int): User {
        return userDao.showUser(uid)
    }

    override fun getFavorites(uid: Int): List<Int> {
        return userDao.getFavorites(uid)
    }

}