package com.biceps_studio.task_layout.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

@Suppress("DEPRECATION")
class Utils {

    companion object {
        const val ID_POST = "idPost"

        //Pengecekan internet
        fun isNetworkConected(context: Context) : Boolean {
            val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return try {
                cm.activeNetworkInfo!!.isConnected
            } catch (exc: Exception){
                false
            }
        }

        //Menampilkan Toast yg dipanggil secara universal
        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        //Menampilkan Toast dengan pesan tidak terhubung ke Internet secara universal
        fun errorNoConnection(context: Context) {
            Toast.makeText(context, "Anda tidak terhubung ke internet", Toast.LENGTH_SHORT).show()
        }
    }
}