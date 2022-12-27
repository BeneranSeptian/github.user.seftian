package app.seftian.githubuser.ui.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.seftian.githubuser.R
import app.seftian.githubuser.data.remote.model.User
import app.seftian.githubuser.databinding.ActivityMainBinding
import app.seftian.githubuser.ui.view.adapter.UserAdapter
import app.seftian.githubuser.ui.viewmodel.MainViewModel
import app.seftian.githubuser.ui.viewmodel.ViewModelFactory
import app.seftian.githubuser.utils.ConnectivityObserver
import app.seftian.githubuser.utils.SettingPreferences
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.splashLoading.value
            }
        }
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = SettingPreferences.getInstance(dataStore)
        mainViewModel =
            ViewModelProvider(this, ViewModelFactory(pref, application))[MainViewModel::class.java]



        lifecycleScope.launchWhenStarted {
            mainViewModel.status.collect { status ->
                setupRecyclerView(status)
                binding.apply {
                    when (status) {
                        ConnectivityObserver.Status.Available -> {
                            tvStatusKoneksi.text =
                                "Koneksi internet tersedia, silahkan cari user yang anda inginkan"
                            btnCari.isEnabled = true
                            etCari.isEnabled = true
                        }
                        ConnectivityObserver.Status.Unavailable -> {
                            tvStatusKoneksi.text =
                                "Koneksi internet tidak tersedia, fitur pencarian dimatikan"
                            btnCari.isEnabled = false
                            etCari.isEnabled = false
                        }
                        ConnectivityObserver.Status.Lost -> {
                            binding.tvStatusKoneksi.text =
                                "Koneksi internet hilang, fitur pencarian dimatikan"
                            btnCari.isEnabled = false
                            etCari.isEnabled = false
                        }
                        ConnectivityObserver.Status.Losing -> {
                            binding.tvStatusKoneksi.text = "Anda mulai kehilangan koneksi internet"
                            btnCari.isEnabled = false
                            etCari.isEnabled = false
                        }
                    }
                }

            }
        }


        binding.apply {
            btnCari.setOnClickListener {
                mainViewModel.getUsers(etCari.text.toString())
                etCari.clearFocus()
            }


            etCari.setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    etCari.clearFocus()
                    val inputMethodManager =
                        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    mainViewModel.getUsers(etCari.text.toString())
                    true
                } else {
                    false
                }

            }
        }

        mainViewModel.users.observe(this) { users ->

            if (users.items.isEmpty()) {
                Toast.makeText(this, "data tidak ditemukan", Toast.LENGTH_LONG).show()
                return@observe
            }

            userAdapter.listUser = users.items
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressbar.isVisible = isLoading
        }

        mainViewModel.responseCode.observe(this) { responseCode ->
            if (responseCode == 401) {
                Toast.makeText(this, "token modar", Toast.LENGTH_LONG).show()
            }
        }



        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->

            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val switch = menu.findItem(R.id.setting).actionView as SwitchMaterial

        switch.isChecked = AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)


        val switchMode = menu.findItem(R.id.setting).actionView as SwitchMaterial


        switchMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }


    private fun setupRecyclerView(status: ConnectivityObserver.Status) = binding.rvUsers.apply {
        userAdapter = UserAdapter()
        adapter = userAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)

        userAdapter.setOnItemClickCallBack(object : UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(users: User) {
                if (status == ConnectivityObserver.Status.Available) {
                    val toDetail = Intent(this@MainActivity, DetailActivity::class.java)
                    toDetail.putExtra("username", users.login)
                    startActivity(toDetail)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Periksa koneksi internet anda",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}



