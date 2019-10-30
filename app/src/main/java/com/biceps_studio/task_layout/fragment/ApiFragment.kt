package com.biceps_studio.task_layout.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.biceps_studio.task_layout.BuildConfig
import com.biceps_studio.task_layout.R
import com.biceps_studio.task_layout.activity.DetailPostActivity
import com.biceps_studio.task_layout.adapter.PostAdapter
import com.biceps_studio.task_layout.api.API
import com.biceps_studio.task_layout.data.PostModel
import com.biceps_studio.task_layout.utils.Utils
import kotlinx.android.synthetic.main.fragment_api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiFragment : Fragment() {

    var postAdapter = PostAdapter()
    var arrayList: ArrayList<PostModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initEvent()
    }

    private fun initEvent() {
        srlMain.setOnRefreshListener {
            checkConnection()
        }

        btnAdd.setOnClickListener {
            addPost()
        }

        postAdapter.setOnDeletePost(object: PostAdapter.OnListener {
            override fun onClickPost(int: Int) {
                startActivity(Intent(activity!!, DetailPostActivity::class.java).putExtra(Utils.ID_POST, int))
            }

            override fun onDeletePost(int: Int) {
                if (Utils.isNetworkConected(activity!!)){
                    srlMain.isRefreshing = true

                    API.deletePost(arrayList[int].id, object: Callback<Response<Void>>{
                        override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                            srlMain.isRefreshing = false //Menghilangkan Loading

                            if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                                Utils.showToast(activity!!, t.message!!)
                            }
                        }

                        override fun onResponse(call: Call<Response<Void>>, response: Response<Response<Void>>) {
                            srlMain.isRefreshing = false //Menghilangkan Loading

                            if (response.isSuccessful){
                                Utils.showToast(activity!!, "Postingan dengan id ${arrayList[int].id} berhasil dihapus")

                                arrayList.removeAt(int) //Menghapus item dari arraylist berdasarakn indexnya

                                postAdapter.notifyItemRemoved(int) //Memberikan aksi untuk menghilangkan item dari recyclerview
                                postAdapter.updateData(arrayList) //Update data yg terbaru setelah ada data yg dihapus
                            } else {
                                Utils.showToast(activity!!, response.message())
                            }
                        }
                    })
                } else {
                    Utils.errorNoConnection(activity!!)
                }
            }
        })
    }

    private fun addPost() {
        val hashMap: HashMap<String, String> = HashMap()

        hashMap["title"] = "POSTINGAN TERBARU"
        hashMap["body"] = "Ini adalah konten dari postingan terbaru"
        hashMap["userId"] = "1"

        if (Utils.isNetworkConected(activity!!)){
            srlMain.isRefreshing = true

            API.addPost(hashMap, object: Callback<PostModel>{
                override fun onFailure(call: Call<PostModel>, t: Throwable) {
                    srlMain.isRefreshing = false //Menghilangkan Loading

                    if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                        Utils.showToast(activity!!, t.message!!)
                    }
                }

                override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                    srlMain.isRefreshing = false //Menghilangkan Loading

                    if (response.isSuccessful){ //Kondisi ketika respon berhasil
                        arrayList.add(response.body()!!) //Menambahkan postmodel yg kita kirim ke server secara manual

                        postAdapter.updateData(arrayList) //Mengubah data pada adapter sehingga data pada recyclerview berubah

                        val lastItem: Int = arrayList.size - 1 //Mendapatkan index terakhir pada variable arraylist

                        rvArrayList.smoothScrollToPosition(lastItem) //Recyclerview otomatis scroll ke posisi data yg baru kita tambahkan
                    } else {
                        if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                            Utils.showToast(activity!!, response.message()) //Menampilkan pesan error yg diberikan oleh server
                        }
                    }
                }
            })
        } else {
            Utils.errorNoConnection(activity!!) //Menampilkan pesan error tidak terhubung ke internet
        }
    }

    override fun onResume() {
        super.onResume()

        checkConnection()
    }

    private fun checkConnection() {
        //Memperlihatkan loading
        srlMain.isRefreshing = true

        if (Utils.isNetworkConected(activity!!)){ //Kondisi ketika terhubung ke Internet
            API.getAllPost(object: Callback<ArrayList<PostModel>> {
                override fun onFailure(call: Call<ArrayList<PostModel>>, t: Throwable) { //Function ketika client gagal mendapatkan response dari server
                    srlMain.isRefreshing = false //Menghilangkan Loading

                    if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                        Utils.showToast(activity!!, t.message!!)
                    }
                }

                override fun onResponse(call: Call<ArrayList<PostModel>>, response: Response<ArrayList<PostModel>>) { //Function ketika client berhasil mendapatkan response
                    srlMain.isRefreshing = false //Menghilangkan Loading

                    if (response.isSuccessful){ //Kondisi ketika response berhasil
                        arrayList = response.body()!!

                        postAdapter.updateData(arrayList) //Update data dari response API
                    } else {
                        if (BuildConfig.DEBUG){ //Kondisi dalam keadaan mode DEBUG
                            Utils.showToast(activity!!, response.message())
                        }
                    }
                }
            })
        } else { //Kondisi ketika tidak terhubung ke Internet
            srlMain.isRefreshing = false //Menghilangkan Loading

            Utils.errorNoConnection(activity!!) //Menampilkan Toast dengan pesan tidak terhubung ke Internet
        }
    }

    private fun initRecyclerView() {
        //Membuat layoutmanager secara vertical
        val linearLayoutManager = LinearLayoutManager(activity!!)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        //Setting atribute rvArrayList
        rvArrayList.apply {
            setHasFixedSize(true)
            adapter = postAdapter
            layoutManager = linearLayoutManager
        }
    }
}
