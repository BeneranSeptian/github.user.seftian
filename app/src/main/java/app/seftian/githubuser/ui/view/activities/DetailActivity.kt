package app.seftian.githubuser.ui.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import app.seftian.githubuser.R
import app.seftian.githubuser.databinding.ActivityDetailBinding
import app.seftian.githubuser.ui.view.adapter.SectionsPagerAdapter
import app.seftian.githubuser.ui.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var jmlFollowers: Int? = 0

    private val viewModel: DetailViewModel by viewModels()


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val username = intent.getStringExtra("username")

        viewModel.getUserDetail(username.toString())

        supportActionBar?.hide()
        viewModel.isLoading.observe(this) {
            binding.progressbar.isVisible = it
        }

        viewModel.userDetail.observe(this) { userDetail ->
            binding.apply {
                tvNameDetail.text = userDetail.name
                tvLocation.text = userDetail.location
                tvUsernameDetail.text = userDetail.login
                tvPerusahaan.text = userDetail.company
                tvRepo.text = userDetail.publicRepos.toString()
                jmlFollowers = userDetail.followers
                Glide.with(this@DetailActivity)
                    .load(userDetail.avatarUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(ivProfilePicDetail)
            }
            val sectionsPagerAdapter = SectionsPagerAdapter(this, username.toString())
            val viewPager: ViewPager2 = binding.viewPager
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = binding.tabLayout
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                val jmlFollow = if (position == 0) {
                    userDetail.followers
                } else {
                    userDetail.following
                }
                tab.text = resources.getString(TAB_TITLES[position], jmlFollow)
            }.attach()

            viewModel.getLocalUSer(username.toString()).observe(this) { user ->
                if (user != null) {
                    if (user.favorite) {
                        binding.fabFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_is_favorite
                            )
                        )
                    } else {
                        binding.fabFavorite.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_not_favorite
                            )
                        )
                    }
                    binding.fabFavorite.setOnClickListener {
                        user.favorite = !user.favorite
                        viewModel.updateFavorite(user)
                    }
                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.deleteNotFavorite()
    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }


}