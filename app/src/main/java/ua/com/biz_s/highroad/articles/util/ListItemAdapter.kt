package ua.com.biz_s.highroad.articles.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.com.biz_s.highroad.articles.domain.Article
import ua.com.biz_s.highroad.databinding.ListItemBinding

class ListItemAdapter(private val clicker: Clicker) :
    ListAdapter<Article, ListItemAdapter.ListItemViewHolder>(DiffCallback) {

    class ListItemViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //        @SuppressLint("SimpleDateFormat")
        fun bind(articleItem: Article) {
            binding.articleItem = articleItem
            //need rebinding
            binding.executePendingBindings()
        }
    }

    /*var articles: List<Article> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listArticle = getItem(position)
        holder.itemView.setOnClickListener {
            clicker.onClick(listArticle)
        }
        holder.bind(listArticle)
    }

//    override fun getItemCount() = articles.size

    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [ListArticlesItem]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [listArticlesItem]
     */
    class Clicker(val event: (articleItem: Article) -> Unit) {
        fun onClick(articleItem: Article) = event(articleItem)
    }

}