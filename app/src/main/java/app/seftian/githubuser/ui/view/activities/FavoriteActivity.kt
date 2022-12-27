package app.seftian.githubuser.ui.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.seftian.githubuser.data.local.UserEntity
import app.seftian.githubuser.databinding.ActivityFavoriteBinding
import app.seftian.githubuser.ui.view.adapter.FavoriteAdapter
import app.seftian.githubuser.ui.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        supportActionBar?.title = "User Favorit"

        viewModel.getFavoriteUser().observe(this) { users ->
            favoriteAdapter.listUser = users
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteUser().observe(this) { users ->
            favoriteAdapter.listUser = users
        }
    }

    private fun setupRecyclerView() = binding.rvUsers.apply {
        favoriteAdapter = FavoriteAdapter()
        adapter = favoriteAdapter
        layoutManager = LinearLayoutManager(this@FavoriteActivity)

        favoriteAdapter.setOnItemClickCallBack(object : FavoriteAdapter.OnItemClickCallBack {
            override fun deleteItem(user: UserEntity) {
                user.favorite = !user.favorite
                viewModel.updateFavorite(user)
            }

            override fun onItemClicked(users: UserEntity) {
                val toDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                toDetail.putExtra("username", users.login)
                toDetail.putExtra("userdetail", users)
                startActivity(toDetail)
            }
        })
    }


}