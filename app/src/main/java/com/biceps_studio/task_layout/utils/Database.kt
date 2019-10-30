package com.biceps_studio.task_layout.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.biceps_studio.task_layout.data.PostModel

class Database(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    private lateinit var sqLiteDatabase: SQLiteDatabase

    override fun onCreate(p0: SQLiteDatabase?) {
        val query: String = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_BODY TEXT, " +
                "$COLUMN_USER_ID INTEGER)"

        p0!!.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_NAME"

        p0!!.execSQL(query)
    }

    companion object {
        const val DATABASE_NAME = "com.biceps_studio.task_layout.utils.db"
        const val VERSION = 1

        const val TABLE_NAME = "POST_MODEL"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_BODY = "body"
        const val COLUMN_USER_ID = "userId"
    }

    fun insertPost(postModel: PostModel) {
        val values: ContentValues = ContentValues().apply {
            put(COLUMN_ID, postModel.id)
            put(COLUMN_TITLE, postModel.title)
            put(COLUMN_BODY, postModel.body)
            put(COLUMN_USER_ID, postModel.userId)
        }

        sqLiteDatabase = writableDatabase
        sqLiteDatabase.insert(TABLE_NAME, null, values)
        sqLiteDatabase.close()
    }

    fun updatePost(postModel: PostModel) {
        val values: ContentValues = ContentValues().apply {
            put(COLUMN_TITLE, postModel.title)
            put(COLUMN_BODY, postModel.body)
            put(COLUMN_USER_ID, postModel.userId)
        }

        val where = "$COLUMN_ID = '${postModel.id}'"

        sqLiteDatabase = writableDatabase
        sqLiteDatabase.update(TABLE_NAME, values, where, null)
        sqLiteDatabase.close()
    }

    fun deletePost(id: Int){
        val where = "$COLUMN_ID = '$id'"

        sqLiteDatabase = writableDatabase
        sqLiteDatabase.delete(TABLE_NAME, where, null)
        sqLiteDatabase.close()
    }

    @SuppressLint("Recycle")
    fun getAllPost() : ArrayList<PostModel> {
        val arrayList : ArrayList<PostModel> = ArrayList()

        sqLiteDatabase = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME"

        val cursor: Cursor = sqLiteDatabase.rawQuery(query, null)
        cursor.moveToFirst()

        for (i: Int in 0 until cursor.count) {
            arrayList.add(catchPostModel(cursor))

            cursor.moveToNext()
        }

        cursor.close()

        sqLiteDatabase.close()

        return arrayList
    }

    @SuppressLint("Recycle")
    fun getPost(id: Int) : PostModel {
        sqLiteDatabase = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = '$id'"

        val cursor: Cursor = sqLiteDatabase.rawQuery(query, null)
        cursor.moveToFirst()

        sqLiteDatabase.close()

        return catchPostModel(cursor)
    }

    @SuppressLint("Recycle")
    fun isExist(id: Int) : Boolean {
        sqLiteDatabase = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = '$id'"

        val cursor: Cursor = sqLiteDatabase.rawQuery(query, null)
        cursor.moveToFirst()

        val isExist: Boolean = cursor.count == 1

        cursor.close()

        sqLiteDatabase.close()

        return isExist
    }

    private fun getString(cursor: Cursor, string: String): String { return cursor.getString(cursor.getColumnIndex(string)) }

    private fun getInt(cursor: Cursor, string: String): Int { return cursor.getInt(cursor.getColumnIndex(string)) }

    fun catchPostModel(cursor: Cursor) : PostModel {
        val postModel = PostModel(
            getString(cursor, COLUMN_BODY),
            getInt(cursor, COLUMN_ID),
            getString(cursor, COLUMN_TITLE),
            getInt(cursor, COLUMN_USER_ID)
        )

        return postModel
    }
}