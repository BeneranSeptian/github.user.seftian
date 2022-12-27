package app.seftian.githubuser.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import app.seftian.githubuser.utils.SettingPreferences
import app.seftian.githubuser.data.remote.model.SearchResponse
import app.seftian.githubuser.data.remote.retrofit.ApiConfig
import app.seftian.githubuser.utils.ConnectivityObserver
import app.seftian.githubuser.utils.ConnectivityObserver.Status.*
import app.seftian.githubuser.utils.NetworkConnectivityObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(private val pref: SettingPreferences, application: Application) :
    AndroidViewModel(application) {

    private val _users = MutableLiveData<SearchResponse>()
    val users: LiveData<SearchResponse> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _status = MutableStateFlow(Unavailable)
    val status= _status.asStateFlow()

    private val _responseCode = MutableLiveData<Int>()
    val responseCode: LiveData<Int> = _responseCode

    private val _splashLoading = MutableStateFlow(true)
    val splashLoading = _splashLoading.asStateFlow()

    init {
        viewModelScope.launch {

            delay(3000)
            _splashLoading.value = false

            val statusKoneksi: ConnectivityObserver = NetworkConnectivityObserver(getApplication())
            statusKoneksi.observe().collect {
                _status.value = it
            }
        }

    }


    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }


    fun getUsers(username: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val response =
                try {
                    ApiConfig.getApiService().getUsers(username)
                } catch (e: HttpException) {
                    Log.e("error", e.message())
                    return@launch
                } catch (e: IOException) {
                    Log.e("error", "Tidak ada koneksi internet")
                    return@launch
                }

            if (response.isSuccessful && response.body() != null) {
                _isLoading.value = false
                _users.value = response.body()!!
            } else {
                Log.e("error cuk",response.code().toString())
                if(response.code() == 401){
                    _responseCode.value = 401
                }
                _isLoading.value = false
            }
        }
    }
}



