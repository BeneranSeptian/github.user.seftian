package app.seftian.githubuser.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
class UserEntity(

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String? = null,

    @field:ColumnInfo(name = "company")
    val company: String? = null,

    @field:ColumnInfo(name = "followers")
    val followers: Int? = 0,

    @field:ColumnInfo(name = "followersUrl")
    val followersUrl: String? = null,

    @field:ColumnInfo(name = "following")
    val following: Int? = 0,

    @field:ColumnInfo(name = "followingUrl")
    val followingUrl: String? = null,

    @field:ColumnInfo(name = "id")
    val id: Int? = 0,

    @field:ColumnInfo(name = "location")
    val location: String? = null,


    @field:ColumnInfo(name = "login")
    @field:PrimaryKey
    val login: String,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "publicRepos")
    val publicRepos: Int? = 0,

    @field:ColumnInfo(name = "favorite")
    var favorite: Boolean = false
) : Parcelable