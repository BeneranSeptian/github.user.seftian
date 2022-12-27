package app.seftian.githubuser.data.remote.model


import com.google.gson.annotations.SerializedName

data class FollowersOrFollowingItem(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("events_url")
    val eventsUrl: String,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following_url")
    val followingUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String
)