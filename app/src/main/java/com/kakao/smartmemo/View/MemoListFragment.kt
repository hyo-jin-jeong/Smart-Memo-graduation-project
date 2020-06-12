package com.kakao.smartmemo.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.smartmemo.Adapter.MemoListAdapter
import com.kakao.smartmemo.Adapter.MemoListDeleteAdapter
import com.kakao.smartmemo.Contract.MemoContract
import com.kakao.smartmemo.Data.MemoData
import com.kakao.smartmemo.Object.FolderObject
import com.kakao.smartmemo.Presenter.MemoPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MemoListFragment : Fragment(), MemoContract.View {
    private lateinit var presenter: MemoPresenter
    private lateinit var recyclerView1: RecyclerView
    private lateinit var memoAdapter: MemoListAdapter
    private lateinit var memoDeleteAdapter: MemoListDeleteAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private var memoList: MutableList<MemoData> = mutableListOf()
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        presenter.getAllMemo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.memo_list_fragment, container, false)
        presenter = MemoPresenter(this)
        recyclerView1 = view.findViewById(R.id.rv_memo_list!!) as RecyclerView
        bottomNavigationView = view.findViewById(R.id.navigationview_bottom)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title = "Memo List"
        (activity as MainActivity).fab.visibility = View.VISIBLE
        (activity as MainActivity).fab_todo.visibility = View.VISIBLE
        (activity as MainActivity).fab_memo.visibility = View.VISIBLE
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_group_in_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.select_group -> {
                selectGroup()
                return true
            }
            R.id.delete_memo -> {
                count++
                if (count % 2 == 0) {
                    presenter.getAllMemo()
                    count = 0
                } else {
                    deleteMemo()
                }
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun showMemoItem(position: Int) {
        var intent = Intent(view?.context, ShowMemo::class.java)
        intent.putExtra("memoData", memoList[position])
        startActivity(intent)
    }

    override fun showAllMemo(memoList: MutableList<MemoData>) {
        count = 0
        this.memoList.clear()
        if (memoList.isEmpty()) {
            memoList.clear()
        } else {
            this.memoList = memoList
        }
        memoAdapter = MemoListAdapter(this.memoList)
        recyclerView1.adapter = memoAdapter
        presenter.setMemoAdapterModel(memoAdapter)
        presenter.setMemoAdapterView(memoAdapter)
        recyclerView1.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        bottomNavigationView.visibility = View.GONE
        memoAdapter.notifyAdapter()
    }

    private fun deleteMemo() {
        if (memoList.size != 0) {
            //하단 메뉴
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.removeItem -> {
                        var deleteMemoList = memoDeleteAdapter.deleteMemo()
                        presenter.deleteMemo(deleteMemoList)
                        presenter.getAllMemo()
                        true
                    }
                    R.id.cancelItem -> {
                        showAllMemo(memoList)
                        true
                    }
                }
                true
            }
            memoDeleteAdapter = MemoListDeleteAdapter(memoList)
            recyclerView1.adapter = memoDeleteAdapter
            presenter.setMemoDeleteAdapterModel(memoDeleteAdapter)
            presenter.setMemoDeleteAdapterView(memoDeleteAdapter)
            recyclerView1.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            bottomNavigationView.visibility = View.VISIBLE
        }

    }

    private fun selectGroup() {
        var i = 1
        val value: Array<CharSequence> = Array(FolderObject.folderInfo.size + 1) { "" }
        val key: Array<CharSequence> = Array(FolderObject.folderInfo.size + 1) { "" } // folderId
        value[0] = "전체메모"
        FolderObject.folderInfo.forEach {
            value[i] = it.value
            key[i] = it.key
            i++
        }
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(
            this.context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
        listDialog.setTitle("폴더 선택")
            .setItems(value, DialogInterface.OnClickListener { _, which ->
                if (which == 0) {
                    (activity as MainActivity).toolbar.title = "Memo List"
                    presenter.getAllMemo()
                } else {
                    (activity as MainActivity).toolbar.title = value[which]
//                    FolderObject.selectFolderInfo = key[which].toString()
                    presenter.getFolderMemo(key[which].toString())
                }

            })
            .show()
    }

    override fun onSuccess() {
        showAllMemo(memoList)
    }

}