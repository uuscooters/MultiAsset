package com.biceps_studio.task_layout.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.biceps_studio.task_layout.R
import com.biceps_studio.task_layout.data.PostModel
import com.biceps_studio.task_layout.utils.Database
import com.biceps_studio.task_layout.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_post.*

class DetailActivity : AppCompatActivity() {

    private lateinit var database: Database
    private lateinit var postModel: PostModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        initData()
        initEvent()
    }

    private fun initEvent() {
        btnUpdate.setOnClickListener {
            postModel.title = etTitle.text.toString()

            database.updatePost(postModel)
        }
    }

    private fun initData() {
        val id: Int = intent.getIntExtra(Utils.ID_POST, -1)

        database = Database(this)

        if (id != -1){
            postModel = database.getPost(id)
        }

        etTitle.setText(postModel.title)
        tvContent.text = postModel.body
    }
}