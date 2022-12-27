package app.seftian.githubuser.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.seftian.githubuser.R
import app.seftian.githubuser.databinding.ItemUserBinding
import app.seftian.githubuser.data.remote.model.FollowersOrFollowingItem
import com.bumptech.glide.Glide

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<FollowersOrFollowingItem>(){
        override fun areItemsTheSame(
            oldItem: FollowersOrFollowingItem,
            newItem: FollowersOrFollowingItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FollowersOrFollowingItem,
            newItem: FollowersOrFollowingItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =AsyncListDiffer(this, diffCallback)
    var listFollowing : List<FollowersOrFollowingItem>
    get() = differ.currentList
    set(value) {differ.submitList(value)}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val following = listFollowing[position]
            Glide.with(holder.itemView.context)
                .load(following.avatarUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(ivProfilePicKecil)
            tvUsername.text = following.login
        }
    }

    override fun getItemCount(): Int {
        return listFollowing.size
    }
    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}