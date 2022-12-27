package app.seftian.githubuser.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.seftian.githubuser.R
import app.seftian.githubuser.databinding.ItemUserBinding
import app.seftian.githubuser.data.remote.model.User
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallBack (onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }


    private val diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var listUser: List<User>
        get() = differ.currentList
        set(value)  { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.binding.apply {
                Glide.with(holder.itemView.context)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(ivProfilePicKecil)
            tvUsername.text = user.login
        }

        holder.itemView.setOnClickListener{
            onItemClickCallBack.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    class ViewHolder( val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallBack{
        fun onItemClicked(users: User)
    }
}
