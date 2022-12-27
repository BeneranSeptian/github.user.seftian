package app.seftian.githubuser.data.remote.retrofit



import app.seftian.githubuser.BuildConfig
import app.seftian.githubuser.data.remote.model.FollowersOrFollowing
import app.seftian.githubuser.data.remote.model.SearchResponse
import app.seftian.githubuser.data.remote.model.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token "+ BuildConfig.API_KEY)
    suspend fun getUsers(@Query("q") username : String) : Response<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token "+ BuildConfig.API_KEY)
    suspend fun getSingleUser(@Path("username") username: String) : Response<UserDetail>

    @GET("users/{username}/followers")
    @Headers("Authorization: token "+ BuildConfig.API_KEY)
    suspend fun getFollowers(@Path("username") username: String) : Response<FollowersOrFollowing>

    @GET("users/{username}/following")
    @Headers("Authorization: token "+ BuildConfig.API_KEY)
    suspend fun getFollowing(@Path("username") username: String) : Response<FollowersOrFollowing>
}