package info.fortheease.seendial

import android.app.Application
import kotlinx.coroutines.flow.Flow

class UserRepository(application: Application) {
    private val userDAO: UserDAO
    val allUsers: Flow<List<User>>

    init {
        val userDatabase = UserDatabase.getInstance(application)
        userDAO = userDatabase.userDAO()
        allUsers = userDAO.getAllUsers()
    }

    suspend fun insert(user: User) {
        userDAO.insert(user)
    }

    suspend fun update(user: User) {
        userDAO.update(user)
    }

    suspend fun delete(user: User) {
        userDAO.delete(user)
    }
}
