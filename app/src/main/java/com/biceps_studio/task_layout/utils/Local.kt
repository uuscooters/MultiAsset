package com.biceps_studio.task_layout.utils

import android.content.Context
import android.content.SharedPreferences

class Local {

    companion object{
        const val NAME = "com.biceps_studio.task_layout.utils"
        const val PRIVATE_MODE = 0

        const val LOCALE = "locale"

        fun saveLocal(context: Context, id: String) {

            val sharedPreferences : SharedPreferences = context.getSharedPreferences(NAME, PRIVATE_MODE)
            val editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(LOCALE, id)
            editor.apply()

        }

        fun getLocal(context: Context): String {
            return context.getSharedPreferences(NAME, PRIVATE_MODE).getString(LOCALE, "en")!!
        }


    }
}