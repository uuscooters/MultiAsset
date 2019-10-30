package com.biceps_studio.task_layout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.biceps_studio.task_layout.R
import com.biceps_studio.task_layout.data.PostModel
import com.biceps_studio.task_layout.utils.Database
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var arrayList: ArrayList<PostModel> = ArrayList()

    private lateinit var onListener: OnListener

    fun setOnDeletePost(onListener: OnListener){
        this.onListener = onListener
    }

    fun updateData(arrayList: ArrayList<PostModel>){
        this.arrayList = arrayList

        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int { return arrayList.size }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postModel: PostModel = arrayList[position]

        holder.itemView.tvTitle.text = postModel.title
        holder.itemView.tvContent.text = postModel.body

        holder.itemView.btnDelete.setOnClickListener { onListener.onDeletePost(position) }
        holder.itemView.cvPost.setOnClickListener { onListener.onClickPost(postModel.id) }

        val context: Context = holder.itemView.context

        val database = Database(context)

        if (database.isExist(postModel.id)){
            holder.itemView.ivSaved.setImageDrawable(context.getDrawable(R.drawable.ic_star))
        } else {
            holder.itemView.ivSaved.setImageDrawable(context.getDrawable(R.drawable.ic_unstar))
        }

        holder.itemView.ivSaved.setOnClickListener {
            if (database.isExist(postModel.id)){
                database.deletePost(postModel.id)

                holder.itemView.ivSaved.setImageDrawable(context.getDrawable(R.drawable.ic_unstar))
            } else {
                database.insertPost(postModel)

                holder.itemView.ivSaved.setImageDrawable(context.getDrawable(R.drawable.ic_star))
            }
        }
    }

    interface OnListener {
        fun onDeletePost(int: Int)
        fun onClickPost(int: Int)
    }
}