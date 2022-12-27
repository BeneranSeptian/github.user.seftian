package app.seftian.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import app.seftian.githubuser.data.local.UserEntity
import app.seftian.githubuser.data.local.room.UserDatabase
import app.seftian.githubuser.utils.AppExecutor

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val executors = AppExecutor()
    private val database = UserDatabase.getInstance(getApplication())
    private val userDao = database.userDao()


    fun getFavoriteUser() : LiveData<List<UserEntity>>{
        return userDao.getFavoriteUsers()
    }

    fun updateFavorite(userEntity: UserEntity){
        executors.diskIO.execute{
            userDao.updateUser(userEntity)
        }
    }


}