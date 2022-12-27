package app.seftian.githubuser.ui.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.seftian.githubuser.databinding.FragmentFollowersBinding
import app.seftian.githubuser.ui.view.adapter.FollowersAdapter
import app.seftian.githubuser.ui.viewmodel.DetailViewModel


class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private var followersAdapter = FollowersAdapter()
    private lateinit var viewModel: DetailViewModel

    private var dataLogin =""

    fun followersFragmentNewInstance(dataLogin:String): FollowersFragment{
        val args = Bundle().apply {
            putString("DATA_LOGIN", dataLogin)
        }

        val fragment = FollowersFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("DATA_LOGIN")?.let {
            dataLogin=it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getFollowers(dataLogin)

        viewModel.listFollowers.observe(viewLifecycleOwner) {
            followersAdapter.listUser = it
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressbar.isVisible = it
        }
        return binding.root

    }

    fun setupRecyclerView() = binding.rvUsers.apply {
        followersAdapter = FollowersAdapter()
        adapter = followersAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


}


