package app.seftian.githubuser.ui.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.seftian.githubuser.ui.view.adapter.FollowingAdapter
import app.seftian.githubuser.databinding.FragmentFollowingBinding
import app.seftian.githubuser.ui.viewmodel.DetailViewModel


class FollowingFragment : Fragment() {

    private lateinit var binding : FragmentFollowingBinding
    private var followingAdapter = FollowingAdapter()
    private lateinit var viewModel : DetailViewModel
    private  var dataLogin =""


    fun followingFragmentNewInstance(dataLogin : String): FollowingFragment{
        val args = Bundle().apply {
            putString("DATA_LOGIN", dataLogin)
        }
        this.dataLogin = dataLogin
        val fragment = FollowingFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("DATA_LOGIN")?.apply {
            dataLogin = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        setupRecyclerView()

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getFollowing(dataLogin)

        viewModel.listFollowing.observe(viewLifecycleOwner){
            followingAdapter.listFollowing = it
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressbar.isVisible = it
        }

        return binding.root
    }
    fun setupRecyclerView() = binding.rvFollowing.apply{
        followingAdapter = FollowingAdapter()
        adapter = followingAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }



}




