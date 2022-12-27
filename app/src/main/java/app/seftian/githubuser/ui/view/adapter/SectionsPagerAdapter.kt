package app.seftian.githubuser.ui.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.seftian.githubuser.ui.view.fragment.FollowersFragment
import app.seftian.githubuser.ui.view.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, val data:String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowersFragment().followersFragmentNewInstance(data)
            }
            1 -> fragment = FollowingFragment().followingFragmentNewInstance(data)
        }
        return fragment as Fragment
    }

}