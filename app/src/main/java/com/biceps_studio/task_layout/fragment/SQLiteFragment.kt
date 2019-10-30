package com.biceps_studio.task_layout.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.biceps_studio.task_layout.R
import com.biceps_studio.task_layout.activity.DetailActivity
import com.biceps_studio.task_layout.adapter.PostAdapter
import com.biceps_studio.task_layout.data.PostModel
import com.biceps_studio.task_layout.utils.Database
import com.biceps_studio.task_layout.utils.Utils
import kotlinx.android.synthetic.main.fragment_api.*

class SQLiteFragment : Fragment() {

    private lateinit var database: Database

    private val postAdapter = PostAdapter()
    private var arrayList: ArrayList<PostModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        database = Database(activity!!)

        initRecyclerView()
        initEvent()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity!!)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        rvArrayList.apply {
            setHasFixedSize(true)
            adapter = postAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun initEvent() {
        btnAdd.visibility = View.GONE

        postAdapter.setOnDeletePost(object: PostAdapter.OnListener {
            override fun onDeletePost(int: Int) {
                Log.e("TAG",  "not implemented")
            }

            override fun onClickPost(int: Int) {
                startActivity(Intent(activity!!, DetailActivity::class.java).putExtra(Utils.ID_POST, int))
            }
        })

        srlMain.setOnRefreshListener { getData() }
    }

    private fun getData() {
        arrayList = database.getAllPost()

        postAdapter.updateData(arrayList)

        srlMain.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()

        getData()
    }
}
