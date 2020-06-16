package com.kakao.smartmemo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.kakao.smartmemo.Adapter.ManagementAdapter
import com.kakao.smartmemo.Contract.ManagementFragmentContract
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Object.UserObject
import com.kakao.smartmemo.Presenter.ManagementFragmentPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.activity_main.*

class ManagementFragment : Fragment(), ManagementFragmentContract.View {

    private lateinit var presenter : ManagementFragmentContract.Presenter
    lateinit var userEmail : TextView
    lateinit var userName : TextView
    lateinit var userAddr : TextView
    lateinit var kakaoAlarmTime : TextView
    lateinit var changeInfo : TextView
    lateinit var cont : Context
    lateinit var plusBtn : ImageView
    private lateinit var folderListView : ListView
    private lateinit var adapter : ManagementAdapter
    private  var folderList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        folderList.clear()
        setAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.management_fragment, container, false)
        presenter = ManagementFragmentPresenter(this)
        cont = view.context


        folderListView = view.findViewById(R.id.folder_list) as ListView

        userEmail = view.findViewById(R.id.user_email)
        userName = view.findViewById(R.id.user_name)
        userAddr = view.findViewById(R.id.user_addr)
        kakaoAlarmTime = view.findViewById(R.id.kakao_alarm_time_text)
        changeInfo = view.findViewById(R.id.change_info)
        plusBtn = view.findViewById(R.id.add_folder)
        folderListView.isClickable = true


        changeInfo.setOnClickListener {
            val memberDataChange = Intent(view.context, MemberDataChange::class.java)
            startActivity(memberDataChange)
        }

        folderListView.setOnItemClickListener { parent, view, position, id ->//그룹 아이템 누르면 그룹 설정
            var groupSettingIntent = Intent(view.context, ModifyGroup::class.java)
            groupSettingIntent.putExtra("folderId", folderList[position])
            startActivity(groupSettingIntent)
        }

        plusBtn.setOnClickListener {
            val nextIntent = Intent(view.context, AddFolder::class.java)
            startActivity(nextIntent)
        }
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="더보기"
        (activity as MainActivity).fab.visibility = View.GONE
        (activity as MainActivity).fab_todo.visibility = View.INVISIBLE
        (activity as MainActivity).fab_memo.visibility = View.INVISIBLE
    }

    private fun setAdapter(){
        FolderObject.folderInfo.forEach {
            folderList.add(it.key)
        }
        userEmail.text = UserObject.email
        userName.text = UserObject.user_name
        userAddr.text = UserObject.addr
        if (UserObject.kakao_alarm_time == "") {
            kakaoAlarmTime.text = "시간 설정 안함"
        } else {
            kakaoAlarmTime.text = UserObject.kakao_alarm_time
        }

        adapter = ManagementAdapter(cont,folderList)
        folderListView.adapter = adapter
        presenter.setManagementAdapterModel(adapter)
        presenter.setManagementAdapterView(adapter)
        adapter.notifyAdapter()
    }
}