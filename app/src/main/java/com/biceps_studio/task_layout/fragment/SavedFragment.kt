package com.biceps_studio.task_layout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.biceps_studio.task_layout.adapter.JobsAdapter
import com.biceps_studio.task_layout.R
import com.biceps_studio.task_layout.`interface`.SavedFragmentListener
import com.biceps_studio.task_layout.activity.MainActivity
import com.biceps_studio.task_layout.data.JobModel
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*

class SavedFragment : Fragment() {

    val list: ArrayList<JobModel> = ArrayList()
    val jobsAdapter = JobsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initEvent()
    }

    private fun initEvent() {
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.savedFragmentListener = object : SavedFragmentListener {
            override fun onSearch(string: CharSequence) {
                if (string.isNotEmpty()){
                    val arrayList: ArrayList<JobModel> = ArrayList()

                    for (jobModel: JobModel in list){
                        if (jobModel.title.toLowerCase(Locale.getDefault()).contains(string.toString().toLowerCase(
                                Locale.getDefault()))){
                            arrayList.add(jobModel)
                        }
                    }

                    jobsAdapter.updateData(arrayList)
                } else {
                    jobsAdapter.updateData(list)
                }
            }
        }
    }

    private fun initRecyclerView() {
        list.add(
            JobModel(
                "Tokopedia",
                "https://upload.wikimedia.org/wikipedia/id/1/13/Tokopedia_Icon.png"
            )
        )
        list.add(
            JobModel(
                "Slack",
                "https://user-images.githubusercontent.com/819186/51553744-4130b580-1e7c-11e9-889e-486937b69475.png"
            )
        )
        list.add(
            JobModel(
                "Tokopedia",
                "https://upload.wikimedia.org/wikipedia/id/1/13/Tokopedia_Icon.pngasdaf"
            )
        )
        list.add(
            JobModel(
                "Slack",
                "https://user-images.githubusercontent.com/819186/51553744-4130b580-1e7c-11e9-889e-486937b69475.png"
            )
        )
        list.add(
            JobModel(
                "Tokopedia",
                "https://upload.wikimedia.org/wikipedia/id/1/13/Tokopedia_Icon.png"
            )
        )
        list.add(
            JobModel(
                "Slack",
                "https://user-images.githubusercontent.com/819186/51553744-4130b580-1e7c-11e9-889e-486937b69475.pngasda"
            )
        )

        jobsAdapter.list = list

        val linearLayoutManager = LinearLayoutManager(activity!!)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        rvList.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = jobsAdapter
        }
    }
}
