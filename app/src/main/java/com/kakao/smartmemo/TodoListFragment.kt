package com.kakao.smartmemo

import android.graphics.Paint
import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.time_location_settings.*
import kotlinx.android.synthetic.main.todo_list_item.*
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoListFragment : Fragment() {

    private lateinit var todolist : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todolist_fragment, container, false)

        todolist = view.findViewById(R.id.todolist) as RecyclerView
        todolist.adapter = TodoAdapter()
        todolist.layoutManager = LinearLayoutManager(view.context)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="Todo List"
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_todolist_remove, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.action_todolist_remove -> {
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }
}