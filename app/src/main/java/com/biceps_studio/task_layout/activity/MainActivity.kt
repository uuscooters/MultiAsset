package com.biceps_studio.task_layout.activity

import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.biceps_studio.task_layout.fragment.JobsFragment
import com.biceps_studio.task_layout.R
import com.biceps_studio.task_layout.`interface`.JobsFragmentListener
import com.biceps_studio.task_layout.`interface`.SavedFragmentListener
import com.biceps_studio.task_layout.fragment.ApiFragment
import com.biceps_studio.task_layout.fragment.SQLiteFragment
import com.biceps_studio.task_layout.fragment.SavedFragment
import com.biceps_studio.task_layout.utils.Local
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    var jobsFragmentListener: JobsFragmentListener? = null
    var savedFragmentListener: SavedFragmentListener? = null

    private lateinit var locale: Locale
    private lateinit var configuration: Configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configuration = Resources.getSystem().configuration

        val id: String = Local.getLocal(this)

        if (id == "en") {
            btnEn.setBackgroundColor(getColor(android.R.color.darker_gray))
        } else {
            btnIn.setBackgroundColor(getColor(android.R.color.darker_gray))
        }

        btnEn.setOnClickListener {
            locale = Locale( "en")
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)

            Local.saveLocal(this, locale.language)

            recreate()
        }

        btnIn.setOnClickListener {
            locale = Locale("in")
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)

            recreate()
        }

        initFragment()
        initEvent()
    }

    private fun initEvent() {
        etSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                jobsFragmentListener!!.onSearch(p0!!)
                savedFragmentListener!!.onSearch(p0)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.e("beforeTextChanged", p0!!.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.e("onTextChanged", p0!!.toString())
            }
        })
    }

    private fun initFragment() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(JobsFragment(), "For you")
        adapter.addFragment(SavedFragment(), "Saved")
        adapter.addFragment(ApiFragment(), "REST API")
        adapter.addFragment(SQLiteFragment(), "SQLite")

        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private var fragmentList: ArrayList<Fragment> = ArrayList()
        private var fragmentTitleList: ArrayList<String> = ArrayList()

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {

            return fragmentList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList[position]
        }
    }
}
