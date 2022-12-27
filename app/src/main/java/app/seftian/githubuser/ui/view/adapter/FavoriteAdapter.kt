package app.seftian.githubuser.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.seftian.githubuser.R
import app.seftian.githubuser.databinding.ItemUserBinding
import app.seftian.githubuser.data.local.UserEntity
import com.bumptech.glide.Glide

class FavoriteAdapter :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    fun setOnItemClickCallBack (onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    private val diffCallback = object : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(
            oldItem: UserEntity,
            newItem: UserEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: UserEntity,
            newItem: UserEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var listUser: List<UserEntity>
        get() = differ.currentList
        set(value)  { differ.submitList(value) }

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
        val user = listUser[holder.adapterPosition]

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(user.avatarUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(ivProfilePicKecil)
            tvUsername.text = user.login
            icRemove.visibility = View.VISIBLE

            icRemove.setOnClickListener{
                onItemClickCallBack.deleteItem(user)
                notifyItemChanged(holder.adapterPosition)
            }
        }

        holder.itemView.setOnClickListener{
            onItemClickCallBack.onItemClicked(user)
        }

    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    class ViewHolder( val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallBack{
        fun deleteItem(user: UserEntity)
        fun onItemClicked(users: UserEntity)
    }


}
