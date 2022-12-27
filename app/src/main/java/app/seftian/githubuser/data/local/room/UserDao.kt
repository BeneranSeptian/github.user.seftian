package app.seftian.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import app.seftian.githubuser.data.local.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE login = :search")
    fun getUser(search:String):LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: UserEntity)

    @Query("select * from users where favorite = 1")
    fun getFavoriteUsers(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToFavoriteUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE favorite = 0")
    fun delete()


}
