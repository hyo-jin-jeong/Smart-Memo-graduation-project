package com.kakao.smartmemo.View

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.smartmemo.Adapter.TodoAdapter
import com.kakao.smartmemo.Adapter.TodoDeleteAdapter
import com.kakao.smartmemo.Contract.TodoContract
import com.kakao.smartmemo.Data.TodoData
import com.kakao.smartmemo.Object.GroupObject
import com.kakao.smartmemo.Presenter.TodoPresenter
import com.kakao.smartmemo.R
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime

class TodoListFragment : Fragment(), TodoContract.View {

    private lateinit var presenter : TodoContract.Presenter
    private lateinit var todolist : ListView
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var textViewTodoList : TextView
    private lateinit var adapter : TodoAdapter
    private lateinit var deleteAdapter : TodoDeleteAdapter
    private lateinit var cont: Context
    private var todoArrayList = mutableListOf<TodoData>()
    val date: LocalDateTime = LocalDateTime.now()
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todolist_fragment, container, false)
        cont = view.context // view의 컨텍스트
        bottomNavigationView = view.findViewById(R.id.navigationview_bottom)
        textViewTodoList = view.findViewById(R.id.textView_todolist)

        presenter = TodoPresenter(this)
        todolist = view.findViewById(R.id.todolist) as ListView
        todolist.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(cont, AddTodo::class.java)
            intent.putExtra("todoData", todoArrayList[position])
            startActivity(intent)
        }

        //하단 메뉴
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.removeItem -> {
                    var listview_count = deleteAdapter.count
                    var checkedItems = deleteAdapter.selectedTodo()
                    if(checkedItems != null) {
                        for (i in listview_count - 1 downTo 0) {
                            for (j in checkedItems) {
                                if (i == j) {
                                    deleteAdapter.deleteTodo(i)
                                    Log.v("seyuuuun", i.toString())
                                }
                            }
                        }
                    }
                    todolist.clearChoices()
                    adapter.notifyAdapter()
                    bottomNavigationView.visibility = GONE //하단메뉴 안보이게
                    todolist.adapter = TodoAdapter(cont, todoArrayList)
                    count++
                    true
                }
                R.id.cancelItem -> {
                    showAllTodo(todoArrayList)
                    true
                }
            }
            true
        }
        bottomNavigationView.visibility = GONE; //하단메뉴 안보이게

        return view
    }

    override fun onStart() {
        super.onStart()
        presenter.getTodo()
        adapter = TodoAdapter(cont, todoArrayList)
        todolist.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        todolist.isClickable = true
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        (activity as MainActivity).toolbar.title="Todo List"
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.select_group_in_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                (activity as MainActivity).mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.select_group -> {
                selectGroup()
                return true
            }
            R.id.delete_memo ->{
                count++
                if(count%2 == 0) {
                    showAllTodo(todoArrayList)
                } else {
                    deleteTodo()
                }

                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    private fun selectGroup(){
        var i = 1
        val items:Array<CharSequence> = Array(GroupObject.groupInfo.size+1) {""}
        val groupIds:Array<CharSequence> = Array(GroupObject.groupInfo.size+1) {""}
        items[0] = "전체메모"
        GroupObject.groupInfo.forEach {
            groupIds[i] = it.key
            items[i] = it.value
            i++
        }

        val listDialog: AlertDialog.Builder = AlertDialog.Builder(
            this.context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
        )
        listDialog.setTitle("그룹 선택")
            .setItems(items, DialogInterface.OnClickListener { _, which ->
                if (which == 0) {
                    (activity as MainActivity).toolbar.title = "Todo List"
                    presenter.getTodo()
                } else {
                    (activity as MainActivity).toolbar.title = items[which]
                    presenter.getGroupTodo(groupIds[which].toString())
                }
            })
            .show()
    }

    override fun showAllTodo(todoData: MutableList<TodoData>) {
        count = 0
        if (todoData.isEmpty()) {
            todoArrayList.clear()
        } else {
            todoArrayList = todoData
        }
        adapter = TodoAdapter(cont, todoArrayList)
        todolist.adapter = adapter
        presenter.setTodoAdapterModel(adapter)
        presenter.setTodoAdapterView(adapter)
        bottomNavigationView.visibility = GONE
        Log.e("todoData", todoData.toString())
    }

    private fun deleteTodo() {
        deleteAdapter = TodoDeleteAdapter(cont, todoArrayList)
        todolist.adapter = deleteAdapter
        presenter.setTodoDeleteAdapterModel(deleteAdapter)
        presenter.setTodoDeleteAdapterView(deleteAdapter)
        bottomNavigationView.visibility = VISIBLE //하단메뉴 보이게
    }
}