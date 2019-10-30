package com.biceps_studio.task_layout.api

import android.util.Log
import com.biceps_studio.task_layout.data.PostModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class API {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val ONE_MINUTE = 60L

        private fun getService() : Service {
            //Membuat interceptor untuk mentracking request dan response API ke dalam Log.Error dengan TAG 'API'
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) { Log.e("API", message) }
            })

            //Menapilkan Request dan Response secara detail termasuk Request & Response body
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            //Mensetting Response secara manual, termasuk pembatasan waktu response dan juga response header
            val client: OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(ONE_MINUTE, TimeUnit.SECONDS)
                .readTimeout(ONE_MINUTE, TimeUnit.SECONDS)
                .writeTimeout(ONE_MINUTE, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        val origin: Request = chain.request()

                        val request: Request = origin.newBuilder().addHeader("Content-Type", "application/json").build()

                        return chain.proceed(request)
                    }
                }).build()

            val gson: Gson = GsonBuilder().setLenient().create()

            //Inisialisasi retrofit termasuk setting baseUrl dan juga handle JSON menjadi Data
            val retrofit: Retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(Service::class.java)
        }

        //Memanggil fungsi getAllPost yg ada di interface Sevice secara ringkas
        fun getAllPost(callback: Callback<ArrayList<PostModel>>){
            getService().getAllPost().enqueue(callback)
        }

        //Memanggil fungsi getPost yg ada di interface Sevice secara ringkas
        fun getPost(id: Int, callback: Callback<PostModel>){
            getService().getPost(id).enqueue(callback)
        }

        //Memanggil fungsi addPost yg ada di interface Sevice secara ringkas
        fun addPost(hashMap: HashMap<String, String>, callback: Callback<PostModel>){
            getService().addPost(hashMap).enqueue(callback)
        }

        //Memanggil fungsi updatePost yg ada di interface Sevice secara ringkas
        fun updatePost(postModel: PostModel, callback: Callback<PostModel>){
            getService().updatePost(postModel.id, postModel).enqueue(callback)
        }

        //Memanggil fungsi deletePost yg ada di interface Sevice secara ringkas
        fun deletePost(id: Int, callback: Callback<Response<Void>>){
            getService().deletePost(id).enqueue(callback)
        }
    }

    interface Service {

        @GET("posts")
        fun getAllPost() : Call<ArrayList<PostModel>>

        @GET("posts/{id}")
        fun getPost(@Path("id") id: Int) : Call<PostModel>

        @POST("posts")
        fun addPost(@Body hashMap: HashMap<String, String>) : Call<PostModel>

        @PUT("posts/{id}")
        fun updatePost(@Path("id") id: Int, @Body postModel: PostModel) : Call<PostModel>

        @DELETE("posts/{id}")
        fun deletePost(@Path("id") id: Int) : Call<Response<Void>>

    }
}