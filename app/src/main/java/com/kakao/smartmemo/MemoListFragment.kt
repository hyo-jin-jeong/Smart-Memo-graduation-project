package com.kakao.smartmemo

import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MemoListFragment : Fragment() {
    lateinit var recyclerView1 : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.memo_list_fragment, container, false)

        recyclerView1 = view.findViewById(R.id.rv_memo_list!!)as RecyclerView
        recyclerView1.adapter = MemoListAdapter()
        recyclerView1.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="Memo List"

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_group_in_memolist, menu)
        //menu?.getItem(1)?.setIcon(context?.let { ContextCompat.getDrawable(it, R.drawable.camera) })
        //menu.add("여행").setIcon(R.drawable.group_color) 메모 동적 생성시 참고
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }

            R.id.action_settings2 -> {
                (activity as MainActivity).toolbar.title=item.title

                return true
            }

            R.id.action_settings3 -> {
                (activity as MainActivity).toolbar.title=item.title

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}