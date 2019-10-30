package com.biceps_studio.task_layout.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.biceps_studio.task_layout.BuildConfig
import com.biceps_studio.task_layout.R
import com.biceps_studio.task_layout.api.API
import com.biceps_studio.task_layout.data.PostModel
import com.biceps_studio.task_layout.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPostActivity : AppCompatActivity() {

    private var id: Int? = null
    private var postModel: PostModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        initData()
        initEvent()
    }

    private fun initEvent() {
        btnUpdate.setOnClickListener { updatePost(postModel!!) }
        srlDetail.setOnRefreshListener { initData() }
    }

    private fun updatePost(postModel: PostModel) {
        postModel.title = etTitle.text.toString()

        if (Utils.isNetworkConected(getActivity())){
            srlDetail.isRefreshing = true

            API.updatePost(postModel, object : Callback<PostModel> {
                override fun onFailure(call: Call<PostModel>, t: Throwable) {
                    srlDetail.isRefreshing = false //Menghilangkan Loading

                    if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                        Utils.showToast(getActivity(), t.message!!)
                    }
                }

                override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                    srlDetail.isRefreshing = false //Menghilangkan Loading

                    if (response.isSuccessful){
                        Utils.showToast(getActivity(), "Postingan berhasil diubah")
                    } else {
                        Utils.showToast(getActivity(), response.message())
                    }
                }
            })
        } else {
            Utils.errorNoConnection(getActivity())
        }
    }

    private fun initData() {
        srlDetail.isRefreshing = true

        if (id == null || id == -1) {
            id = intent.getIntExtra(Utils.ID_POST, -1)
        }

        if (Utils.isNetworkConected(getActivity())){
            if (id != -1){
                API.getPost(id!!, object : Callback<PostModel> {
                    override fun onFailure(call: Call<PostModel>, t: Throwable) {
                        srlDetail.isRefreshing = false //Menghilangkan Loading

                        if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                            Utils.showToast(getActivity(), t.message!!)
                        }
                    }

                    override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                        srlDetail.isRefreshing = false //Menghilangkan Loading

                        if (response.isSuccessful) {
                            postModel = response.body()

                            etTitle.setText(postModel!!.title)
                            tvContent.text = postModel!!.body
                        } else {
                            if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                                Utils.showToast(getActivity(), response.message())
                            }
                        }
                    }
                })
            }
        } else {
            srlDetail.isRefreshing = false
        }
    }

    fun getActivity() : Context { return this }
}
